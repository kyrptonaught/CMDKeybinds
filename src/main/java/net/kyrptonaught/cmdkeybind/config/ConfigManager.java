package net.kyrptonaught.cmdkeybind.config;

import blue.endless.jankson.Jankson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.cmdkeybind.CmdKeybindMod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().create();
    private static final Jankson JANKSON = Jankson.builder().build();
    ConfigOptions config;
    private final File configFile;

    public ConfigManager() {
        this.configFile = new File(FabricLoader.getInstance().getConfigDirectory(), CmdKeybindMod.MOD_ID + "config.json5");
    }

    public ConfigOptions getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            if (!configFile.exists() && !configFile.createNewFile()) {
                System.out.println(CmdKeybindMod.MOD_ID + " Failed to save config! Overwriting with default config.");
                config = new ConfigOptions();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String result = JANKSON.toJson(config).toJson(true, true, 0);
            if (!configFile.exists())
                configFile.createNewFile();
            FileOutputStream out = new FileOutputStream(configFile, false);

            out.write(result.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(CmdKeybindMod.MOD_ID + " Failed to save config! Overwriting with default config.");
            config = new ConfigOptions();
            return;
        }
    }

    public void loadConfig() {
        if (!configFile.exists() || !configFile.canRead()) {
            System.out.println(CmdKeybindMod.MOD_ID + " Config not found! Creating one.");
            config = new ConfigOptions();
            saveConfig();
            return;
        }
        try {
            String regularized = JANKSON.load(configFile).toJson(false, false, 0);
            config = GSON.fromJson(regularized, ConfigOptions.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(CmdKeybindMod.MOD_ID + " Failed to load config! Overwriting with default config.");
            config = new ConfigOptions();
        }
        if (config.macros.size() == 0) {
            CmdKeybindMod.addEmptyMacro();
        }
        saveConfig();
    }
}
