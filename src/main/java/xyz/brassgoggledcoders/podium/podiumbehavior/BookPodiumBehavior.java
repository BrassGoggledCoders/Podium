package xyz.brassgoggledcoders.podium.podiumbehavior;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.LecternContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import xyz.brassgoggledcoders.podium.api.podium.IPodium;
import xyz.brassgoggledcoders.podium.api.podium.PodiumBehavior;
import xyz.brassgoggledcoders.podium.container.reference.SingleValueFunctionIntArray;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.ToIntFunction;

public class BookPodiumBehavior extends PodiumBehavior {
    private final ToIntFunction<ItemStack> pageLength;
    private int page;

    public BookPodiumBehavior(IPodium podium, ToIntFunction<ItemStack> pageLength) {
        super(podium);
        this.pageLength = pageLength;
        this.page = 0;
    }

    @Override
    public ActionResultType activate(PlayerEntity playerEntity) {
        if (playerEntity instanceof ServerPlayerEntity) {
            playerEntity.openContainer(new LecternContainerProvider());
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public int getComparatorSignal() {
        int pages = this.pageLength.applyAsInt(this.getPodium().getDisplayItemStack());
        float f = pages > 1 ? (float)this.getPage() / ((float)pages - 1.0F) : 1.0F;
        return MathHelper.floor(f * 14.0F) + 1;
    }

    private int getPage() {
        return page;
    }

    private void setPage(int pageIn) {
        int i = MathHelper.clamp(pageIn, 0, this.pageLength.applyAsInt(this.getPodium()
                .getDisplayItemStack()) - 1);
        if (i != this.page) {
            this.page = i;
            this.getPodium().requestSave();
            this.getPodium().pulseRedstone();
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putInt("page", page);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        page = nbt.getInt("page");
    }

    public class LecternContainerProvider implements INamedContainerProvider {
        @Override
        @Nonnull
        public ITextComponent getDisplayName() {
            return new TranslationTextComponent("container.lectern");
        }

        @Nullable
        @Override
        @ParametersAreNonnullByDefault
        public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
            return new LecternContainer(id, BookPodiumBehavior.super.getPodium().getPodiumInventory(),
                    new SingleValueFunctionIntArray(BookPodiumBehavior.this::getPage, BookPodiumBehavior.this::setPage));
        }
    }
}
