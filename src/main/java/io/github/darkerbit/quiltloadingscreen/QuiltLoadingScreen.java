package io.github.darkerbit.quiltloadingscreen;

import io.github.darkerbit.quiltloadingscreen.mixin.DrawableHelperAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;

import java.util.ArrayList;
import java.util.Random;

public class QuiltLoadingScreen {
    public static final int BACKGROUND_COLOR = BackgroundHelper.ColorMixer.getArgb(0, 35, 22, 56);

    private static final Identifier PATCH_TEXTURE =
            new Identifier("quilt-loading-screen", "textures/gui/patch.png");

    private static final int PATCH_SIZE = 24;
    private static final int PATCH_COUNT = 8;

    private final MinecraftClient client;

    private final Random random = new Random();

    private final ArrayList<FallingPatch> fallingPatches = new ArrayList<>();

    private float patchTimer = 0f;

    public QuiltLoadingScreen(MinecraftClient client) {
        this.client = client;
    }

    public void updatePatches(MatrixStack matrices, float delta, boolean ending) {
        for (FallingPatch patch : fallingPatches) {
            if (ending)
                patch.fallSpeed *= 1.0 + delta / 3;

            patch.update(delta);
        }

        patchTimer -= delta;

        if (patchTimer < 0f && !ending) {
            fallingPatches.add(new FallingPatch(
                    random.nextDouble() * this.client.getWindow().getScaledWidth(), -PATCH_SIZE, 0,
                    (random.nextDouble() - 0.5) * 0.6,
                    random.nextDouble() * 3.0 + 1.0,
                    (random.nextDouble() - 0.5) * 6.0,
                    random.nextDouble() / 2 + 0.5,
                    random.nextInt(8)
            ));

            patchTimer = random.nextFloat();
        }
    }

    public void renderPatches(MatrixStack matrices, float delta, boolean ending) {
        // spike prevention
        if (delta < 2.0f)
            updatePatches(matrices, delta, ending);

        client.getTextureManager().bindTexture(PATCH_TEXTURE);

        for (FallingPatch patch : fallingPatches) {
            patch.render(matrices);
        }
    }

    private static class FallingPatch {
        private double x, y, rot;
        private final int type;

        private final double horizontal, rotSpeed;
        private final double scale;

        public double fallSpeed;

        public FallingPatch(double x, double y, double rot, double horizontal, double fallSpeed, double rotSpeed, double scale, int type) {
            this.x = x;
            this.y = y;
            this.rot = rot;

            this.horizontal = horizontal;
            this.fallSpeed = fallSpeed;
            this.rotSpeed = rotSpeed;

            this.scale = scale;

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
            matrix.multiply(Matrix4f.scale((float) scale, (float) scale, (float) scale));

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
