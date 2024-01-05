package joshie.enchiridion.library;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.List;

import joshie.enchiridion.helpers.StackHelper;
import joshie.enchiridion.library.ModBooks.ModBookData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class LibraryStorage {
    private ArrayList<ItemStack> books;

    public LibraryStorage() {
        books = new ArrayList<>();
    }

    //Dummy creation for the client
    public LibraryStorage(ModBooks modBooks) {
        updateStoredBooks(modBooks);
    }

    public LibraryStorage(List<ItemStack> books) {
        this.books = (ArrayList<ItemStack>) books;
    }

    /** Returns this list of books in this storage **/
    public List<ItemStack> getBooks() {
        return books;
    }

    /** Runs through the list of books, comparing it against the books list
     * adding and removing where appropriate */
    public LibraryStorage updateStoredBooks(ModBooks modBooks) {
        //If books aren't created it
        if (books == null) {
            books = new ArrayList<>();
        }

        //Loop through the ModBookData
        for (ModBookData book : modBooks.getBooks()) {
            if (book.item == null) continue; //Skip over this book if it is null
            if (book.free) { //If the book should be obtained for free, loop through the list
                boolean hasBook = false;
                for (ItemStack stack : books) { //Let's check the list of books
                    if (stack.isItemEqual(book.item)) {
                        hasBook = true;
                        break;
                    }
                }

                //If we don't have the book already and it is supposed to be free
                //Then we should add it to the list of books
                if (!hasBook) {
                    books.add(book.item);
                }
            }
        }

        //Now we loop through the stacks to see if the book should be removed
        //If it is no longer allowed in the list
        Iterator<ItemStack> it = books.iterator();
        while (it.hasNext()) {
            ItemStack stack = it.next();
            boolean isAllowed = false;
            for (ModBookData book : modBooks.getBooks()) {
                if (book.item == null) continue;
                if (book.item.isItemEqual(stack)) {
                    isAllowed = true;
                    break;
                }
            }

            //If this stack isn't allowed in the library, mark it for removal
            if (!isAllowed) {
                it.remove();
            }
        }

        return this;
    }

    /** Adds a book **/
    public void add(ItemStack stack) {
        books.add(stack);
    }

    /** Overwrites one book with another **/
    public void overwrite(ItemStack stack, ItemStack overwrites) {
        int index = 0;
        for (index = 0; index < books.size(); index++) {
            ItemStack book = books.get(index);
            if (book.isItemEqual(overwrites)) {
                break;
            }
        }

        if (index < books.size() && index >= 0) {
            books.set(index, stack);
        } else { //If we were unable to find the book to overwrite, add it?
            add(stack); //Add the book, if overwriting was not found???
        }
    }

    /** Called to read the data stored on these books from nbt **/
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList stacks = nbt.getTagList("BooksList", 10);
        for (int i = 0; i < stacks.tagCount(); i++) {
            NBTTagCompound tag = stacks.getCompoundTagAt(i);
            ItemStack stack = StackHelper.getItemStackFromNBT(tag);
            books.add(stack);
        }
    }

    /** Write the stored books to nbt **/
    public void writeToNBT(NBTTagCompound nbt) {
        //Saving the books
        NBTTagList stacks = new NBTTagList();
        for (ItemStack stack : books) {
            stacks.appendTag(StackHelper.writeItemStackToNBT(new NBTTagCompound(), stack));
        }

        nbt.setTag("BooksList", stacks);
    }
}
