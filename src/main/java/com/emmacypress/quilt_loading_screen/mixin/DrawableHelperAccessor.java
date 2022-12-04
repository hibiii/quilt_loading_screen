/*
 * Copyright (c) 2021 darkerbit
 * Copyright (c) 2021, 2022 triphora
 *
 * Quilt Loading Screen is under the MIT License. See LICENSE for details.
 */

package com.emmacypress.quilt_loading_screen.mixin;

import net.minecraft.client.gui.DrawableHelper;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DrawableHelper.class)
public interface DrawableHelperAccessor {
	@Invoker("m_tkjjzcwb")
	static void quiltLoadingScreen$drawTexturedQuad(Matrix4f matrices, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1) {
		throw new UnsupportedOperationException();
	}
}
