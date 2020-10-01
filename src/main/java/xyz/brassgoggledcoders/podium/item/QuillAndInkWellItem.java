package xyz.brassgoggledcoders.podium.item;

import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import xyz.brassgoggledcoders.podium.api.PodiumAPI;
import xyz.brassgoggledcoders.podium.api.PodiumCaps;
import xyz.brassgoggledcoders.podium.content.PodiumItems;

import javax.annotation.Nonnull;
import java.util.Random;

public class QuillAndInkWellItem extends Item {
    private static final Random random = new Random();
    private final DyeColor inkColor;

    public QuillAndInkWellItem(@Nonnull DyeColor inkColor, Properties properties) {
        super(properties);
        this.inkColor = inkColor;
    }

    @Override
    @Nonnull
    public ActionResultType onItemUse(@Nonnull ItemUseContext context) {
        TileEntity tileEntity = context.getWorld().getTileEntity(context.getPos());
        if (tileEntity != null && context.getPlayer() != null) {
            boolean copied = tileEntity.getCapability(PodiumCaps.BOOK_HOLDER)
                    .map(bookHolder -> PodiumAPI.copyBook(bookHolder, context.getPlayer(), context.getHand(),
                            context.getItem(), this.getInkColor())
                    ).orElse(false);
            return copied ? ActionResultType.func_233537_a_(context.getWorld().isRemote()) : ActionResultType.FAIL;
        }
        return super.onItemUse(context);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack resultItemStack = itemStack.copy();
        if (resultItemStack.attemptDamageItem(1, random, null)) {
            resultItemStack = new ItemStack(PodiumItems.QUILL_AND_EMPTY_INKWELL.get());
        }
        return resultItemStack;
    }

    public DyeColor getInkColor() {
        return inkColor;
    }
}
