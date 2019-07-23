package net.kyrptonaught.cmdkeybind.config;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.cmdkeybind.CmdKeybindMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import java.util.Optional;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public String getModId() {
        return CmdKeybindMod.MOD_ID;
    }

    @Override
    public Optional<Supplier<Screen>> getConfigScreen(Screen screen) {

        return Optional.of(() -> buildScreen(screen));
    }

    public static Screen buildScreen(Screen screen) {
        ConfigOptions options = CmdKeybindMod.config.getConfig();
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen).setTitle("Macros");

        builder.setSavingRunnable(() -> {
            CmdKeybindMod.config.saveConfig();
        });
        ConfigCategory category = builder.getOrCreateCategory("key.cmdkeybind.config.category.main");
        ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();
        category.addEntry(entryBuilder.startBooleanToggle("key.cmdkeybind.config.enabled", true).build());
        category.addEntry(new ButtonEntry("key.cmdkeybind.config.add", buttonEntry -> {
            ((ClothConfigInterface) MinecraftClient.getInstance().currentScreen).cmd_save();
            builder.getSavingRunnable().run();
            options.macros.add(new ConfigOptions.ConfigKeyBind());
            CmdKeybindMod.config.saveConfig();
            MinecraftClient.getInstance().openScreen(ModMenuIntegration.buildScreen(builder.getParentScreen()));
        }));
        for (int i = 0; i < options.macros.size(); i++)
            category.addEntry(buildNewMacro(builder, entryBuilder, i).build());
        return builder.build();
    }

    private static SubCategoryBuilder buildNewMacro(ConfigBuilder builder, ConfigEntryBuilder entryBuilder, int macroNum) {
        ConfigOptions options = CmdKeybindMod.config.getConfig();
        SubCategoryBuilder sub = entryBuilder.startSubCategory("key.cmdkeybind.config.sub.macro").setTooltip(options.macros.get(macroNum).command);
        sub.add(entryBuilder.startTextField("key.cmdkeybind.config.macro.command", options.macros.get(macroNum).command).setSaveConsumer(cmd -> options.macros.get(macroNum).command = cmd).build());
        sub.add(new KeyBindEntry("key.cmdkeybind.config.macro.key", options.macros.get(macroNum).keyName, key -> options.macros.get(macroNum).updateKey(key)));
        sub.add(new ButtonEntry("key.cmdkeybind.config.remove", buttonEntry -> {
            ((ClothConfigInterface) MinecraftClient.getInstance().currentScreen).cmd_save();
            builder.getSavingRunnable().run();
            options.macros.remove(macroNum);
            CmdKeybindMod.config.saveConfig();
            MinecraftClient.getInstance().openScreen(ModMenuIntegration.buildScreen(builder.getParentScreen()));
        }));
        return sub;
    }
}
