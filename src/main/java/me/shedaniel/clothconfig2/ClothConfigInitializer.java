package me.shedaniel.clothconfig2;

import com.google.common.collect.ImmutableList;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class ClothConfigInitializer implements ClientModInitializer {
    
    public static final Logger LOGGER = LogManager.getFormatterLogger("ClothConfig");
    
    @Override
    public void onInitializeClient() {
        if (FabricLoader.getInstance().isModLoaded("modmenu")) {
            try {
                Class<?> clazz = Class.forName("io.github.prospector.modmenu.api.ModMenuApi");
                Method method = clazz.getMethod("addConfigOverride", String.class, Runnable.class);
                method.invoke(null, "cloth-config2", (Runnable) () -> {
                    ConfigBuilder builder = ConfigBuilder.create().setParentScreen(MinecraftClient.getInstance().currentScreen).setTitle("Cloth Mod Config Demo");
                    builder.setDefaultBackgroundTexture(new Identifier("minecraft:textures/block/oak_planks.png"));
                    ConfigCategory playZone = builder.getOrCreateCategory("Play Zone");
                    ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();
                    playZone.addEntry(entryBuilder.startBooleanToggle("Simple Boolean", false).buildEntry());
                    playZone.addEntry(entryBuilder.startTextField("Simple Boolean", "ab").setDefaultValue(() -> "ab").buildEntry());
                    playZone.addEntry(entryBuilder.startLongSlider("Long Slider", 0, -10, 10).setDefaultValue(() -> 0l).buildEntry());
                    playZone.addEntry(entryBuilder.startIntList("Int List", Arrays.asList(1, 6, 14, 1414)).setTooltip("this is a bad tooltip").setSaveConsumer(integers -> integers.forEach(System.out::println)).setDefaultValue(Arrays.asList(1, 6, 14, 1414)).build());
                    playZone.addEntry(entryBuilder.startStrList("Party Member List", Arrays.asList("Tim", "Daniel", "John")).setTooltip("A list of party members.").setDefaultValue(Arrays.asList("Tim", "Daniel", "John")).build());
                    playZone.addEntry(entryBuilder.startIntField("Integer Field", 2).setDefaultValue(() -> 2).setMin(2).setMax(99).buildEntry());
                    SubCategoryBuilder randomCategory = entryBuilder.startSubCategory("Random Sub-Category");
                    randomCategory.add(entryBuilder.startTextDescription("§7This is a promotional message brought to you by Danielshe. Shop your favorite Lil Tater at store.liltater.com!").setTooltipSupplier(() -> Optional.of(new String[]{"This is an example tooltip."})).buildEntry());
                    randomCategory.add(entryBuilder.startSubCategory("Sub-Sub-Category", ImmutableList.of(entryBuilder.startEnumSelector("Enum Field No. 1", DemoEnum.class, DemoEnum.CONSTANT_2).setDefaultValue(() -> DemoEnum.CONSTANT_1).buildEntry(), entryBuilder.startEnumSelector("Enum Field No. 2", DemoEnum.class, DemoEnum.CONSTANT_2).setDefaultValue(() -> DemoEnum.CONSTANT_1).buildEntry())).buildEntry());
                    for(int i = 0; i < 10; i++)
                        randomCategory.add(entryBuilder.startIntSlider("Integer Slider No. " + (i + 1), 0, -99, 99).buildEntry());
                    playZone.addEntry(randomCategory.buildEntry());
                    ConfigCategory enumZone = builder.getOrCreateCategory("Enum Zone");
                    enumZone.setCategoryBackground(new Identifier("minecraft:textures/block/stone.png"));
                    enumZone.addEntry(entryBuilder.startEnumSelector("Enum Field", DemoEnum.class, DemoEnum.CONSTANT_2).setDefaultValue(() -> DemoEnum.CONSTANT_1).buildEntry());
                    ConfigCategory partyZone = builder.getOrCreateCategory("Party Zone");
                    MinecraftClient.getInstance().openScreen(builder.build());
                });
            } catch (Exception e) {
                ClothConfigInitializer.LOGGER.error("[ClothConfig] Failed to add test config override for ModMenu!", e);
            }
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
