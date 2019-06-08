package me.shedaniel.clothconfig2;

import com.google.common.collect.ImmutableList;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod("cloth-config2")
public class ClothConfigInitializer {
    
    public ClothConfigInitializer() {
        Optional<? extends ModContainer> modContainer = ModList.get().getModContainerById("cloth-config2");
        if (modContainer.isPresent()) {
            modContainer.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (minecraft, screen) -> {
                ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen).setTitle("Cloth Mod Config Demo");
                builder.setDefaultBackgroundTexture(new ResourceLocation("minecraft:textures/block/oak_planks.png"));
                ConfigCategory playZone = builder.getOrCreateCategory("Play Zone");
                ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();
                playZone.addEntry(entryBuilder.startBooleanToggle("Simple Boolean", false).buildEntry());
                playZone.addEntry(entryBuilder.startTextField("Simple Boolean", "ab").setDefaultValue(() -> "ab").buildEntry());
                playZone.addEntry(entryBuilder.startLongSlider("Long Slider", 0, -10, 10).setDefaultValue(() -> 0l).buildEntry());
                playZone.addEntry(entryBuilder.startIntField("Integer Field", 2).setDefaultValue(() -> 2).setMin(2).setMax(99).buildEntry());
                SubCategoryBuilder randomCategory = entryBuilder.startSubCategory("Random Sub-Category");
                randomCategory.add(entryBuilder.startTextDescription("§7This is a promotional message brought to you by Danielshe. Shop your favorite Lil Tater at store.liltater.com!").setTooltipSupplier(() -> Optional.of(new String[]{"This is an example tooltip."})).buildEntry());
                randomCategory.add(entryBuilder.startSubCategory("Sub-Sub-Category", ImmutableList.of(entryBuilder.startEnumSelector("Enum Field No. 1", DemoEnum.class, DemoEnum.CONSTANT_2).setDefaultValue(() -> DemoEnum.CONSTANT_1).buildEntry(), entryBuilder.startEnumSelector("Enum Field No. 2", DemoEnum.class, DemoEnum.CONSTANT_2).setDefaultValue(() -> DemoEnum.CONSTANT_1).buildEntry())).buildEntry());
                for(int i = 0; i < 10; i++)
                    randomCategory.add(entryBuilder.startIntSlider("Integer Slider No. " + (i + 1), 0, -99, 99).buildEntry());
                playZone.addEntry(randomCategory.buildEntry());
                ConfigCategory enumZone = builder.getOrCreateCategory("Enum Zone");
                enumZone.setCategoryBackground(new ResourceLocation("minecraft:textures/block/stone.png"));
                enumZone.addEntry(entryBuilder.startEnumSelector("Enum Field", DemoEnum.class, DemoEnum.CONSTANT_2).setDefaultValue(() -> DemoEnum.CONSTANT_1).buildEntry());
                return builder.build();
            });
        }
    }
    
    private static enum DemoEnum {
        CONSTANT_1("Constant 1"),
        CONSTANT_2("Constant 2"),
        CONSTANT_3("Constant 3");
        
        private final String key;
        
        private DemoEnum(String key) {
            this.key = key;
        }
        
        @Override
        public String toString() {
            return this.key;
        }
    }
    
}
