package io.github.darkerbit.quiltloadingscreen;

import io.github.darkerbit.quiltloadingscreen.mixin.DrawableHelperAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;

public class QuiltLoadingScreen {
    public static final int BACKGROUND_COLOR = BackgroundHelper.ColorMixer.getArgb(0, 35, 22, 56);

    private static final Identifier PATCH_TEXTURE =
            new Identifier("quilt-loading-screen", "textures/gui/patch.png");

    private static final int PATCH_SIZE = 32;
    private static final int PATCH_COUNT = 8;

    private final MinecraftClient client;

    private double rot = 0;

    public QuiltLoadingScreen(MinecraftClient client) {
        this.client = client;
    }

    public void renderPatches(MatrixStack matrices, float delta) {
        client.getTextureManager().bindTexture(PATCH_TEXTURE);

        renderPatch(matrices, 64, 64, rot, 3);

        rot += delta * 2;
    }

    public static void renderPatch(MatrixStack matrices, double x, double y, double rot, int type) {
        matrices.push();
        matrices.translate(x, y, 0);

        Matrix4f matrix = matrices.peek().getModel();

        float r = (float) Math.toRadians(rot);
        Quaternion quaternion = new Quaternion(0.0f, 0.0f, r, false);

        matrix.multiply(quaternion);

        double x1 = -PATCH_SIZE / (double) 2;
        double y1 = -PATCH_SIZE / (double) 2;
        double x2 = PATCH_SIZE / (double) 2;
        double y2 = PATCH_SIZE / (double) 2;

        float u0 = 1.0f / PATCH_COUNT * type;
        float u1 = u0 + 1.0f / PATCH_COUNT;

        DrawableHelperAccessor.quiltLoadingScreen$drawTexturedQuad(
                matrix,
                (int) x1, (int) x2, (int) y1, (int) y2, 0,
                u0, u1, 0.0f, 1.0f
        );

        matrices.pop();
    }
}
