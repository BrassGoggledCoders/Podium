package xyz.brassgoggledcoders.podium.content;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.block.material.Material;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.podium.Podium;
import xyz.brassgoggledcoders.podium.block.PodiumBlock;
import xyz.brassgoggledcoders.podium.renderer.PodiumTileEntityRenderer;
import xyz.brassgoggledcoders.podium.tileentity.PodiumTileEntity;

public class PodiumBlocks {

    public static final BlockEntry<PodiumBlock> PODIUM = Podium.getRegistrate()
            .object("podium")
            .block(Material.WOOD, PodiumBlock::new)
            .lang("Podium")
            .blockstate((context, provider) -> provider.horizontalBlock(context.get(), provider.models()
                    .getExistingFile(Podium.rl("block/podium")))
            )
            .item()
            .recipe((context, provider) -> ShapedRecipeBuilder.shapedRecipe(context.get())
                    .patternLine("PPP")
                    .patternLine(" F ")
                    .patternLine("SSS")
                    .key('P', ItemTags.PLANKS)
                    .key('F', ItemTags.FENCES)
                    .key('S', ItemTags.WOODEN_SLABS)
                    .addCriterion("has_item", RegistrateRecipeProvider.hasItem(ItemTags.PLANKS))
                    .build(provider)
            )
            .group(() -> ItemGroup.DECORATIONS)
            .build()
            .tileEntity(PodiumTileEntity::new)
            .renderer(() -> PodiumTileEntityRenderer::new)
            .build()
            .register();

    public static final RegistryEntry<TileEntityType<PodiumTileEntity>> PODIUM_TILE_ENTITY =
            PODIUM.getSibling(ForgeRegistries.TILE_ENTITIES);

    public static void setup() {

    }
}
