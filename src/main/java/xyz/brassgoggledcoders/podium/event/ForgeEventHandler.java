package xyz.brassgoggledcoders.podium.event;

import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.podium.Podium;
import xyz.brassgoggledcoders.podium.api.PodiumCaps;
import xyz.brassgoggledcoders.podium.api.bookholder.IBookHolder;
import xyz.brassgoggledcoders.podium.api.bookholder.LecternBookHolder;
import xyz.brassgoggledcoders.podium.api.event.GetPageContentsEvent;
import xyz.brassgoggledcoders.podium.api.event.GetPodiumBehaviorEvent;
import xyz.brassgoggledcoders.podium.capability.BasicCapabilityProvider;
import xyz.brassgoggledcoders.podium.podiumbehavior.BookPodiumBehavior;

@EventBusSubscriber(modid = Podium.ID, bus = Bus.FORGE)
public class ForgeEventHandler {
    public static final ResourceLocation BOOK_HOLDER = Podium.rl("book_holder");

    @SubscribeEvent
    public static void onAttachTileEntityCapabilities(AttachCapabilitiesEvent<TileEntity> attachCapabilitiesEvent) {
        if (attachCapabilitiesEvent.getObject() instanceof LecternTileEntity) {
            BasicCapabilityProvider<IBookHolder> lecternBookHolder = new BasicCapabilityProvider<>(
                    PodiumCaps.BOOK_HOLDER, new LecternBookHolder((LecternTileEntity) attachCapabilitiesEvent.getObject()));
            attachCapabilitiesEvent.addCapability(BOOK_HOLDER, lecternBookHolder);
            attachCapabilitiesEvent.addListener(lecternBookHolder::invalidate);
        }
    }

    @SubscribeEvent
    public static void handleGetPodiumBehavior(GetPodiumBehaviorEvent event) {
        Item item = event.getPodium().getDisplayItemStack().getItem();
        if (item == Items.WRITABLE_BOOK || item == Items.WRITTEN_BOOK) {
            event.setPodiumBehavior(new BookPodiumBehavior(event.getPodium()));
        }
    }

    @SubscribeEvent
    public static void handleGetPageContents(GetPageContentsEvent event) {
        ItemStack bookStack = event.getBookHolder().getItemStack();
        Item item = bookStack.getItem();
        int page = -1;
        String pageString = event.getBookHolder().getOpenPage();
        if (pageString != null) {
            try {
                page = Integer.parseInt(pageString);
            } catch (NumberFormatException exception) {
                //Do nothing.
            }
        }
        if (item == Items.WRITTEN_BOOK && page >= 0) {
            if (WrittenBookItem.validBookTagContents(bookStack.getTag())) {
                CompoundNBT bookNBT = bookStack.getTag();
                ListNBT pagesNBT = bookNBT.getList("pages", Constants.NBT.TAG_STRING);
                String currentPage = pagesNBT.getString(page);
                ITextComponent textComponent = TextComponent.Serializer.getComponentFromJson(currentPage);
                if (textComponent != null) {
                    event.setPageContent(textComponent.getString());
                }
            }
        } else if (item == Items.WRITABLE_BOOK && page >= 0) {
            if (WritableBookItem.isNBTValid((bookStack.getTag()))) {
                CompoundNBT bookNBT = bookStack.getTag();
                ListNBT pagesNBT = bookNBT.getList("pages", Constants.NBT.TAG_STRING);
                event.setPageContent(pagesNBT.getString(page));
            }
        }
    }
}

