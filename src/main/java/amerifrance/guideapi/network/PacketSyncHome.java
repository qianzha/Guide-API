package amerifrance.guideapi.network;

import amerifrance.guideapi.api.IGuideItem;
import amerifrance.guideapi.api.util.NBTBookTags;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncHome implements IMessage, IMessageHandler<PacketSyncHome, IMessage> {

    public int page;

    public PacketSyncHome() {
        this.page = -1;
    }

    public PacketSyncHome(int page) {
        this.page = page;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.page = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(page);
    }

    @Override
    public IMessage onMessage(PacketSyncHome message, MessageContext ctx) {
        ItemStack book = ctx.getServerHandler().playerEntity.getHeldItemOffhand();
        if (book == null || !(book.getItem() instanceof IGuideItem))
            book = ctx.getServerHandler().playerEntity.getHeldItemMainhand();

        if (book != null && book.getItem() instanceof IGuideItem) {
            if (message.page != -1) {
                book.getTagCompound().setInteger(NBTBookTags.CATEGORY_PAGE_TAG, message.page);
                book.getTagCompound().removeTag(NBTBookTags.CATEGORY_TAG);
                book.getTagCompound().removeTag(NBTBookTags.ENTRY_TAG);
            }
        }
        return null;
    }
}
