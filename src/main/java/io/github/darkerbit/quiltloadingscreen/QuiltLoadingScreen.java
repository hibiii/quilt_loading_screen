package io.github.darkerbit.quiltloadingscreen;

import io.github.darkerbit.quiltloadingscreen.mixin.DrawableHelperAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Random;

public class QuiltLoadingScreen {
    public static final int BACKGROUND_COLOR = BackgroundHelper.ColorMixer.getArgb(0, 35, 22, 56);

    private static final Identifier PATCH_TEXTURE =
            new Identifier("quilt-loading-screen", "textures/gui/patch.png");

    private static final Identifier PRIDE_TEXTURE =
            new Identifier("quilt-loading-screen", "textures/gui/community_quilt.png");

    private static final int PATCH_COUNT = 16;

    private final int patchSize;

    private final MinecraftClient client;

    private final Random random = new Random();

    private final ArrayList<FallingPatch> fallingPatches = new ArrayList<>();

    private float patchTimer = 0f;

    private final boolean prideMonth;

    public QuiltLoadingScreen(MinecraftClient client) {
        this.client = client;

        prideMonth = LocalDate.now().getMonth() == Month.JUNE;

        patchSize = prideMonth ? 20 : 24;

        createPatch(prideMonth ? 12 : 8); // summons the holy pineapple
    }

    public void createPatch(int type) {
        fallingPatches.add(new FallingPatch(
                random.nextDouble() * this.client.getWindow().getScaledWidth(), -patchSize, 0,
                (random.nextDouble() - 0.5) * 0.6,
                random.nextDouble() * 3.0 + 1.0,
                (random.nextDouble() - 0.5) * 6.0,
                random.nextDouble() / 2 + 0.5,
                type, patchSize
        ));
    }

    public void updatePatches(MatrixStack matrices, float delta, boolean ending) {
        for (FallingPatch patch : fallingPatches) {
            if (ending)
                patch.fallSpeed *= 1.0 + delta / 3;

            patch.update(delta);
        }

        patchTimer -= delta;

        if (patchTimer < 0f && !ending) {
            createPatch(random.nextInt(prideMonth ? 12 : 8));

            patchTimer = random.nextFloat();
        }
    }

    public void renderPatches(MatrixStack matrices, float delta, boolean ending) {
        // spike prevention
        if (delta < 2.0f)
            updatePatches(matrices, delta, ending);

        client.getTextureManager().bindTexture(prideMonth ? PRIDE_TEXTURE : PATCH_TEXTURE);

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

        private int patchSize;

        public FallingPatch(double x, double y, double rot, double horizontal, double fallSpeed, double rotSpeed, double scale, int type, int patchSize) {
            this.x = x;
            this.y = y;
            this.rot = rot;

            this.horizontal = horizontal;
            this.fallSpeed = fallSpeed;
            this.rotSpeed = rotSpeed;

            this.scale = scale;

            this.type = type;

            this.patchSize = patchSize;
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

            double x1 = -patchSize / (double) 2;
            double y1 = -patchSize / (double) 2;
            double x2 = patchSize / (double) 2;
            double y2 = patchSize / (double) 2;

            float u0 = 1.0f / PATCH_COUNT * type;
            float u1 = u0 + 1.0f / PATCH_COUNT;

            DrawableHelperAccessor.quiltLoadingScreen$drawTexturedQuad(
                    matrix,
                    (int) x1, (int) x2, (int) y1, (int) y2, 0,
                    u0, u1, 0.0f, 0.5f
            );

            matrices.pop();
        }
    }
}
