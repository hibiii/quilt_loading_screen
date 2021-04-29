package io.github.darkerbit.quiltloadingscreen.mixin;

import io.github.darkerbit.quiltloadingscreen.QuiltLoadingScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceReloadMonitor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(SplashScreen.class)
public abstract class SplashScreenMixin extends Overlay {
    private QuiltLoadingScreen quiltLoadingScreen$loadingScreen;

    @Inject(
            method = "<init>(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/resource/ResourceReloadMonitor;Ljava/util/function/Consumer;Z)V",
            at = @At("TAIL")
    )
    public void constructor(MinecraftClient client, ResourceReloadMonitor monitor, Consumer<Optional<Throwable>> exceptionHandler, boolean reloading, CallbackInfo ci) {
        quiltLoadingScreen$loadingScreen = new QuiltLoadingScreen(client);
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

    // Render before first texture bind, which is used for rendering the logo
    @Inject(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getTextureManager()Lnet/minecraft/client/texture/TextureManager;")
    )
    private void renderPatches(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        quiltLoadingScreen$loadingScreen.renderPatches(matrices, delta);
    }
}
