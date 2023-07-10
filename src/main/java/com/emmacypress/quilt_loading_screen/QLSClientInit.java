/*
 * Copyright (c) 2021, 2022, 2023 darkerbit
 * Copyright (c) 2021, 2022, 2023 triphora
 *
 * Quilt Loading Screen is under the MIT License. See LICENSE for details.
 */

package com.emmacypress.quilt_loading_screen;

import com.emmacypress.quilt_loading_screen.mixin.SplashOverlayAccessor;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.client.util.ColorUtil;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType;

import static com.emmacypress.quilt_loading_screen.QuiltLoadingScreen.MODID;
import static com.emmacypress.quilt_loading_screen.QuiltLoadingScreen.id;

/**
 * This should be the only class in the entire mod that interacts with anything loader-specific.
 */
public class QLSClientInit implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		MidnightConfig.init(MODID, Config.class);

		ResourceLoader.registerBuiltinResourcePack(id("quilt-ui"), mod, ResourcePackActivationType.NORMAL);

		if (Config.modifyBackgroundColor)
			SplashOverlayAccessor.setMojangRed(ColorUtil.ARGB32.getArgb(0, 35, 22, 56));
	}
}
