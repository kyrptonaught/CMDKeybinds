package net.kyrptonaught.cmdkeybind.config;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.cmdkeybind.CmdKeybindMod;
import net.kyrptonaught.cmdkeybind.MacroTypes.BaseMacro;
import net.kyrptonaught.cmdkeybind.config.clothconfig.ButtonEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public String getModId() {
        return CmdKeybindMod.MOD_ID;
    }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return (screen) -> buildScreen(screen);
    }

    private static Screen buildScreen(Screen screen) {
        ConfigOptions options = CmdKeybindMod.getConfig();
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen).setTitle(new TranslatableText("Macros"));

        builder.setSavingRunnable(() -> {
            CmdKeybindMod.config.save();
            CmdKeybindMod.buildMacros();
        });
        ConfigCategory category = builder.getOrCreateCategory(new TranslatableText("key.cmdkeybind.config.category.main"));
        ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();
        category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.cmdkeybind.config.enabled"), options.enabled).setDefaultValue(true).setSaveConsumer(val -> options.enabled = val).build());

        for (int i = 0; i < options.macros.size(); i++)
            category.addEntry(buildNewMacro(builder, entryBuilder, i).build());
        category.addEntry(new ButtonEntry(new TranslatableText("key.cmdkeybind.config.add"), buttonEntry -> {
            CmdKeybindMod.addEmptyMacro();
            reloadScreen(builder);
        }));
        return builder.build();
    }

    private static SubCategoryBuilder buildNewMacro(ConfigBuilder builder, ConfigEntryBuilder entryBuilder, int macroNum) {
        ConfigOptions.ConfigMacro macro = CmdKeybindMod.getConfig().macros.get(macroNum);
        SubCategoryBuilder sub = entryBuilder.startSubCategory(new LiteralText(macro.command)).setTooltip(new LiteralText(macro.keyName));
        sub.add(entryBuilder.startTextField(new TranslatableText("key.cmdkeybind.config.macro.command"), macro.command).setDefaultValue("/").setSaveConsumer(cmd -> macro.command = cmd).build());
        sub.add(entryBuilder.startKeyCodeField(new TranslatableText("key.cmdkeybind.config.macro.key"), InputUtil.fromTranslationKey(macro.keyName)).setSaveConsumer(key -> macro.keyName = key.getTranslationKey()).build());
        sub.add(entryBuilder.startKeyCodeField(new TranslatableText("key.cmdkeybind.config.macro.keymod"), InputUtil.fromTranslationKey(macro.keyModName)).setSaveConsumer(key -> macro.keyModName = key.getTranslationKey()).setDefaultValue(InputUtil.UNKNOWN_KEY).build());
        sub.add(entryBuilder.startEnumSelector(new TranslatableText("key.cmdkeybind.config.macrotype"), BaseMacro.MacroType.class, macro.macroType).setSaveConsumer(val -> macro.macroType = val).build());
        sub.add(entryBuilder.startIntField(new TranslatableText("key.cmdkeybind.config.delay"), macro.delay).setDefaultValue(0).setSaveConsumer(val -> macro.delay = val).build());
        sub.add(new ButtonEntry(new TranslatableText("key.cmdkeybind.config.remove"), buttonEntry -> {
            CmdKeybindMod.getConfig().macros.remove(macroNum);
            reloadScreen(builder);
        }));
        return sub;
    }

    private static void reloadScreen(ConfigBuilder builder) {
        builder.getSavingRunnable().run();
        ((ClothConfigScreen) MinecraftClient.getInstance().currentScreen).saveAll(false);
        MinecraftClient.getInstance().openScreen(buildScreen(builder.getParentScreen()));
    }
}