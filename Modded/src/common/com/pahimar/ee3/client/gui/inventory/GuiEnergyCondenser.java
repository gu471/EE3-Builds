package com.pahimar.ee3.client.gui.inventory;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.pahimar.ee3.inventory.ContainerEnergyCondenser;
import com.pahimar.ee3.lib.Strings;
import com.pahimar.ee3.lib.Textures;
import com.pahimar.ee3.tileentity.TileEnergyCondenser;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Equivalent-Exchange-3
 * 
 * GuiEnergyCondenser
 * 
 * @author pahimar & RedmenNL
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GuiEnergyCondenser extends GuiContainer {

    private TileEnergyCondenser tileEnergyCondenser;

    public GuiEnergyCondenser(InventoryPlayer inventoryPlayer, TileEnergyCondenser energyCondenser) {

        super(new ContainerEnergyCondenser(inventoryPlayer, energyCondenser));
        tileEnergyCondenser = energyCondenser;
        xSize = 248;
        ySize = 195;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {

        //fontRenderer.drawString(tileEnergyCondenser.isInvNameLocalized() ? tileEnergyCondenser.getInvName() : StatCollector.translateToLocal(tileEnergyCondenser.getInvName()), xSize/2, 10, 4210752);
        fontRenderer.drawString(StatCollector.translateToLocal(Strings.CONTAINER_INVENTORY), 44, ySize - 96 + 2, 4210752);
        fontRenderer.drawString("0", 30, 11, 4210752);
        fontRenderer.drawString(Float.toString(TileEnergyCondenser.maxEMC), 168, 11, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float opacity, int x, int y) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(Textures.GUI_ENERGY_CONDENSER);

        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        this.drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);
        this.drawTexturedModalRect(xStart + 40, yStart + 5, 0, 195, (int)(Float.parseFloat(tileEnergyCondenser.getCustomName())/TileEnergyCondenser.maxEMC*123), 18);
        this.drawTexturedModalRect(xStart + 40, yStart + 5, 0, 213, 123, 20);
    }
}
