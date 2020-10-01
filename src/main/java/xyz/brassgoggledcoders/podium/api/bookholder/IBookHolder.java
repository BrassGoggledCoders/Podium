package xyz.brassgoggledcoders.podium.api.bookholder;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IBookHolder {
    @Nonnull
    ItemStack getItemStack();

    @Nullable
    String getOpenPage();

    @Nullable
    String getPageContents();
}
