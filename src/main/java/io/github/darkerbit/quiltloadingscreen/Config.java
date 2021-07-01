package io.github.darkerbit.quiltloadingscreen;

import eu.midnightdust.lib.config.MidnightConfig;

public class Config extends MidnightConfig {
  @Entry public static boolean prideQuiltsEnabled = false;

  public static boolean isPrideQuiltsEnabled() {
    return prideQuiltsEnabled;
  }

  public static void initConfig() {
    MidnightConfig.init(QuiltLoadingScreen.MODID, Config.class);
  }
}
