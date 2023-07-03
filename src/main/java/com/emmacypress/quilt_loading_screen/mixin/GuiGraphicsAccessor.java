package com.emmacypress.quilt_loading_screen.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiGraphics.class)
public interface GuiGraphicsAccessor {
	@Invoker("drawTexturedQuad")
	void quiltLoadingScreen$drawTexturedQuad(Identifier texture, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2);
}
