package io.github.darkerbit.quiltloadingscreen.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashScreen.class)
public abstract class SplashScreenMixin extends Overlay {
    private static final int QUILTLOADINGSCREEN_BACKGROUND = BackgroundHelper.ColorMixer.getArgb(0, 35, 22, 56);

    private static final Identifier QUILTLOADINGSCREEN_PATCHTEXTURE =
            new Identifier("quilt-loading-screen", "textures/gui/patch.png");

    @Final
    @Shadow
    private MinecraftClient client;

    // Replace the colour used for the background fill of the splash screen
    @ModifyArg(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SplashScreen;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"),
            index = 5
    )
    private int changeColor(int in) {
        return (in & (0xFF << 24) | QUILTLOADINGSCREEN_BACKGROUND); // Use existing transparency
    }

    // Render before first texture bind, which is used for rendering the logo
    @Inject(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getTextureManager()Lnet/minecraft/client/texture/TextureManager;")
    )
    private void renderPatches(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.client.getTextureManager().bindTexture(QUILTLOADINGSCREEN_PATCHTEXTURE);

        drawTexture(matrices, 0, 0, 0, 0, 128, 128);
    }
}
