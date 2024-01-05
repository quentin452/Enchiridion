package joshie.enchiridion.api;

import net.minecraft.item.ItemStack;

public interface IEnchiridionAPI {
    /** Registering an itemstack here, will cause them to open the bookdata
     *  with the identifier passed in.
     * @param stack
     * @param identifier */
    void registerBookData(ItemStack stack, String identifier);

    /** Registering your mod, will have the mod search your assets folder
     *  for a books folder with json. You need to do this if you wish your book data
     *  to ever get registered.
     *
     *  @param mod  This should either be just your modid i.e. "Mariculture" , IF your assetspath is the same
     *              ^ the assets path will get converted to all lower case, so don't worry about capitalisation for the above
     *              or in the format "modid:assetspath". for example, Enchiridion would be.
     *
     *              Enchiridion2:enchiridion
     *
     *              With the first half being the mod id, and the second half being
     *              what your assets folder is called, where you store the book json.*/
    void registerModBooks(String mod);

    /** Register a method for handling the opening of books **/
    void registerBookHandler(IBookHandler handler);

    /** Register a recipe handler **/
    void registerRecipeHandler(IRecipeHandler handler);
}
