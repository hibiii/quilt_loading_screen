/*
 * Copyright (c) 2021, 2022, 2023 darkerbit
 * Copyright (c) 2021, 2022, 2023 triphora
 *
 * Quilt Loading Screen is under the MIT License. See LICENSE for details.
 */

package com.emmacypress.quilt_loading_screen.mixin;

import net.minecraft.client.gui.screen.SplashOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SplashOverlay.class)
public interface SplashOverlayAccessor {
	@Mutable
	@Accessor("MOJANG_RED")
	static void setMojangRed(int newValue) {
		throw new UnsupportedOperationException();
	}
}
