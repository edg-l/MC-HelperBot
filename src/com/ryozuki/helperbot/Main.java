package com.ryozuki.helperbot;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class Main extends JavaPlugin {
    private FileConfiguration config = null;
    private Map<String, String> QA = null;

    @Override
    public void onEnable() {
        super.onEnable();

        this.config = this.getConfig();
        config.addDefault("BotName", "<HelperBot>: ");
        config.addDefault("IgnoreQuestionMark", true);

        QA = new HashMap<>();
        QA.put("How are you", "I'm fine thanks :P");
        QA.put("How (i|to) spawn", "Try /spawn");

        try {
            config.getConfigurationSection("QA").getValues(false);
        }
        catch (NullPointerException e) {
            config.createSection("QA", QA);
        }

        saveConfig();

        this.getCommand("setbotname").setExecutor(new BotNameCommand(this));
        this.getCommand("reloadbot").setExecutor(new ReloadCommand(this));
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void saveConfig() {
        config.options().copyDefaults(true);
        super.saveConfig();
    }

    boolean getIgnore() {
        return  config.getBoolean("IgnoreQuestionMark");
    }

    String getBotName() {
        return config.getString("BotName");
    }

    void setBotName(String newName) {
        config.set("BotName", newName);
        saveConfig();
    }

    Map<String, Object> getQA() {
        return config.getConfigurationSection("QA").getValues(false);
    }

    void setConfig(FileConfiguration config) {
        this.config = config;
    }


}