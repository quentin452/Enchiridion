package joshie.enchiridion.handlers;

import joshie.enchiridion.ECommonProxy;
import joshie.enchiridion.books.Book;
import joshie.enchiridion.books.BookRegistry;
import joshie.enchiridion.books.gui.GuiBook;
import joshie.enchiridion.books.gui.GuiBookCreate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        ItemStack held = player.getCurrentEquippedItem();
        if (held.getItem() == ECommonProxy.book) {
            Book book = BookRegistry.INSTANCE.getBook(held);
            if (book != null) {
                return GuiBook.INSTANCE.setBook(book, player.isSneaking());
            } else return GuiBookCreate.INSTANCE.setStack(player.getCurrentEquippedItem());
        }
        
        return null;
    }
}