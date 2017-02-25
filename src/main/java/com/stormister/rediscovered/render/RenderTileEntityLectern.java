
package com.stormister.rediscovered.render;

import org.lwjgl.opengl.GL11;

import com.stormister.rediscovered.Rediscovered;
import com.stormister.rediscovered.blocks.tiles.TileEntityLectern;
import com.stormister.rediscovered.models.ModelLectern;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityLectern extends TileEntitySpecialRenderer {
	private static final ResourceLocation field_110871_a = new ResourceLocation(
			Rediscovered.modid + ":" + "textures/models/Lectern.png");

	private ModelLectern model;

	public RenderTileEntityLectern() {
		model = new ModelLectern();
	}

	public void renderAModelAt(TileEntityLectern tile, double d, double d1, double d2, float f) {

		int j = 0;
		if ((tile != null) && tile.hasWorldObj()) {
			j = tile.getBlockMetadata();
		}
		short short1 = 0;

		if (j == 2) {
			short1 = 180;
		}

		if (j == 3) {
			short1 = 0;
		}

		if (j == 4) {
			short1 = 90;
		}

		if (j == 5) {
			short1 = -90;
		}
		bindTexture(field_110871_a);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
		GL11.glScalef(1.0F, -1F, -1F);
		GL11.glRotatef(short1, 0.0F, 1.0F, 0.0F);

		model.renderAll();
		GL11.glPopMatrix();
		// end
	}

	@Override
	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8,
			int blah) {
		renderAModelAt((TileEntityLectern) par1TileEntity, par2, par4, par6, par8);
	}
}