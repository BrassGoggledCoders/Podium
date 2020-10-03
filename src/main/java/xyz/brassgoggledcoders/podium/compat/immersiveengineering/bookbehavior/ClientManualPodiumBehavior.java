package xyz.brassgoggledcoders.podium.compat.immersiveengineering.bookbehavior;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.gui.ManualScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import xyz.brassgoggledcoders.podium.api.podium.IPodium;
import xyz.brassgoggledcoders.podium.api.podium.PodiumBehavior;

public class ClientManualPodiumBehavior extends PodiumBehavior {
    private ManualScreen manualScreen;

    public ClientManualPodiumBehavior(IPodium podium) {
        super(podium);
    }

    @Override
    public ActionResultType activate(PlayerEntity playerEntity) {
        if (this.manualScreen == null) {
            this.manualScreen = ManualHelper.getManual().getGui(false);
        }
        Minecraft.getInstance().displayGuiScreen(manualScreen);
        return ActionResultType.SUCCESS;
    }
}
