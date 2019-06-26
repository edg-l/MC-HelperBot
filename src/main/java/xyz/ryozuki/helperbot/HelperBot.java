package xyz.ryozuki.helperbot;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;

public class HelperBot extends JavaPlugin {
    private boolean placeHolderApiEnabled = false;
    private static HelperBot instance;
    private List<Question> questions = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        reloadConfig();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeHolderApiEnabled = true;
            getLogger().info("PlaceholderApi support enabled.");
        }

        Metrics metrics = new Metrics(this);
        metrics.addCustomChart(new Metrics.SimplePie("placeholderapi", () -> placeHolderApiEnabled ? "yes" : "no"));

        try {
            reloadQuestions();
        } catch (SecurityException e) {
            getLogger().severe(e.toString());
        }

        getCommand("helperbot").setExecutor(new CommandHandler());
        getCommand("helperbot").setTabCompleter(new AutoCompleter());
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    void reloadQuestions() {
        getDataFolder().mkdir();
        readQuestions();
    }

    String getBotName() {
        return getConfig().getString("BotName");
    }

    void setBotName(String newName) {
        getConfig().set("BotName", newName);
        saveConfig();
    }

    void readQuestions() {
        File questionsFile = new File(getDataFolder(), "questions.yaml");

        if (!questionsFile.exists()) {
            saveResource("questions.yaml", false);
            questionsFile = new File(getDataFolder(), "questions.yaml");
        }

        YamlConfiguration questionsConfig = YamlConfiguration.loadConfiguration(questionsFile);

        questions.clear();

        if (questionsConfig.contains("questions")) {
            List<LinkedHashMap<String, Object>> list =
                    (ArrayList<LinkedHashMap<String, Object>>) questionsConfig.getList("questions");

            for (LinkedHashMap<String, Object> map : list) {
                String question = (String) map.get("question");
                String answer = (String) map.get("answer");
                int cooldown = (int) map.getOrDefault("cooldown", 0);
                boolean broadcast = (boolean) map.getOrDefault("broadcast", true);
                boolean disable = (boolean) map.getOrDefault("disable", false);
                if(!disable)
                    questions.add(new Question(question, answer, cooldown, broadcast));
            }

            getLogger().info(String.format("Loaded %s questions!", questions.size()));
        }
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public static HelperBot getInstance() {
        return instance;
    }

    public boolean isPlaceHolderApiEnabled() {
        return placeHolderApiEnabled;
    }
}
