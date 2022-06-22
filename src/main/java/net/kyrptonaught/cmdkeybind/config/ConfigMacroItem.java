package net.kyrptonaught.cmdkeybind.config;

import net.kyrptonaught.cmdkeybind.CmdKeybindMod;
import net.kyrptonaught.cmdkeybind.MacroTypes.BaseMacro;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigSection;
import net.kyrptonaught.kyrptconfig.config.screen.items.*;
import net.kyrptonaught.kyrptconfig.config.screen.items.number.IntegerItem;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ConfigMacroItem extends SubItem<ConfigOptions.ConfigMacro> {
    public ConfigMacroItem(ConfigSection configSection, ConfigOptions.ConfigMacro macro) {
        super(Text.literal(macro.command));
        setToolTip(Text.literal(macro.keyName));

        TextItem command = (TextItem) addConfigItem(new TextItem(Text.translatable("key.cmdkeybind.config.macro.command"), macro.command, "/").setMaxLength(1024).setSaveConsumer(cmd -> macro.command = cmd));
        KeybindItem keyItem = (KeybindItem) addConfigItem(new KeybindItem(Text.translatable("key.cmdkeybind.config.macro.key"), macro.keyName, InputUtil.Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_KP_0).getTranslationKey()).setSaveConsumer(key -> macro.keyName = key));
        KeybindItem keymodItem = (KeybindItem) addConfigItem(new KeybindItem(Text.translatable("key.cmdkeybind.config.macro.keymod"), macro.keyModName, InputUtil.UNKNOWN_KEY.getTranslationKey()).setSaveConsumer(key -> macro.keyModName = key));
        EnumItem<BaseMacro.MacroType> macroType = (EnumItem<BaseMacro.MacroType>) addConfigItem(new EnumItem<>(Text.translatable("key.cmdkeybind.config.macrotype"), BaseMacro.MacroType.values(), macro.macroType, BaseMacro.MacroType.SingleUse).setSaveConsumer(val -> macro.macroType = val));
        IntegerItem delayItem = (IntegerItem) addConfigItem(new IntegerItem(Text.translatable("key.cmdkeybind.config.delay"), macro.delay, 0).setSaveConsumer(val -> macro.delay = val));
        addConfigItem(new ButtonItem(Text.translatable("key.cmdkeybind.config.remove")).setClickEvent(() -> {
            CmdKeybindMod.getConfig().macros.remove(macro);
            configSection.configs.remove(this);
        }));

        command.setValueUpdatedEvent(value -> setTitleText(Text.literal(value)));
        keyItem.setValueUpdatedEvent(value -> setToolTip(Text.literal(value)));
        macroType.setValueUpdatedEvent(value -> delayItem.setHidden(!value.isDelayApplicable()));

        delayItem.setHidden(!macro.macroType.isDelayApplicable());
    }
}
