package io.github.darkerbit.quiltloadingscreen;

import io.github.darkerbit.quiltloadingscreen.mixin.DrawableHelperAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;

import java.util.ArrayList;

public class QuiltLoadingScreen {
    public static final int BACKGROUND_COLOR = BackgroundHelper.ColorMixer.getArgb(0, 35, 22, 56);

    private static final Identifier PATCH_TEXTURE =
            new Identifier("quilt-loading-screen", "textures/gui/patch.png");

    private static final int PATCH_SIZE = 24;
    private static final int PATCH_COUNT = 8;

    private final MinecraftClient client;

    private final ArrayList<FallingPatch> fallingPatches = new ArrayList<>();

    // TODO: Create system for endless patches
    private boolean init = true;

    public QuiltLoadingScreen(MinecraftClient client) {
        this.client = client;
    }

    public void updatePatches(MatrixStack matrices, float delta) {
        if (init) {
            fallingPatches.add(new FallingPatch(64, -PATCH_SIZE, 0, 0.3f, 2.0f, 2.0f, 7));
            init = false;
        }

        for (FallingPatch patch : fallingPatches) {
            patch.update(delta);
        }
    }

    public void renderPatches(MatrixStack matrices, float delta) {
        updatePatches(matrices, delta);

        client.getTextureManager().bindTexture(PATCH_TEXTURE);

        for (FallingPatch patch : fallingPatches) {
            patch.render(matrices);
        }
    }

    private static class FallingPatch {
        private double x, y, rot;
        private final int type;

        private final double horizontal, fallSpeed, rotSpeed;

        public FallingPatch(double x, double y, double rot, double horizontal, double fallSpeed, double rotSpeed, int type) {
            this.x = x;
            this.y = y;
            this.rot = rot;

            this.horizontal = horizontal;
            this.fallSpeed = fallSpeed;
            this.rotSpeed = rotSpeed;

            this.type = type;
        }

        public void update(float delta) {
            x += horizontal * delta;
            y += fallSpeed * delta;

            rot += rotSpeed * delta;
        }

        public void render(MatrixStack matrices) {
            matrices.push();
            matrices.translate(x, y, 0);

            Matrix4f matrix = matrices.peek().getModel();
            matrix.multiply(new Quaternion(0.0f, 0.0f, (float) rot, true));

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
}
