package xyz.brassgoggledcoders.podium;

import com.tterrag.registrate.Registrate;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.brassgoggledcoders.podium.api.bookholder.IBookHolder;
import xyz.brassgoggledcoders.podium.capability.EmptyCapabilityStorage;
import xyz.brassgoggledcoders.podium.content.PodiumBlocks;

@Mod(Podium.ID)
public class Podium {
    public static final String ID = "podium";

    private static final NonNullLazy<Registrate> REGISTRATE = NonNullLazy.of(() -> Registrate.create(ID));

    public Podium() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::commonSetup);

        PodiumBlocks.setup();
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(IBookHolder.class, new EmptyCapabilityStorage<>(), () -> null);
    }

    public static Registrate getRegistrate() {
        return REGISTRATE.get();
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}
