package xyz.brassgoggledcoders.podium.compat.immersiveengineering;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class PodiumIE {
    public static final String IE_ID = "immersiveengineering";

    public static final RegistryObject<Item> MANUAL = RegistryObject.of(new ResourceLocation(IE_ID, "manual"),
            ForgeRegistries.ITEMS);

    public static void setup() {
        if (ModList.get().isLoaded(IE_ID)) {
            MinecraftForge.EVENT_BUS.register(PodiumIEEventHandler.class);
        }
    }
}
