package net.kyrptonaught.cmdkeybind;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.cmdkeybind.config.KeyBindEntry;
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
        ConfigOptions options = CmdKeybindMod.config.getConfig();
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen).setTitle("Macros");
        builder.setSavingRunnable(() -> {
            CmdKeybindMod.config.saveConfig();
        });
        ConfigCategory category = builder.getOrCreateCategory("key.cmdkeybind.config.category.main");
        ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();
        category.addEntry(entryBuilder.startBooleanToggle("key.cmdkeybind.config.enabled", true).build());
        category.addEntry(entryBuilder.startIntSlider("key.cmdkeybind.config.numMacros", options.macros.size(), 1, 20).setSaveConsumer(newSize -> adjustSize(newSize, options)).build());
        for (int i = 0; i < options.macros.size(); i++)
            category.addEntry(buildNewMacro(entryBuilder, i).build());
        return Optional.of(() -> builder.build());
    }

    private SubCategoryBuilder buildNewMacro(ConfigEntryBuilder entryBuilder, int macroNum) {
        ConfigOptions options = CmdKeybindMod.config.getConfig();
        SubCategoryBuilder sub = entryBuilder.startSubCategory("key.cmdkeybind.config.sub.macro").setTooltip(options.macros.get(macroNum).command);
        sub.add(entryBuilder.startTextField("key.cmdkeybind.config.macro.command", options.macros.get(macroNum).command).setSaveConsumer(cmd -> options.macros.get(macroNum).command = cmd).build());
        sub.add(new KeyBindEntry("key.cmdkeybind.config.macro.key",options.macros.get(macroNum).getKeyName(),key -> options.macros.get(macroNum).updateKey(key.getLeft(),key.getRight())));
        return sub;
    }

    private void adjustSize(int newSize, ConfigOptions config) {
        if (newSize > config.macros.size()) {
            for (int i = 0; i < newSize - config.macros.size(); i++)
                config.macros.add(new ConfigOptions.MacroKeyBind());
        }
        if (newSize < config.macros.size()) {
            for (int i = 0; i < config.macros.size() - newSize; i++)
                config.macros.remove(config.macros.size() - 1);
        }
    }
}
