package joshie.enchiridion;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.library.LibraryRegistry;
import joshie.enchiridion.library.handlers.*;

public class ECommonHandler {

    public static void init() {
        EnchiridionAPI.instance = new EAPIHandler();
        EnchiridionAPI.library = LibraryRegistry.INSTANCE;
        EnchiridionAPI.library.registerBookHandler(new EnchiridionBookHandler()); //Enchiridion
        EnchiridionAPI.library.registerBookHandler(new WritableBookHandler()); //Writeable Books
        EnchiridionAPI.library.registerBookHandler(new WrittenBookHandler()); //Written Books
        EnchiridionAPI.library.registerBookHandler(new TemporarySwitchHandler()); //Default Handler
        EnchiridionAPI.library.registerBookHandler(new RightClickHandler()); //Kept for backwards compatibility
        EnchiridionAPI.library.registerBookHandler(new CopyNBTHandler()); //Copy NBT Handler
    }
}