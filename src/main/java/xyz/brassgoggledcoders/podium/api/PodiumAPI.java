package xyz.brassgoggledcoders.podium.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.podium.api.event.GetPodiumBehaviorEvent;
import xyz.brassgoggledcoders.podium.api.podium.IPodium;
import xyz.brassgoggledcoders.podium.api.podium.PodiumBehavior;

import javax.annotation.Nullable;

public class PodiumAPI {
    public static final Logger LOGGER = LogManager.getLogger("podium-api");

    @Nullable
    public static PodiumBehavior getPodiumBehavior(IPodium podium, ItemStack itemStack) {
        GetPodiumBehaviorEvent event = new GetPodiumBehaviorEvent(podium, itemStack);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getPodiumBehavior();
    }
}
