package xyz.brassgoggledcoders.podium.compat.immersiveengineering.bookbehavior;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import xyz.brassgoggledcoders.podium.api.podium.IPodium;
import xyz.brassgoggledcoders.podium.api.podium.PodiumBehavior;

public class ManualPodiumBehavior extends PodiumBehavior {
    public ManualPodiumBehavior(IPodium podium) {
        super(podium);
    }

    @Override
    public ActionResultType activate(PlayerEntity playerEntity) {
        return ActionResultType.SUCCESS;
    }
}
