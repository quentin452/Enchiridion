package joshie.enchiridion.library;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import joshie.enchiridion.helpers.StackHelper;
import net.minecraft.item.ItemStack;

import com.google.gson.annotations.Expose;

import cpw.mods.fml.common.Loader;

public class ModBooks {
    private final ArrayList<ModBookData> books = new ArrayList<>();

    public void addBook(ModBookData book) {
        books.add(book);
    }

    public List<ModBookData> getBooks() {
        return books;
    }

    public static class ModBookData {
        private String mod;
        private String stack;
        private String type;
        public boolean free;
        public boolean onCrafted;
        @SerializedName("openGuiClass")
        public String openGuiClass;
        @SerializedName("openGuiNBT")
        public String openGuiNBT;
        public boolean pickUp;
        public String overwrite;
        public ItemStack item; //If this isn't null, then this book is actively installed


        public ModBookData() {}

        public ModBookData(String mod, String stack, String type) {
            this.mod = mod;
            this.stack = stack;
            this.type = type;
            this.free = true;
            this.onCrafted = false;
            this.openGuiClass = "";
            this.openGuiNBT = "";
            this.pickUp = false;
            this.overwrite = "";
        }

        public ModBookData(String mod, String item, int meta, String register) {
            this(mod, mod + ":" + item + " " + meta, register);
        }

        public ModBookData setOverwrites(String overwrite) {
            this.free = false;
            this.overwrite = overwrite;
            return this;
        }

        public ModBookData setOpenGuiClass(String clazz) {
            this.free = false;
            this.openGuiClass = clazz;
            return this;
        }

        public ModBookData setOpenGuiNBT(String nbt) {
            this.free = false;
            this.openGuiNBT = nbt;
            return this;
        }
    }

    /** sets up the items with their actual item **/
    public ModBooks init() {
        for (ModBookData book : books) {
            if (Loader.isModLoaded(book.mod)) {
                try {
                    book.item = StackHelper.getStackFromString(book.stack);
                    if (book.item == null || book.item.getItem() == null) {
                        book.item = null; //Make the book item null if it's item is null
                    }
                } catch (Exception e) {}
            }
        }

        return this;
    }

    /** Register books to the bookhandler registry, Called client side whenever a server
     * sends a new list of books; this is jsut, so we know how they should be handled **/
    public void registerBooks() {
        for (ModBookData book : books) {
            if (book.item == null) continue;
            BookHandlerRegistry.registerBook(book.item, book.type);
        }
    }

    /** Default books in the json file **/
    public static ModBooks getModBooks(ModBooks data) {
        data.addBook(new ModBookData("AgriCraft", "journal", 0, "default"));
        data.addBook(new ModBookData("aura", "lexicon", 0, "switch"));
        data.addBook(new ModBookData("AWWayofTime", "itemBloodMagicBook", 0, "switch"));
        data.addBook(new ModBookData("Botania", "Botania:lexicon 0", "network"));
        data.addBook(new ModBookData("Botania", "Botania:lexicon 0 {knowledge.minecraft:1b,knowledge.alfheim:1b}", "network").setOpenGuiClass("vazkii.botania.client.gui.lexicon.GuiLexiconIndex").setOpenGuiNBT("knowledge.alfheim").setOverwrites("Botania:lexicon 0"));
        //data.addBook(new ModBookData("ChromatiCraft", "chromaticraft_item_help", 0, "default")); //Disabled due to currently not fixable with current book types
        data.addBook(new ModBookData("factorization", "docbook", 0, "default"));
        data.addBook(new ModBookData("HardcoreQuesting", "quest_book", 0, "network"));
        data.addBook(new ModBookData("ImmersiveEngineering", "tool" , 3, "network"));
        data.addBook(new ModBookData("Mariculture", "guide", 0, "switch"));
        data.addBook(new ModBookData("Mariculture", "guide", 1, "switch"));
        data.addBook(new ModBookData("Mariculture", "guide", 2, "switch"));
        data.addBook(new ModBookData("Mariculture", "guide", 3, "switch"));
        data.addBook(new ModBookData("Mariculture", "guide", 4, "switch"));
        data.addBook(new ModBookData("Mariculture", "guide", 5, "switch"));
        data.addBook(new ModBookData("AWWayofTime", "Mariculture:guide 6", "switch"));
        data.addBook(new ModBookData("Botania", "Mariculture:guide 7", "switch"));
        data.addBook(new ModBookData("Mariculture", "guide", 8, "switch"));
        data.addBook(new ModBookData("OpenBlocks", "infoBook", 0, "network"));
        data.addBook(new ModBookData("OpenComputers", "item", 98, "network"));
        data.addBook(new ModBookData("Steamcraft", "book", 0, "switch"));
        data.addBook(new ModBookData("TConstruct", "manualBook", 0, "default"));
        data.addBook(new ModBookData("TConstruct", "manualBook", 1, "default"));
        data.addBook(new ModBookData("TConstruct", "manualBook", 2, "default"));
        data.addBook(new ModBookData("TConstruct", "manualBook", 3, "default"));
        data.addBook(new ModBookData("TConstruct", "manualBook", 4, "default"));
        data.addBook(new ModBookData("Thaumcraft", "ItemThaumonomicon", 0, "network"));
        data.addBook(new ModBookData("witchery", "bookbiomes2", 0, "switch"));
        data.addBook(new ModBookData("witchery", "cauldronbook", 0, "switch"));
        data.addBook(new ModBookData("witchery", "ingredient", 46, "switch"));
        data.addBook(new ModBookData("witchery", "ingredient", 47, "switch"));
        data.addBook(new ModBookData("witchery", "ingredient", 48, "switch"));
        data.addBook(new ModBookData("witchery", "ingredient", 49, "switch"));
        data.addBook(new ModBookData("witchery", "ingredient", 81, "switch"));
        data.addBook(new ModBookData("witchery", "ingredient", 106, "switch"));
        data.addBook(new ModBookData("witchery", "ingredient", 107, "switch"));
        data.addBook(new ModBookData("witchery", "ingredient", 127, "switch"));
        return data;
    }
}
