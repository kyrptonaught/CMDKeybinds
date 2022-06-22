package net.kyrptonaught.cmdkeybind.config;

import net.kyrptonaught.cmdkeybind.CmdKeybindMod;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigScreen;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigSection;
import net.kyrptonaught.kyrptconfig.config.screen.items.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class MacroScreenFactory {

    public static Screen buildScreen(Screen screen) {
        ConfigOptions options = CmdKeybindMod.getConfig();

        ConfigScreen configScreen = new ConfigScreen(screen, Text.translatable("key.cmdkeybind.config.title"));
        configScreen.setSavingEvent(() -> {
            CmdKeybindMod.config.save();
            CmdKeybindMod.buildMacros();
        });
        ConfigSection mainSection = new ConfigSection(configScreen, Text.translatable("key.cmdkeybind.config.category.main"));
        mainSection.addConfigItem(new BooleanItem(Text.translatable("key.cmdkeybind.config.enabled"), options.enabled, true).setSaveConsumer(val -> options.enabled = val));
        mainSection.addConfigItem(new KeybindItem(Text.translatable("key.cmdkeybind.config.openmacrokeybind"), options.openMacroScreenKeybind.rawKey, "key.keyboard.unknown").setSaveConsumer(val -> options.openMacroScreenKeybind.setRaw(val)));
        for (int i = 0; i < options.macros.size(); i++)
            mainSection.addConfigItem(new ConfigMacroItem(mainSection, options.macros.get(i)));

        mainSection.addConfigItem(new ButtonItem(Text.translatable("key.cmdkeybind.config.add")).setClickEvent(() -> {
            CmdKeybindMod.addEmptyMacro();
            mainSection.insertConfigItem(new ConfigMacroItem(mainSection, options.macros.get(options.macros.size() - 1)), mainSection.configs.size() - 1);
        }));
        return configScreen;
    }
}
