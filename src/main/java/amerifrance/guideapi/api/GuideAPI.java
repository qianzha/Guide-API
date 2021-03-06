package amerifrance.guideapi.api;

import amerifrance.guideapi.api.impl.Book;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuideAPI {

    /**
     * The new Book registry. Handles world persistence to avoid de-sync issues.
     *
     * Register a book with {@link net.minecraftforge.fml.common.registry.GameRegistry#register(IForgeRegistryEntry)}
     */
    public static final IForgeRegistry<Book> BOOKS = PersistentRegistryManager.createRegistry(
            new ResourceLocation("guideapi", "books"),
            Book.class,
            new ResourceLocation("guideapi", "invalid_book"),
            0,
            1024,
            false,
            null,
            null,
            null
    );
    private static final List<ITypeReader> TYPE_READERS = new ArrayList<ITypeReader>();
    /**
     * The item corresponding to the Guide-API books. Access it after the Pre-Init event.
     */
    public static Item guideBook;

    /**
     * Adds a new {@link ITypeReader} to be used when creating JSON books. If you wish for modpack developers to
     * be able to use your Category, Entry, or Page in their book, you must register one of these.
     * <p>
     * Add your TypeReader during FMLPreInitializationEvent. The list will be queried for the only time during FMLInitializationEvent.
     *
     * @param typeReader - ITypeReader to register.
     * @see amerifrance.guideapi.util.json.serialization.TypeReaders
     */
    public static void addTypeReader(ITypeReader typeReader) {
        if (!TYPE_READERS.contains(typeReader))
            TYPE_READERS.add(typeReader);
    }

    public static List<ITypeReader> getTypeReaders() {
        return new ArrayList<ITypeReader>(TYPE_READERS);
    }

    /**
     * Obtains a new ItemStack associated with the provided book.
     *
     * @param book - The book to get an ItemStack for.
     * @return - The ItemStack associated with the provided book.
     */
    public static ItemStack getStackFromBook(Book book) {
        if (BOOKS.containsValue(book))
            return new ItemStack(guideBook, 1, BOOKS.getValues().indexOf(book));

        return null;
    }

    /**
     * Helper method for setting a model for your book.
     * <p>
     * Use if you wish to use a custom model.
     * <p>
     * Only call <b>AFTER</b> you have registered your book.
     *
     * @param book        - Book to set model for
     * @param modelLoc    - Location of the model file
     * @param variantName - Variant to use
     */
    @SideOnly(Side.CLIENT)
    public static void setModel(Book book, ResourceLocation modelLoc, String variantName) {
        ModelLoader.setCustomModelResourceLocation(
                guideBook,
                BOOKS.getValues().indexOf(book),
                new ModelResourceLocation(modelLoc, variantName)
        );
    }

    /**
     * Helper method for setting a model for your book.
     * <p>
     * Use if you wish to use the default model with color.
     * <p>
     * Only call <b>AFTER</b> you have registered your book.
     *
     * @param book - Book to set model for
     */
    @SideOnly(Side.CLIENT)
    public static void setModel(Book book) {
        setModel(book, new ResourceLocation("guideapi", "ItemGuideBook"), "inventory");
    }
}
