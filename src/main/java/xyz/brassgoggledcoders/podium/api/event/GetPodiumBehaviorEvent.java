package xyz.brassgoggledcoders.podium.api.event;

import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import xyz.brassgoggledcoders.podium.api.bookholder.IBookHolder;
import xyz.brassgoggledcoders.podium.api.podium.IPodium;
import xyz.brassgoggledcoders.podium.api.podium.PodiumBehavior;

import javax.annotation.Nonnull;

public class GetPodiumBehaviorEvent extends Event {
    private final ItemStack itemStack;
    private final IPodium podium;
    private PodiumBehavior podiumBehavior;

    public GetPodiumBehaviorEvent(IPodium podium, @Nonnull ItemStack itemStack) {
        this.podium = podium;
        this.itemStack = itemStack;
        this.podiumBehavior = null;
    }

    public IPodium getPodium() {
        return this.podium;
    }

    public PodiumBehavior getPodiumBehavior() {
        return this.podiumBehavior;
    }

    public void setPodiumBehavior(PodiumBehavior podiumBehavior) {
        this.podiumBehavior = podiumBehavior;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
