package io.github.darkerbit.quiltloadingscreen;

import io.github.darkerbit.quiltloadingscreen.mixin.DrawableHelperAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class QuiltLoadingScreen {
    public static final int BACKGROUND_COLOR = BackgroundHelper.ColorMixer.getArgb(0, 35, 22, 56);

    private static final Identifier PATCH_TEXTURE =
            new Identifier("quilt-loading-screen", "textures/gui/patch.png");

    private static final int PATCH_SIZE = 32;
    private static final int PATCH_SIZE_TEX = 128;
    private static final int PATCH_COUNT = 8;

    private final MinecraftClient client;

    public QuiltLoadingScreen(MinecraftClient client) {
        this.client = client;
    }

    public void renderPatches(MatrixStack matrices, float delta) {
        client.getTextureManager().bindTexture(PATCH_TEXTURE);

        renderPatch(matrices, 64, 64, 45, 3);
    }

    public static void renderPatch(MatrixStack matrices, double x, double y, double rot, int type) {
        matrices.push();
        matrices.translate(x, y, 0);

        double r = Math.toRadians(rot);

        double x1 = -PATCH_SIZE / (double) 2;
        double y1 = -PATCH_SIZE / (double) 2;
        double x2 = PATCH_SIZE / (double) 2;
        double y2 = PATCH_SIZE / (double) 2;

        // Yarn mislabeled these arguments, it's x0 x1 y0 y1 not x0 y0 x1 y 1
        DrawableHelperAccessor.quiltLoadingScreen$drawTexture(
                matrices,
                (int) x1, (int) x2, (int) y1, (int) y2, 0,
                PATCH_SIZE_TEX, PATCH_SIZE_TEX,
                type * PATCH_SIZE_TEX, 0,
                PATCH_COUNT * PATCH_SIZE_TEX, PATCH_SIZE_TEX
        );

        matrices.pop();
    }
}
