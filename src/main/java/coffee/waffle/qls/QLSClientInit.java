/*
 * Copyright (c) 2021 darkerbit
 * Copyright (c) 2021, 2022 wafflecoffee
 *
 * Quilt Loading Screen is under the MIT License. See LICENSE for details.
 */

package coffee.waffle.qls;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType;

import static coffee.waffle.qls.QuiltLoadingScreen.MODID;
import static coffee.waffle.qls.QuiltLoadingScreen.id;

/**
 * This should be the only class in the entire mod that interacts with anything not from MC, Mixin, config, or stdlib.
 */
public class QLSClientInit implements ClientModInitializer {
  @Override
  public void onInitializeClient(ModContainer mod) {
    MidnightConfig.init(MODID, Config.class);

    ResourceLoader.registerBuiltinResourcePack(id("quilt-ui"), mod, ResourcePackActivationType.NORMAL);
  }
}
