package physica.nuclear.client.render.item;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import physica.library.client.render.ItemRenderObjModel;

@SideOnly(Side.CLIENT)
public class ItemRenderTurbine extends ItemRenderObjModel {

	public ItemRenderTurbine(String objFile, String textureFile, String domain, String modelDirectory, String modelTextureDirectory) {
		super(objFile, textureFile, domain, modelDirectory, modelTextureDirectory);
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glScaled(0.0925, 0.0525, 0.0925);
		if (ItemRenderType.INVENTORY.equals(type)) {
			GL11.glTranslatef(-0.5f, -0.8f, -0.5f);
			GL11.glRotatef(180f, 0, 1, 0);
		} else if (ItemRenderType.EQUIPPED_FIRST_PERSON.equals(type)) {
			GL11.glRotatef(45f, 0, 1, 0);
			GL11.glTranslatef(1f, 12f, 9f);
		} else if (ItemRenderType.EQUIPPED.equals(type)) {
			GL11.glTranslatef(8f, 10f, 8f);
		}
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(model_texture);
		model_base.render();
		GL11.glPopMatrix();
	}
}
