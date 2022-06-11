/*
 * Copyright (c) 2021 darkerbit
 * Copyright (c) 2021, 2022 wafflecoffee
 *
 * Quilt Loading Screen is under the MIT License. See LICENSE for details.
 */

package coffee.waffle.qls.mixin;

import coffee.waffle.qls.QuiltLoadingScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceReload;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(SplashOverlay.class)
public abstract class SplashOverlayMixin extends Overlay {
  @Final
  @Shadow
  private MinecraftClient client;

  @Shadow
  private static int withAlpha(int color, int alpha) {
    throw new UnsupportedOperationException("Shadowed method somehow called outside mixin. Exorcise your computer.");
  }

  private QuiltLoadingScreen quiltLoadingScreen$loadingScreen;

  @Inject(
          method = "<init>(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/resource/ResourceReload;Ljava/util/function/Consumer;Z)V",
          at = @At("TAIL")
  )
  private void constructor(MinecraftClient client, ResourceReload monitor, Consumer<Optional<Throwable>> exceptionHandler, boolean reloading, CallbackInfo ci) {
    quiltLoadingScreen$loadingScreen = new QuiltLoadingScreen(this.client);
  }

  // Replace the colour used for the background fill of the splash screen
  @ModifyArg(
          method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
          at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SplashOverlay;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"),
          index = 5
  )
  private int changeColor(int in) {
    if (this.client.options.getMonochromeLogo().get())
      return in;

    return withAlpha(QuiltLoadingScreen.BACKGROUND_COLOR, in >> 24); // Use existing transparency
  }

  // For some reason Mojang decided to not use `fill` in a specific case so I have to replace a local variable
  @ModifyVariable(
          method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
          at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/function/IntSupplier;getAsInt()I", ordinal = 2),
          ordinal = 4 // int m (or int o according to mixin apparently)
  )
  private int changeColorGl(int in) {
    return this.client.options.getMonochromeLogo().get() ? in : QuiltLoadingScreen.BACKGROUND_COLOR;
  }

  // Render before shader texture set to render before the logo
  @Inject(
          method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
          at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V", ordinal = 0),
          locals = LocalCapture.CAPTURE_FAILSOFT
  )
  private void renderPatches(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci,
                             int i, int j, long l, float f) {
    quiltLoadingScreen$loadingScreen.renderPatches(matrices, delta, f >= 1.0f);
  }
}
