/*
 * Copyright (c) 2021, 2022, 2023 darkerbit
 * Copyright (c) 2021, 2022, 2023 triphora
 *
 * Quilt Loading Screen is under the MIT License. See LICENSE for details.
 */

package com.emmacypress.quilt_loading_screen;

import com.emmacypress.quilt_loading_screen.mixin.GuiGraphicsAccessor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Random;

public class QuiltLoadingScreen {
	public static final String MODID = "quilt_loading_screen";

	private final MinecraftClient client;
	private final int patchesInTextures, patchSize, patchCount;
	private final Identifier texture;

	private final Random random = new Random();
	private final ArrayList<FallingPatch> fallingPatches = new ArrayList<>();

	private float patchTimer = 0f;

	public QuiltLoadingScreen(MinecraftClient client) {
		boolean prideMonth = Config.prideQuiltsEnabled || LocalDate.now().getMonth() == Month.JUNE;

		this.client = client;
		this.patchesInTextures = prideMonth ? 19 : 12;
		this.patchSize = prideMonth ? 20 : 24;
		this.patchCount = prideMonth ? 32 : 16;
		this.texture = prideMonth ? id("textures/gui/pride_patches.png") : id("textures/gui/patches.png");

		createPatch(patchesInTextures); // summons the holy pineapple
	}

	public void createPatch(int type) {
		fallingPatches.add(new FallingPatch(
			random.nextDouble() * this.client.getWindow().getScaledWidth(), -patchSize, 0,
			(random.nextDouble() - 0.5) * 0.6,
			random.nextDouble() * 3.0 + 1.0,
			(random.nextDouble() - 0.5) / 6.0,
			random.nextDouble() / 2 + 0.5,
			type, patchSize
		));
	}

	public void updatePatches(float delta, boolean ending) {
		for (FallingPatch patch : fallingPatches) {
			if (ending)
				patch.fallSpeed *= 1.0 + delta / 3;

			patch.update(delta);
		}

		patchTimer -= delta;

		if (patchTimer < 0f && !ending) {
			createPatch(random.nextInt(patchesInTextures));

			patchTimer = random.nextFloat();
		}
	}

	public void renderPatches(GuiGraphics graphics, float delta, boolean ending) {
		// spike prevention
		if (delta < 2.0f)
			updatePatches(delta, ending);

		RenderSystem.defaultBlendFunc();

		MatrixStack matrices = graphics.getMatrices();

		for (FallingPatch patch : fallingPatches) {
			patch.render(graphics, matrices, client.options.getMonochromeLogo().get());
		}
	}

	static Identifier id(String id) {
		return new Identifier(MODID, id);
	}

	private class FallingPatch {
		private double x, y, rot;
		private final int type;

		private final double horizontal, rotSpeed;
		private final double scale;

		public double fallSpeed;

		private final int patchSize;

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

		public void render(GuiGraphics graphics, MatrixStack matrices, boolean monochrome) {
			matrices.push();
			matrices.translate(x, y, 0);

			matrices.peek().getModel().rotateZ((float) rot);
			matrices.scale((float) scale, (float) scale, (float) scale);

			double x1 = -patchSize / 2d;
			double y1 = -patchSize / 2d;
			double x2 = patchSize / 2d;
			double y2 = patchSize / 2d;

			float u0 = 1.0f / patchCount * type;
			float u1 = u0 + 1.0f / patchCount;

			float offset = monochrome ? 0.5f : 0.0f;

			((GuiGraphicsAccessor) graphics).quiltLoadingScreen$drawTexturedQuad(
				texture,
				(int) x1, (int) x2, (int) y1, (int) y2, 0,
				u0, u1, 0.0f + offset, 0.5f + offset
			);

			matrices.pop();
		}
	}
}
