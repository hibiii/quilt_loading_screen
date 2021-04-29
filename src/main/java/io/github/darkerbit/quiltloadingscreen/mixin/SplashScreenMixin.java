package io.github.darkerbit.quiltloadingscreen.mixin;

import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.gui.screen.SplashScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SplashScreen.class)
public class SplashScreenMixin {
    private static final int QUILTLOADINGSCREEN_BACKGROUND = BackgroundHelper.ColorMixer.getArgb(0, 35, 22, 56);

    @ModifyArg(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SplashScreen;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"),
            index = 5
    )
    private int changeColor(int in) {
        return (in & (0xFF << 24) | QUILTLOADINGSCREEN_BACKGROUND); // Use existing transparency
    }
}
