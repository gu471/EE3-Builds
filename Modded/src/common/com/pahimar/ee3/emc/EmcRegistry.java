package com.pahimar.ee3.emc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.ImmutableSortedMap;
import com.pahimar.ee3.core.helper.LogHelper;
import com.pahimar.ee3.core.helper.RecipeHelper;
import com.pahimar.ee3.item.CustomWrappedStack;
import com.pahimar.ee3.item.OreStack;
import com.pahimar.ee3.item.crafting.RecipeRegistry;

public class EmcRegistry {

    private static EmcRegistry emcRegistry = null;

    private ImmutableSortedMap<CustomWrappedStack, EmcValue> stackMappings;
    private ImmutableSortedMap<EmcValue, List<CustomWrappedStack>> valueMappings;

    private static void lazyInit() {

        if (emcRegistry == null) {
            emcRegistry = new EmcRegistry();
            emcRegistry.init();
        }
    }

    private void init() {

        ImmutableSortedMap.Builder<CustomWrappedStack, EmcValue> stackMappingsBuilder = ImmutableSortedMap.naturalOrder();
        ImmutableSortedMap.Builder<EmcValue, List<CustomWrappedStack>> valueMappingsBuilder = ImmutableSortedMap.naturalOrder();

        Map<CustomWrappedStack, EmcValue> defaultValues = EmcDefaultValues.getDefaultValueMap();

        // Handle the stack mappings
        stackMappingsBuilder.putAll(defaultValues);
        stackMappings = stackMappingsBuilder.build();
        
        // Handle the value mappings
        SortedMap<EmcValue, ArrayList<CustomWrappedStack>> tempValueMappings = new TreeMap<EmcValue, ArrayList<CustomWrappedStack>>();

        for (CustomWrappedStack stack : defaultValues.keySet()) {
            EmcValue value = defaultValues.get(stack);

            if (tempValueMappings.containsKey(value)) {
                if (!(tempValueMappings.get(value).contains(stack))) {
                    tempValueMappings.get(value).add(stack);
                }
            }
            else {
                tempValueMappings.put(value, new ArrayList<CustomWrappedStack>(Arrays.asList(stack)));
            }
        }
        
        valueMappingsBuilder.putAll(tempValueMappings);
        valueMappings = valueMappingsBuilder.build();
    }

    public static boolean hasEmcValue(Object object) {

        lazyInit();

        if (CustomWrappedStack.canBeWrapped(object)) {
            
            CustomWrappedStack stack = new CustomWrappedStack(object);
            
            if (emcRegistry.stackMappings.containsKey(new CustomWrappedStack(stack.getWrappedStack()))) {
                return emcRegistry.stackMappings.containsKey(new CustomWrappedStack(stack.getWrappedStack()));
            }
            else {
                
                /*
                 * If the wrapped stack is an ItemStack, check to see if it has an entry in the OreDictionary.
                 * If it does, check every ItemStack that shares the entry in the OreDictionary with the wrapped
                 * stack
                 */
                if (stack.getWrappedStack() instanceof ItemStack) {
                    
                    ItemStack wrappedItemStack = (ItemStack) stack.getWrappedStack();
                    OreStack oreStack = new OreStack(wrappedItemStack);
                    boolean hasValue = false;
                    
                    if (oreStack.oreId != -1) {
                        List<ItemStack> oreItemStacks = OreDictionary.getOres(oreStack.oreId);
                        
                        // Scan all ItemStacks in the OreDictionary entry for equality
                        for (ItemStack oreItemStack : oreItemStacks) {
                            if (emcRegistry.stackMappings.containsKey(new CustomWrappedStack(oreItemStack)) && !hasValue) {
                                hasValue = true;
                            }
                        }
                        
                        // Lastly, scan all ItemStacks in the OreDictionary entry for wildcard equality
                        if (!hasValue) {
                            for (ItemStack oreItemStack : oreItemStacks) {
                                if ((oreItemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) && (wrappedItemStack.itemID == oreItemStack.itemID) && (!hasValue)) {
                                    hasValue = true;
                                }
                            }
                        }
                    }
                    
                    return hasValue;
                }
            }
        }

        return false;
    }

    public static EmcValue getEmcValue(Object object) {

        lazyInit();

        if (hasEmcValue(object)) {
            
            CustomWrappedStack stack = new CustomWrappedStack(object);
            
            if (emcRegistry.stackMappings.containsKey(new CustomWrappedStack(stack.getWrappedStack()))) {
                return emcRegistry.stackMappings.get(new CustomWrappedStack(stack.getWrappedStack()));
            }
            else {
                
                /*
                 * If the wrapped stack is an ItemStack, check to see if it has an entry in the OreDictionary.
                 * If it does, check every ItemStack that shares the entry in the OreDictionary with the wrapped
                 * stack
                 */
                if (stack.getWrappedStack() instanceof ItemStack) {
                    OreStack oreStack = new OreStack((ItemStack) stack.getWrappedStack());
                    
                    if (oreStack.oreId != -1) {
                        List<ItemStack> oreItemStacks = OreDictionary.getOres(oreStack.oreId);
                        EmcValue lowestValue = null;
                        
                        for (ItemStack oreItemStack : oreItemStacks) {
                            
                            if (emcRegistry.stackMappings.containsKey(new CustomWrappedStack(oreItemStack))) {
                                EmcValue currentValue = emcRegistry.stackMappings.get(new CustomWrappedStack(oreItemStack));
                                
                                if (lowestValue == null) {
                                    lowestValue = currentValue;
                                }
                                else {
                                    if (currentValue.compareTo(lowestValue) < 0) {
                                        lowestValue = currentValue;
                                    }
                                }
                            }
                        }
                        
                        // TODO Handle the OreDictionary wildcard meta case
                        
                        return lowestValue;
                    }
                }
            }
        }

        return null;
    }
    
    public static void attemptValueAssignment() {
        
        lazyInit();

        List<CustomWrappedStack> uncomputedStacks = new ArrayList<CustomWrappedStack>();
        boolean foundNew = true;
        
        //Get every CustomWrappedStack with a recipe that doesn't have a Emc value yet
        for (CustomWrappedStack stack : RecipeRegistry.getDiscoveredStacks()) {
            
            if (!hasEmcValue(stack)) {
                uncomputedStacks.add(stack);
            }
        }
        
        //If we found a new Emc value the previous time, try again, this CustomWrappedStack might unlock the Emc value for another CustomWrappedStack
        while (foundNew && !uncomputedStacks.isEmpty())
        {
            foundNew = false;
            Map<CustomWrappedStack, EmcValue> valueMap = new HashMap<CustomWrappedStack, EmcValue>();
            
            Iterator<CustomWrappedStack> i = uncomputedStacks.iterator();
            while (i.hasNext()) {
                
                CustomWrappedStack stack = i.next();
                for (List<CustomWrappedStack> recipeInputs : RecipeRegistry.getRecipeMappings().get(stack)) {
                    
                    EmcValue stackValue = computeEmcValueFromList(stack.getStackSize(), recipeInputs);
                    if (stackValue != null) {
                        
                        valueMap.put(stack, stackValue);
                        
                        //Remove the CustomWrappedStack from the uncomputedStacks list, we found it, so we don't need to find it again!
                        i.remove();
                        foundNew = true;
                        break;
                    }
                }
            }
            emcRegistry.addToMap(valueMap);
        }
    }
    
        private static EmcValue computeEmcValueFromList(int recipeOutputSize, List<CustomWrappedStack> recipeInputs) {
        
                float[] computedSubValues = new float[EmcType.TYPES.length];
        
                for (CustomWrappedStack stack : recipeInputs) {
        
                    CustomWrappedStack unitStack = new CustomWrappedStack(stack.getWrappedStack());
                    EmcValue stackValue = getEmcValue(unitStack);
        
                    if (stackValue != null) {
                        for (EmcType emcType : EmcType.TYPES) {
                            computedSubValues[emcType.ordinal()] += stackValue.components[emcType.ordinal()] * stack.getStackSize() / recipeOutputSize;
                        }
                    }
                    else {
                        return null;
                    }
                }
                EmcValue stackValue = new EmcValue(computedSubValues);
                if (stackValue.getValue() != 0F) {
                    return new EmcValue(computedSubValues);
                }
                else {
                    return null;
                }
             }
    
    private void addToMap(Map<CustomWrappedStack, EmcValue> map) {

        ImmutableSortedMap.Builder<CustomWrappedStack, EmcValue> stackMappingsBuilder = ImmutableSortedMap.naturalOrder();
        ImmutableSortedMap.Builder<EmcValue, List<CustomWrappedStack>> valueMappingsBuilder = ImmutableSortedMap.naturalOrder();

        Map<CustomWrappedStack, EmcValue> defaultValues = EmcDefaultValues.getDefaultValueMap();

        // Handle the stack mappings
        stackMappingsBuilder.putAll(stackMappings);
        stackMappingsBuilder.putAll(map);
        stackMappings = stackMappingsBuilder.build();
        
        // Handle the value mappings
        SortedMap<EmcValue, ArrayList<CustomWrappedStack>> tempValueMappings = new TreeMap<EmcValue, ArrayList<CustomWrappedStack>>();

        for (CustomWrappedStack stack : defaultValues.keySet()) {
            EmcValue value = defaultValues.get(stack);

            if (tempValueMappings.containsKey(value)) {
                if (!(tempValueMappings.get(value).contains(stack))) {
                    tempValueMappings.get(value).add(stack);
                }
            }
            else {
                tempValueMappings.put(value, new ArrayList<CustomWrappedStack>(Arrays.asList(stack)));
            }
        }
        
        valueMappingsBuilder.putAll(tempValueMappings);
        valueMappings = valueMappingsBuilder.build();
    }

    public static List<CustomWrappedStack> getStacksInRange(int start, int finish) {

        return getStacksInRange(new EmcValue(start), new EmcValue(finish));
    }

    public static List<CustomWrappedStack> getStacksInRange(float start, float finish) {

        return getStacksInRange(new EmcValue(start), new EmcValue(finish));
    }

    public static List<CustomWrappedStack> getStacksInRange(EmcValue start, EmcValue finish) {

        lazyInit();

        List<CustomWrappedStack> stacksInRange = new ArrayList<CustomWrappedStack>();

        SortedMap<EmcValue, List<CustomWrappedStack>> tailMap = emcRegistry.valueMappings.tailMap(start);
        SortedMap<EmcValue, List<CustomWrappedStack>> headMap = emcRegistry.valueMappings.headMap(finish);

        SortedMap<EmcValue, List<CustomWrappedStack>> smallerMap;
        SortedMap<EmcValue, List<CustomWrappedStack>> biggerMap;

        if (!tailMap.isEmpty() && !headMap.isEmpty()) {

            if (tailMap.size() <= headMap.size()) {
                smallerMap = tailMap;
                biggerMap = headMap;
            }
            else {
                smallerMap = headMap;
                biggerMap = tailMap;
            }

            for (EmcValue value : smallerMap.keySet()) {
                if (biggerMap.containsKey(value)) {
                    stacksInRange.addAll(emcRegistry.valueMappings.get(value));
                }
            }
        }

        return stacksInRange;
    }

    public static void printStackValueMappings() {

        lazyInit();
        
        for (CustomWrappedStack stack : emcRegistry.stackMappings.keySet()) {
            LogHelper.debug("Stack: " + stack + ", Value: " + emcRegistry.stackMappings.get(stack));
        }
    }
    
    public static void printUnmappedStacks() {
        
        lazyInit();
        List<CustomWrappedStack> discoveredStacks = new ArrayList<CustomWrappedStack>(RecipeRegistry.getDiscoveredStacks());
        Collections.sort(discoveredStacks);
        for (CustomWrappedStack stack : discoveredStacks) {
            if (!hasEmcValue(stack)) {
                if (RecipeRegistry.getRecipeMappings().get(stack).size() > 0) {
                    for (List<CustomWrappedStack> recipeInputs : RecipeRegistry.getRecipeMappings().get(stack)) {
                        LogHelper.debug(stack + ": " + RecipeHelper.collateInputStacks(recipeInputs));
                    }
                }
                else {
                    LogHelper.debug(stack);
                }
            }
        }
    }
    
}
