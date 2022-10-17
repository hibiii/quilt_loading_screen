/*
 * Copyright (c) 2021 darkerbit
 * Copyright (c) 2021, 2022 wafflecoffee
 *
 * Quilt Loading Screen is under the MIT License. See LICENSE for details.
 */

package coffee.waffle.qls;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import eu.midnightdust.lib.config.MidnightConfig;

import static coffee.waffle.qls.QuiltLoadingScreen.MODID;

public class ModMenuInit implements ModMenuApi {
  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return parent -> MidnightConfig.getScreen(parent, MODID);
  }
}
