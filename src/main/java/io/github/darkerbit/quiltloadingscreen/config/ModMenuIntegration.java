package io.github.darkerbit.quiltloadingscreen.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.darkerbit.quiltloadingscreen.QuiltLoadingScreen;

public class ModMenuIntegration implements ModMenuApi {
  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return parent -> MidnightConfig.getScreen(parent, QuiltLoadingScreen.MODID);
  }
}
