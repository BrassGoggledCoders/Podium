package xyz.brassgoggledcoders.podium.content;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.podium.Podium;
import xyz.brassgoggledcoders.podium.item.QuillAndInkWellItem;

public class PodiumItems {

    public static final RegistryEntry<Item> QUILL_AND_EMPTY_INKWELL = Podium.getRegistrate()
            .object("quill_and_inkwell")
            .item(Item::new)
            .lang("Quill and Inkwell")
            .group(() -> ItemGroup.MISC)
            .recipe((context, provider) -> ShapelessRecipeBuilder.shapelessRecipe(context.get())
                    .addIngredient(Items.GLASS_BOTTLE)
                    .addIngredient(Tags.Items.FEATHERS)
                    .addCriterion("has_item", RegistrateRecipeProvider.hasItem(Items.GLASS_BOTTLE))
                    .build(provider)
            )
            .register();

    public static final RegistryEntry<QuillAndInkWellItem> QUILL_AND_INK_WELL_BLACK =
            createQuillAndInkWellItem(DyeColor.BLACK)
                    .register();

    private static ItemBuilder<QuillAndInkWellItem, Registrate> createQuillAndInkWellItem(DyeColor dyeColor) {
        return Podium.getRegistrate()
                .object(String.format("quill_and_inkwell_%s", dyeColor.getString()))
                .item(properties -> new QuillAndInkWellItem(dyeColor, properties))
                .properties(properties -> properties.maxDamage(10))
                .group(() -> ItemGroup.MISC)
                .lang(String.format("Quill and Inkwell with %s Ink", capitalizeWord(dyeColor.getTranslationKey())))
                .recipe((context, provider) -> {
                            ShapelessRecipeBuilder.shapelessRecipe(context.get())
                                    .addIngredient(dyeColor.getTag())
                                    .addIngredient(QUILL_AND_EMPTY_INKWELL.get())
                                    .addCriterion("has_item", RegistrateRecipeProvider.hasItem(dyeColor.getTag()))
                                    .build(provider);
                            ShapelessRecipeBuilder.shapelessRecipe(Items.WRITABLE_BOOK)
                                    .addIngredient(Items.BOOK)
                                    .addIngredient(context.get())
                                    .addCriterion("has_item", RegistrateRecipeProvider.hasItem(context.get()))
                                    .build(provider, Podium.rl(String.format("writable_book_from_quill_and_inkwell_%s",
                                            dyeColor.getString())));
                        }
                );
    }

    private static String capitalizeWord(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static void setup() {

    }
}
