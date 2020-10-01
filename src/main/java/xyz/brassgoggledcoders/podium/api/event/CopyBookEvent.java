package xyz.brassgoggledcoders.podium.api.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import xyz.brassgoggledcoders.podium.api.bookholder.IBookHolder;

@Cancelable
public class CopyBookEvent extends Event {
    private final IBookHolder bookHolder;
    private final PlayerEntity copier;
    private final Hand writingHand;
    private final DyeColor inkColor;

    private ItemStack writingUtensil;
    private boolean copied = false;

    public CopyBookEvent(IBookHolder bookHolder, PlayerEntity copier, Hand writingHand, ItemStack writingUtensil, DyeColor inkColor) {
        this.bookHolder = bookHolder;
        this.copier = copier;
        this.writingHand = writingHand;
        this.writingUtensil = writingUtensil;
        this.inkColor = inkColor;
    }

    public IBookHolder getBookHolder() {
        return bookHolder;
    }

    public PlayerEntity getCopier() {
        return copier;
    }

    public ItemStack getWritingUtensil() {
        return writingUtensil;
    }

    public DyeColor getInkColor() {
        return inkColor;
    }

    public boolean isCopied() {
        return copied;
    }

    public void setCopied(boolean copied) {
        this.copied = copied;
    }

    public Hand getWritingHand() {
        return writingHand;
    }

    public void useInk() {
        if (this.getWritingUtensil().hasContainerItem()) {
            this.writingUtensil = this.getWritingUtensil().getContainerItem();
        } else if (this.getWritingUtensil().isDamageable()) {
            this.getWritingUtensil().damageItem(1, this.copier, playerEntity -> playerEntity.sendBreakAnimation(
                    this.getWritingHand()));
        } else {
            this.getWritingUtensil().shrink(1);
        }
    }
}
