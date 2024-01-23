package enchiridion.api;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

public class DisplayRegistry {
	/** Caches **/
	private static final HashMap<String, IIcon> fluidsCache = new HashMap<>();
	private static final HashMap<String, ItemStack> itemCache = new HashMap<>();
	private static final HashMap<String, Integer[]> metaCache = new HashMap<>();
    private static final HashSet<String> oreDicCache = new HashSet<>();

	static {
		registerMetaCycling(Item.getItemFromBlock(Blocks.wool), "wool", new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 });
	}

	// Registers Ore Dictionary names to cycle, by default no ore dictionary names will cycle
	// Mariculture adds everything to the cycling list
    public static void registerOreDictionaryCycling(String name) {
        oreDicCache.add(name);
    }

	// Register items with their meta values to cycle through on the use of a key
	public static void registerMetaCycling(Item item, String key, Integer[] metas) {
		metaCache.put(key, metas);
		registerShorthand(key, new ItemStack(item));
	}

	//Register an item shorthand key for calling throughout
	public static void registerShorthand(String key, ItemStack stack) {
		if(!itemCache.containsKey(key)) itemCache.put(key, stack);
	}

    public static ItemStack getIcon(String str) {
        if (str.isEmpty()) return null;
        else {
            List<ItemStack> ores = OreDictionary.getOres(str);
            if (!ores.isEmpty()) itemCache.put(str, ores.get(0));
            else {
                ItemStack stack = StackHelper.getStackFromString(str);
                itemCache.put(str, stack);
            }
        }
        return itemCache.get(str);
    }

	public static IIcon getFluidIcon(String str) {
		if (fluidsCache.containsKey(str))
			return fluidsCache.get(str);
		else {
			Fluid fluid = FluidRegistry.getFluid(str);
			if(fluid == null) return null;
			fluidsCache.put(str, fluid.getIcon());
			return FluidRegistry.getFluid(str).getIcon();
		}
	}


    public static void updateIcons() {
        for (String str : oreDicCache) {
            List<ItemStack> ores = OreDictionary.getOres(str);
            if (!ores.isEmpty()) {
                itemCache.put(str, ores.get(GuideHandler.rand.nextInt(ores.size())));
            }
        }

        for (Entry<String, Integer[]> meta : metaCache.entrySet()) {
            ItemStack stack = itemCache.get(meta.getKey());
            if (stack != null && meta.getValue().length > 0) {
                stack.setItemDamage(meta.getValue()[GuideHandler.rand.nextInt(meta.getValue().length)]);
                itemCache.put(meta.getKey(), stack);
            }
        }
    }
}
