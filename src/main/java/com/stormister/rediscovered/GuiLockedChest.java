package com.stormister.rediscovered;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiLockedChest extends GuiContainer implements GuiYesNoCallback {
	public final ResourceLocation texture = new ResourceLocation(mod_Rediscovered.modid.toLowerCase(),
			"textures/gui/LockedChest.png");
	private final int PROMPT_REPLY_ACTION = 0;
	private URI displayedURI = null;

	public GuiLockedChest(EntityPlayer player) {
		super(new ContainerLockedChest(player));
		xSize = 248;
		ySize = 166;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		switch (p_146284_1_.id) {
		case 0:
			mc.displayGuiScreen((GuiScreen) null);
			mc.setIngameFocus();
			break;
		case 1:
			URI uri = URI.create("http://artur1998g.ru/store/loot.html");
			if (uri != null) {
				// Rude not to ask
				if (Minecraft.getMinecraft().gameSettings.chatLinksPrompt) {
					displayedURI = uri;
					mc.displayGuiScreen(
							new GuiConfirmOpenLink(this, displayedURI.toString(), PROMPT_REPLY_ACTION, false));
				} else {
					openURI(uri);
				}
			}
			break;
		}
	}

	@Override
	public void confirmClicked(boolean result, int action) {
		if ((action == PROMPT_REPLY_ACTION) && result) {
			openURI(displayedURI);
			displayedURI = null;
		}
		mc.displayGuiScreen(this);
	}

	// @Override
	// public void componentMouseDown(BaseComponent component, int offsetX, int
	// offsetY, int button) {
	// if (component.getName().equals("btnDonate")) {
	// if (((GuiComponentTextButton)component).isButtonEnabled()) {
	// URI uri = URI.create(getContainer().getOwner().getDonateUrl());
	// if (uri != null) {
	// // Rude not to ask
	// if (Minecraft.getMinecraft().gameSettings.chatLinksPrompt) {
	// this.displayedURI = uri;
	// this.mc.displayGuiScreen(new GuiConfirmOpenLink(this,
	// this.displayedURI.toString(), PROMPT_REPLY_ACTION, false));
	// } else {
	// openURI(uri);
	// }
	// }
	// }
	// }
	// }

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect((width / 2) - (xSize / 2), height / 6, 0, 0, xSize, ySize);
	}

	@Override
	public void initGui() {
		buttonList.clear();
		buttonList.add(
				new GuiButton(0, (width / 2) - 107, (height / 6) + 130, 98, 20, I18n.format("Not Now", new Object[0])));
		buttonList.add(new GuiButton(1, (width / 2) + 10, (height / 6) + 130, 98, 20,
				I18n.format("Go to store", new Object[0])));
	}

	private void openURI(URI uri) {
		try {
			Desktop.getDesktop().browse(uri);
		} catch (IOException e) {
		}
	}
}
