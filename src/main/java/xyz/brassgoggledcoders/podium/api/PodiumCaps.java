package xyz.brassgoggledcoders.podium.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import xyz.brassgoggledcoders.podium.api.bookholder.IBookHolder;

public class PodiumCaps {
    @CapabilityInject(IBookHolder.class)
    public static Capability<IBookHolder> BOOK_HOLDER;
}
