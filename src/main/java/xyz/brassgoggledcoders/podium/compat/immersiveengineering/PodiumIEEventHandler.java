package xyz.brassgoggledcoders.podium.compat.immersiveengineering;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import xyz.brassgoggledcoders.podium.api.event.GetPodiumBehaviorEvent;
import xyz.brassgoggledcoders.podium.compat.immersiveengineering.bookbehavior.ClientManualPodiumBehavior;
import xyz.brassgoggledcoders.podium.compat.immersiveengineering.bookbehavior.ManualPodiumBehavior;

public class PodiumIEEventHandler {

    @SubscribeEvent
    public static void getPodiumBehavior(GetPodiumBehaviorEvent event) {
        if (event.getItemStack().getItem() == PodiumIE.MANUAL.get()) {
            event.setPodiumBehavior(DistExecutor.unsafeRunForDist(
                    () -> () -> new ClientManualPodiumBehavior(event.getPodium()),
                    () -> () -> new ManualPodiumBehavior(event.getPodium())
            ));
        }
    }
}
