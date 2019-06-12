package joshie.enchiridion.library;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.data.library.ModdedBooks;
import joshie.enchiridion.data.library.ModdedBooks.ModdedBook;
import joshie.enchiridion.helpers.FileHelper;
import joshie.enchiridion.helpers.GsonHelper;
import joshie.enchiridion.helpers.StackHelper;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class ModSupport {
    private static ModdedBooks books;

    public static void loadDataFromJson(String serverName, String json) {
        books = getDefaults();
        if (json != null) {
            books.mergeIn(GsonHelper.getModifiedGson().fromJson(json, ModdedBooks.class));
        } else {
            try {
                //Write the json
                String defaultJson = GsonHelper.getModifiedGson().toJson(new ModdedBooks()); //Add a blank default
                File toSave = FileHelper.getLibraryFile(serverName);
                Writer writer = new OutputStreamWriter(new FileOutputStream(toSave), StandardCharsets.UTF_8);
                writer.write(defaultJson);
                writer.close();
            } catch (Exception ignored) {
            }
        }

        //Now that we have loaded in the data we should convert it
        EnchiridionAPI.library.resetStacksAllowedInLibrary();
        for (ModdedBook book : books.getList()) {
            try {
                ItemStack stack = StackHelper.getStackFromString(book.getItem());
                if (!stack.isEmpty()) {
                    switch (book.getHandler()) {
                        case "blacklist":
                            LibraryRegistry.INSTANCE.unregisterBookHandlerForStackFromJSON(stack, book.shouldMatchNBT());
                            break;
                        case "customwood":
                            EnchiridionAPI.library.registerWood(stack, book.shouldMatchNBT());
                            break;
                        default:
                            LibraryRegistry.INSTANCE.registerBookHandlerForStackFromJSON(book.getHandler(), stack, book.shouldMatchNBT());
                            break;
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    private static ModdedBooks getDefaults() { //TODO Test books
        books = new ModdedBooks(); //Create the books
        books.add("enchiridion", "enchiridion:book", false);
        books.add("writeable", "minecraft:writable_book", false);
        books.add("written", "minecraft:written_book", false);
        books.add("switchclick", "actuallyadditions:item_booklet", false);
        books.add("switchclick", "astralsorcery:ItemJournal", false);
        books.add("switchclick", "bibliocraft:BiblioRedBook", false);
        books.add("switchclick", "bibliocraft:BigBook", false);
        books.add("switchclick", "bibliocraft:RecipeBook", false);
        books.add("switchclick", "bibliocraft:StockroomCatalog", false);
        books.add("switchclick", "pokecube:pokedex", false);
        books.add("switchclick", "rftools:rftools_manual", false);
        books.add("switchclick", "rftoolscontrol:rftoolscontrol_manual", false);
        books.add("switchclick", "theoneprobe:probenote", false);
        books.add("switchclick", "totemic:totempedia", false);

        /*if (EInfo.IS_GUIDEAPI_LOADED) {
            for (Book book : GuideAPI.getBooks().values()) {
                books.add("copynbt", "guideapi:" + book.getRegistryName().toString().replace(":", "-"), false, false);
            }
        }*/

        books.add("customwood", "minecraft:planks 1", false);
        books.add("customwood", "minecraft:planks 5", false);
        books.add("customwood", "biomesoplenty:planks_0 14", false);

        //Not updated to 1.12 yet. Support not guaranteed.
        books.add("customwood", "botania:livingwood", false);
        books.add("customwood", "chisel:livingwood-planks", false);
        books.add("customwood", "chisel:livingwood-raw", false);
        books.add("customwood", "chisel:planks-dark-oak", false);
        books.add("customwood", "chisel:planks-spruce", false);
        books.add("customwood", "chisel:thinWood-dark", false);
        books.add("customwood", "chisel:thinWood-spruce", false);
        books.add("customwood", "thaumcraft:plank 0", false);
        books.add("switchclick", "aura:lexicon", false);
        books.add("switchclick", "botania:lexicon", false);
        books.add("switchclick", "deepresonance:dr_manual", false);
        books.add("switchclick", "environmentaltech:digital_guide", false);
        books.add("switchclick", "extrautils2:book", false);
        books.add("switchclick", "guidebook:guideBook", false);
        books.add("switchclick", "harvestfestival:book", false);
        books.add("switchclick", "harvestfestival:cookbook", false);
        books.add("switchclick", "immersiveengineering:tool 3", false);
        books.add("switchclick", "openblocks:infoBook", false);
        books.add("switchclick", "opencomputers:tool 4", false);
        books.add("switchclick", "railcraft:routing_table", false);
        books.add("switchclick", "refraction:book", false);
        books.add("switchclick", "rftoolsdim:rftoolsdim_manual", false);
        books.add("switchclick", "simpleachievements:achievement_book", false);
        books.add("switchclick", "tconstruct:book", false);
        books.add("switchclick", "thaumcraft:thaumonomicon", false);
        books.add("switchclick", "villagebox:village_book", false);
        books.add("warpbook", "warpbook:warpbook", false);
        return books;
    }

    public static ItemStack[] getFreeBooks() {
        return books.getFreeBooks();
    }

    private static final HashMap<String, ModdedBooks> CACHE = new HashMap<>();

    public static void reset() {
        CACHE.clear();
    }

    public static int getHashcode(String serverName) {
        if (CACHE.containsKey(serverName)) {
            return CACHE.get(serverName).hashCode();
        }

        loadDataFromJson(serverName, FileHelper.getLibraryJson(serverName)); //Load in any existing json files
        CACHE.put(serverName, books);
        return books.hashCode();
    }
}