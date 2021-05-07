package io.github.darkerbit.quiltloadingscreen.mixin;

import io.github.darkerbit.quiltloadingscreen.QuiltLoadingScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceReload;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(SplashScreen.class)
public abstract class SplashScreenMixin extends Overlay {
    @Final
    @Shadow
    private MinecraftClient client;

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
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SplashScreen;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"),
            index = 5
    )
    private int changeColor(int in) {
        return (in & (0xFF << 24) | QuiltLoadingScreen.BACKGROUND_COLOR); // Use existing transparency
    }

    // Render before first texture set to render before the logo
    @Inject(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void renderPatches(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci,
                               int i, int j, long l, float f) {
        quiltLoadingScreen$loadingScreen.renderPatches(matrices, delta, f >= 1.0f);
    }
}
