package com.pahimar.ee3.client.renderer.item;

import net.minecraft.client.model.ModelChest;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.pahimar.ee3.lib.Textures;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Equivalent-Exchange-3
 * 
 * ItemEnergyCondenserRenderer
 * 
 * @author pahimar & RedmenNL
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class ItemEnergyCondenserRenderer implements IItemRenderer {

    private ModelChest modelChest;

    public ItemEnergyCondenserRenderer() {

        modelChest = new ModelChest();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {

        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {

        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        switch (type) {
            case ENTITY: {
                renderEnergyCondenser(0.5F, 0.5F, 0.5F);
                break;
            }
            case EQUIPPED: {
                renderEnergyCondenser(1.0F, 1.0F, 1.0F);
                break;
            }
            case EQUIPPED_FIRST_PERSON: {
                renderEnergyCondenser(1.0F, 1.0F, 1.0F);
                break;
            }
            case INVENTORY: {
                renderEnergyCondenser(0.0F, 0.075F, 0.0F);
                break;
            }
            default:
                break;
        }
    }

    private void renderEnergyCondenser(float x, float y, float z) {

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(Textures.MODEL_ENERGY_CONDENSER);
        GL11.glPushMatrix(); //start
        GL11.glTranslatef(x, y, z); //size
        GL11.glRotatef(180, 1, 0, 0);
        GL11.glRotatef(-90, 0, 1, 0);
        modelChest.renderAll();
        GL11.glPopMatrix(); //end
    }
}

