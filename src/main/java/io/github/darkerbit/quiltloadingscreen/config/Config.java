package io.github.darkerbit.quiltloadingscreen.config;

import io.github.darkerbit.quiltloadingscreen.QuiltLoadingScreen;

public class Config extends MidnightConfig {
  @Entry public static boolean prideQuiltsEnabled = false;

  public static boolean isPrideQuiltsEnabled() {
    return prideQuiltsEnabled;
  }

  public static void initConfig() {
    MidnightConfig.init(QuiltLoadingScreen.MODID, Config.class);
  }
}
