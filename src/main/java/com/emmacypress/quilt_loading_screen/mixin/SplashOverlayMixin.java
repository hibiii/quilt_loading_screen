/*
 * Copyright (c) 2021, 2022, 2023 darkerbit
 * Copyright (c) 2021, 2022, 2023 triphora
 *
 * Quilt Loading Screen is under the MIT License. See LICENSE for details.
 */

package com.emmacypress.quilt_loading_screen.mixin;

import com.emmacypress.quilt_loading_screen.QuiltLoadingScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceReload;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(SplashOverlay.class)
public abstract class SplashOverlayMixin extends Overlay {
	@Final @Shadow private MinecraftClient client;

	private QuiltLoadingScreen quiltLoadingScreen$loadingScreen;

	@Inject(
		method = "<init>(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/resource/ResourceReload;Ljava/util/function/Consumer;Z)V",
		at = @At("TAIL")
	)
	private void constructor(MinecraftClient client, ResourceReload monitor, Consumer<Optional<Throwable>> exceptionHandler, boolean reloading, CallbackInfo ci) {
		quiltLoadingScreen$loadingScreen = new QuiltLoadingScreen(this.client);
	}

	// Render before logo
	@Inject(
		method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFunc(II)V", ordinal = 0),
		locals = LocalCapture.CAPTURE_FAILSOFT
	)
	private void renderPatches(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci,
														 int i, int j, long l, float f) {
		quiltLoadingScreen$loadingScreen.renderPatches(graphics, delta, f >= 1.0f);
	}
}
