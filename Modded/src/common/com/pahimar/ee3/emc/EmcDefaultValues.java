package com.pahimar.ee3.emc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.pahimar.ee3.item.CustomWrappedStack;
import com.pahimar.ee3.item.OreStack;
import com.pahimar.ee3.item.crafting.RecipeRegistry;

public class EmcDefaultValues {

    private static EmcDefaultValues emcDefaultValues = null;
    private Map<CustomWrappedStack, EmcValue> valueMap;

    private EmcDefaultValues() {

        valueMap = new HashMap<CustomWrappedStack, EmcValue>();
    }

    private static void lazyInit() {

        if (emcDefaultValues == null) {
            emcDefaultValues = new EmcDefaultValues();
            emcDefaultValues.init();
        }
    }

    private void init() {

        valueMap.put(new CustomWrappedStack(Block.stone), new EmcValue(1, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Block.grass), new EmcValue(1, Arrays.asList(new EmcComponent(EmcType.CORPOREAL, 9), EmcComponent.ESSENTIA_UNIT_COMPONENT)));
        valueMap.put(new CustomWrappedStack(Block.dirt), new EmcValue(1, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Block.cobblestone), new EmcValue(1, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Block.sand), new EmcValue(1, Arrays.asList(new EmcComponent(EmcType.CORPOREAL, 9), EmcComponent.AMORPHOUS_UNIT_COMPONENT)));
        valueMap.put(new CustomWrappedStack(Block.leaves), new EmcValue(1, Arrays.asList(new EmcComponent(EmcType.CORPOREAL, 9), EmcComponent.ESSENTIA_UNIT_COMPONENT)));
        valueMap.put(new CustomWrappedStack(Block.glass), new EmcValue(1, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(new ItemStack(Block.glass.blockID, 1, OreDictionary.WILDCARD_VALUE)), new EmcValue(1, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Block.tallGrass), new EmcValue(1, Arrays.asList(new EmcComponent(EmcType.CORPOREAL, 9), EmcComponent.ESSENTIA_UNIT_COMPONENT)));
        for (int meta = 0; meta < 16; meta++) {
            valueMap.put(new CustomWrappedStack(new ItemStack(Block.tallGrass.blockID, 1, meta)), new EmcValue(1, Arrays.asList(new EmcComponent(EmcType.CORPOREAL, 9), EmcComponent.ESSENTIA_UNIT_COMPONENT)));
        }
        valueMap.put(new CustomWrappedStack(Block.deadBush), new EmcValue(1, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Block.ice), new EmcValue(1, EmcComponent.CORPOREAL_UNIT_COMPONENT));

        //valueMap.put(new CustomWrappedStack(Block.wood), new EmcValue(32, Arrays.asList(new EmcComponent(EmcType.CORPOREAL, 4), EmcComponent.ESSENTIA_UNIT_COMPONENT)));
        //valueMap.put(new CustomWrappedStack(Block.oreIron), new EmcValue(256, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        //valueMap.put(new CustomWrappedStack(Block.oreGold), new EmcValue(2048, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        //valueMap.put(new CustomWrappedStack(Block.oreDiamond), new EmcValue(8192, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        //valueMap.put(new CustomWrappedStack(Block.oreCoal), new EmcValue(32, Arrays.asList(new EmcComponent(EmcType.CORPOREAL, 4), new EmcComponent(EmcType.KINETIC, 1))));
        
        /* RedmenNL*/
        valueMap.put(new CustomWrappedStack(Item.enderPearl), new EmcValue(1028, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Item.glowstone), new EmcValue(384, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Item.redstone), new EmcValue(64, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Item.blazeRod), new EmcValue(1536, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Item.ingotIron), new EmcValue(256, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Item.ingotGold), new EmcValue(2048, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Item.diamond), new EmcValue(8192, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Item.coal), new EmcValue(128, Arrays.asList(new EmcComponent(EmcType.CORPOREAL, 4), new EmcComponent(EmcType.KINETIC, 1))));
        valueMap.put(new CustomWrappedStack(new ItemStack(Item.dyePowder.itemID, 1, 4)), new EmcValue(864, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Item.gunpowder), new EmcValue(192, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Item.ghastTear), new EmcValue(4096, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Item.emerald), new EmcValue(10240, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Item.clay), new EmcValue(64, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Item.beefRaw), new EmcValue(64, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Item.chickenRaw), new EmcValue(64, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Item.fishRaw), new EmcValue(64, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Item.leather), new EmcValue(64, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Item.flint), new EmcValue(4, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        
        valueMap.put(new CustomWrappedStack(Block.glowStone), new EmcValue(1536, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        valueMap.put(new CustomWrappedStack(Block.obsidian), new EmcValue(64, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        
        for (ItemStack stack : OreDictionary.getOres("ingotCopper"))
        {
            valueMap.put(new CustomWrappedStack(stack), new EmcValue(85, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        }
        for (ItemStack stack : OreDictionary.getOres("ingotTin"))
        {
            valueMap.put(new CustomWrappedStack(stack), new EmcValue(256, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        }
        for (ItemStack stack : OreDictionary.getOres("ingotBronze"))
        {
            valueMap.put(new CustomWrappedStack(stack), new EmcValue(255, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        }
        for (ItemStack stack : OreDictionary.getOres("ingotSilver"))
        {
            valueMap.put(new CustomWrappedStack(stack), new EmcValue(512, EmcComponent.CORPOREAL_UNIT_COMPONENT));
        }
        for (int meta = 0; meta < 4; meta++)
        {
            valueMap.put(new CustomWrappedStack(new ItemStack(Block.wood.blockID, 1, meta)), new EmcValue(32, Arrays.asList(new EmcComponent(EmcType.CORPOREAL, 4), EmcComponent.ESSENTIA_UNIT_COMPONENT)));
        }
        
        // TODO Multiple passes for value computation
        for (int i = 0; i < 16; i++)
        {
            attemptValueAssignment();
        }
    }

    private List<CustomWrappedStack> attemptValueAssignment() {

        List<CustomWrappedStack> computedStacks = new ArrayList<CustomWrappedStack>();

        for (CustomWrappedStack stack : RecipeRegistry.getDiscoveredStacks()) {
            
            if (!valueMap.containsKey(stack)) {
                
                for (CustomWrappedStack recipeOutput : RecipeRegistry.getRecipeMappings().keySet()) {
                    
                    if (stack.equals(new CustomWrappedStack(recipeOutput.getWrappedStack()))) {
                        
                        for (List<CustomWrappedStack> recipeInputs : RecipeRegistry.getRecipeMappings().get(recipeOutput)) {
                                
                            EmcValue computedValue = computeEmcValueFromList(recipeOutput.getStackSize(), recipeInputs);
                            
                            if (computedValue instanceof EmcValue) {
                                valueMap.put(stack, computedValue);
                                computedStacks.add(stack);
                            }
                        }
                    }
                }
            }
        }
        
        return computedStacks;
    }

    private EmcValue computeEmcValueFromList(int recipeOutputSize, List<CustomWrappedStack> recipeInputs) {

        float[] computedSubValues = new float[EmcType.TYPES.length];

        for (CustomWrappedStack stack : recipeInputs) {

            CustomWrappedStack unitStack = new CustomWrappedStack(stack.getWrappedStack());
            EmcValue stackValue = valueMap.get(unitStack);
            
            if (stackValue == null && unitStack.getWrappedStack() instanceof ItemStack) {
                if (OreDictionary.getOreID((ItemStack) unitStack.getWrappedStack()) != -1) {
                    // TODO Detect if other stacks that are part of this OreStack have values, and nab them
                    unitStack = new CustomWrappedStack(new OreStack(OreDictionary.getOreID((ItemStack) unitStack.getWrappedStack())));
                    stackValue = valueMap.get(unitStack);
                }
            }

            if (stackValue != null) {
                for (EmcType emcType : EmcType.TYPES) {
                    computedSubValues[emcType.ordinal()] += stackValue.components[emcType.ordinal()] * stack.getStackSize() / recipeOutputSize;
                }
            }
            else {
                return null;
            }
        }

        return new EmcValue(computedSubValues);
    }

    public static Map<CustomWrappedStack, EmcValue> getDefaultValueMap() {

        lazyInit();
        return emcDefaultValues.valueMap;
    }
}
