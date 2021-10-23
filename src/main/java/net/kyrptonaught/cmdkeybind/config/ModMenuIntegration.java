package net.kyrptonaught.cmdkeybind.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.cmdkeybind.CmdKeybindMod;
import net.kyrptonaught.cmdkeybind.MacroTypes.BaseMacro;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigScreen;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigSection;
import net.kyrptonaught.kyrptconfig.config.screen.items.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModMenuIntegration::buildScreen;
    }

    private static Screen buildScreen(Screen screen) {
        ConfigOptions options = CmdKeybindMod.getConfig();

        ConfigScreen configScreen = new ConfigScreen(screen, new TranslatableText("Macros"));
        configScreen.setSavingEvent(() -> {
            CmdKeybindMod.config.save();
            CmdKeybindMod.buildMacros();
        });
        ConfigSection mainSection = new ConfigSection(configScreen, new TranslatableText("key.cmdkeybind.config.category.main"));
        mainSection.addConfigItem(new BooleanItem(new TranslatableText("key.cmdkeybind.config.enabled"), options.enabled, true).setSaveConsumer(val -> options.enabled = val));

        for (int i = 0; i < options.macros.size(); i++)
            mainSection.addConfigItem(buildNewMacro(mainSection, i));

        mainSection.addConfigItem(new ButtonItem(new TranslatableText("key.cmdkeybind.config.add")).setClickEvent(() -> {
            CmdKeybindMod.addEmptyMacro();
            mainSection.insertConfigItem(buildNewMacro(mainSection, options.macros.size() - 1), mainSection.configs.size() - 1);
        }));
        return configScreen;
    }

    private static SubItem buildNewMacro(ConfigSection configSection, int macroNum) {
        ConfigOptions.ConfigMacro macro = CmdKeybindMod.getConfig().macros.get(macroNum);
        SubItem macroSub = (SubItem) new SubItem(new LiteralText(macro.command)).setToolTip(new LiteralText(macro.keyName));
        macroSub.addConfigItem(new TextItem(new TranslatableText("key.cmdkeybind.config.macro.command"), macro.command, "/").setSaveConsumer(cmd -> macro.command = cmd));
        macroSub.addConfigItem(new KeybindItem(new TranslatableText("key.cmdkeybind.config.macro.key"), macro.keyName, InputUtil.Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_KP_0).getTranslationKey()).setSaveConsumer(key -> macro.keyName = key));
        macroSub.addConfigItem(new KeybindItem(new TranslatableText("key.cmdkeybind.config.macro.keymod"), macro.keyModName, InputUtil.UNKNOWN_KEY.getTranslationKey()).setSaveConsumer(key -> macro.keyModName = key));
        macroSub.addConfigItem(new EnumItem<>(new TranslatableText("key.cmdkeybind.config.macrotype"), BaseMacro.MacroType.values(), macro.macroType, BaseMacro.MacroType.SingleUse).setSaveConsumer(val -> macro.macroType = val));
        macroSub.addConfigItem(new IntegerItem(new TranslatableText("key.cmdkeybind.config.delay"), macro.delay, 0).setSaveConsumer(val -> macro.delay = val));
        macroSub.addConfigItem(new ButtonItem(new TranslatableText("key.cmdkeybind.config.remove")).setClickEvent(() -> {
            CmdKeybindMod.getConfig().macros.remove(macro);
            //configSection.removeConfigItem(macroNum + 1);
            configSection.configs.remove(macroSub);
        }));
        return macroSub;
    }
}