package com.pahimar.ee3.tileentity;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.pahimar.ee3.block.ModBlocks;
import com.pahimar.ee3.emc.EmcRegistry;
import com.pahimar.ee3.lib.Sounds;
import com.pahimar.ee3.lib.Strings;

/**
 * Equivalent-Exchange-3
 * 
 * TileEnergyCondenser
 * 
 * @author pahimar & RedmenNL
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class TileEnergyCondenser extends TileEE implements IInventory {

    /** The current angle of the chest lid (between 0 and 1) */
    public float lidAngle;

    /** The angle of the chest lid last tick */
    public float prevLidAngle;

    /** The number of players currently using this chest */
    public int numUsingPlayers;

    /** Server sync counter (once per 20 ticks) */
    private int ticksSinceSync;
    
    /** Total EMC stored */
    public float storedEMC;
    
    /** Max EMC storage */
    public static final float maxEMC = 8192F;
    
    /** Update Interger */
    private int updateTime = 0;
    
    /** Should update when updateTime == 0? */
    private boolean shouldUpdate = false;
    

    public static final int INVENTORY_SIZE = (13 * 4) + 1;

    /**
     * The ItemStacks that hold the items currently being used in the Alchemical
     * Chest
     */
    private ItemStack[] inventory;

    public TileEnergyCondenser() {

        super();
        inventory = new ItemStack[INVENTORY_SIZE];
    }

    @Override
    public int getSizeInventory() {

        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex) {

        return inventory[slotIndex];
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int decrementAmount) {

        ItemStack itemStack = getStackInSlot(slotIndex);
        if (itemStack != null) {
            if (itemStack.stackSize <= decrementAmount) {
                setInventorySlotContents(slotIndex, null);
            }
            else {
                itemStack = itemStack.splitStack(decrementAmount);
                if (itemStack.stackSize == 0) {
                    setInventorySlotContents(slotIndex, null);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotIndex) {

        if (inventory[slotIndex] != null) {
            ItemStack itemStack = inventory[slotIndex];
            inventory[slotIndex] = null;
            return itemStack;
        }
        else
            return null;
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack) {

        inventory[slotIndex] = itemStack;

        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }

    @Override
    public String getInvName() {

        return this.hasCustomName() ? this.getCustomName() : Strings.CONTAINER_ENERGY_CONDENSER_NAME;
    }

    @Override
    public int getInventoryStackLimit() {

        return 64;
    }

    /**
     * Called when a client event is received with the event number and
     * argument, see World.sendClientEvent
     */
    @Override
    public boolean receiveClientEvent(int eventID, int numUsingPlayers) {

        if (eventID == 1) {
            this.numUsingPlayers = numUsingPlayers;
            return true;
        }
        else
            return super.receiveClientEvent(eventID, numUsingPlayers);
    }

    @Override
    public void openChest() {

        ++numUsingPlayers;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, ModBlocks.energyCondenser.blockID, 1, numUsingPlayers);
    }

    @Override
    public void closeChest() {

        --numUsingPlayers;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, ModBlocks.energyCondenser.blockID, 1, numUsingPlayers);
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses,
     * e.g. the mob spawner uses this to count ticks and creates a new spawn
     * inside its implementation.
     */
    @Override
    public void updateEntity() {

        super.updateEntity();

        if (++ticksSinceSync % 20 * 4 == 0) {
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, Block.enderChest.blockID, 1, numUsingPlayers);
        }

        prevLidAngle = lidAngle;
        float angleIncrement = 0.1F;
        double adjustedXCoord, adjustedZCoord;

        if (numUsingPlayers > 0 && lidAngle == 0.0F) {
            adjustedXCoord = xCoord + 0.5D;
            adjustedZCoord = zCoord + 0.5D;
            worldObj.playSoundEffect(adjustedXCoord, yCoord + 0.5D, adjustedZCoord, Sounds.CHEST_OPEN, 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (numUsingPlayers == 0 && lidAngle > 0.0F || numUsingPlayers > 0 && lidAngle < 1.0F) {
            float var8 = lidAngle;

            if (numUsingPlayers > 0) {
                lidAngle += angleIncrement;
            }
            else {
                lidAngle -= angleIncrement;
            }

            if (lidAngle > 1.0F) {
                lidAngle = 1.0F;
            }

            if (lidAngle < 0.5F && var8 >= 0.5F) {
                adjustedXCoord = xCoord + 0.5D;
                adjustedZCoord = zCoord + 0.5D;
                worldObj.playSoundEffect(adjustedXCoord, yCoord + 0.5D, adjustedZCoord, Sounds.CHEST_CLOSE, 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (lidAngle < 0.0F) {
                lidAngle = 0.0F;
            }
        }
        
        if (!this.worldObj.isRemote)
        {
            
            if (getStackInSlot(0) != null && EmcRegistry.hasEmcValue(getStackInSlot(0)) && EmcRegistry.getEmcValue(getStackInSlot(0)).getValue() != 0F && EmcRegistry.getEmcValue(getStackInSlot(0)).getValue() <= maxEMC)
            {
                if (storedEMC < EmcRegistry.getEmcValue(getStackInSlot(0)).getValue())
                {
                    for (int i = 1; i < INVENTORY_SIZE; i++)
                    {
                        if (getStackInSlot(i) != null && getStackInSlot(i).getItem() != getStackInSlot(0).getItem() )
                        {
                            ItemStack stack = getStackInSlot(i);
                            if (EmcRegistry.hasEmcValue(stack) && EmcRegistry.getEmcValue(stack).getValue() != 0F)
                            {
                                storedEMC += EmcRegistry.getEmcValue(stack).getValue();
                                decrStackSize(i, 1);
                                this.customName = Float.toString(storedEMC);
                                shouldUpdate = true;
                                break;
                            }
                        }
                    }
                }
                if (storedEMC >= EmcRegistry.getEmcValue(getStackInSlot(0)).getValue())
                {
                    shouldUpdate = addItemsFromEMC();
                }
            }
            if (updateTime > 0)
            {
                updateTime--;
            }
            if (shouldUpdate && updateTime == 0)
                updateEMC();
        }
    }
    
    private void updateEMC()
    {
        this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        updateTime = 10;
        shouldUpdate = false;
    }
    
    private boolean addItemsFromEMC()
    {
        for (int i = 1; i < INVENTORY_SIZE; i++)
        {
            ItemStack stack = getStackInSlot(i);
            if (stack != null && stack.getItem() == getStackInSlot(0).getItem() && stack.stackSize != stack.getMaxStackSize())
            {
                storedEMC -= EmcRegistry.getEmcValue(getStackInSlot(0)).getValue();
                getStackInSlot(i).stackSize++;
                this.customName = Float.toString(storedEMC);
                return true;
            }
        }
        for (int i = 1; i < INVENTORY_SIZE; i++)
        {
            ItemStack stack = getStackInSlot(i);
            if (stack == null)
            {
                storedEMC -= EmcRegistry.getEmcValue(getStackInSlot(0)).getValue();
                ItemStack newStack = getStackInSlot(0).copy();
                newStack.stackSize = 1;
                setInventorySlotContents(i, newStack);
                this.customName = Float.toString(storedEMC);
                return true;
            }
        }
        return false;
    }
    
    public float getStoredEMC()
    {
        return storedEMC;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {

        super.readFromNBT(nbtTagCompound);

        // Read in the ItemStacks in the inventory from NBT
        NBTTagList tagList = nbtTagCompound.getTagList("Items");
        inventory = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound = (NBTTagCompound) tagList.tagAt(i);
            byte slotIndex = tagCompound.getByte("Slot");
            if (slotIndex >= 0 && slotIndex < inventory.length) {
                inventory[slotIndex] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }
        storedEMC = nbtTagCompound.getFloat("EMC");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {

        super.writeToNBT(nbtTagCompound);

        // Write the ItemStacks in the inventory to NBT
        NBTTagList tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < inventory.length; ++currentIndex) {
            if (inventory[currentIndex] != null) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) currentIndex);
                inventory[currentIndex].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        nbtTagCompound.setTag("Items", tagList);
        nbtTagCompound.setFloat("EMC", storedEMC);
    }

    @Override
    public boolean isInvNameLocalized() {

        return this.hasCustomName();
    }

    @Override
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack) {

        return true;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(super.toString());

        stringBuilder.append("TileAlchemicalChest Data - ");
        for (int i = 0; i < inventory.length; i++) {
            if (i != 0) {
                stringBuilder.append(", ");
            }

            if (inventory[i] != null) {
                stringBuilder.append(String.format("inventory[%d]: %s", i, inventory[i].toString()));
            }
            else {
                stringBuilder.append(String.format("inventory[%d]: empty", i));
            }
        }
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }
}

