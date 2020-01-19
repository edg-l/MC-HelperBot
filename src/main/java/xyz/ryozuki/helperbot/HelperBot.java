package xyz.ryozuki.helperbot;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class HelperBot extends JavaPlugin {

    private final List<Question> questions = new ArrayList<>();

    private boolean placeHolderApiEnabled = false;
    private CommandHandler commandHandler;
    private AutoCompleter autoCompleter;
    private ChatListener chatListener;

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public AutoCompleter getAutoCompleter() {
        return autoCompleter;
    }

    public ChatListener getChatListener() {
        return chatListener;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeHolderApiEnabled = true;
            getLogger().info("PlaceholderApi support enabled.");
        }

        Metrics metrics = new Metrics(this);

        metrics.addCustomChart(
                new Metrics.SimplePie("placeholderapi", () ->
                        placeHolderApiEnabled ? "yes" : "no")
        );

        try {
            reloadQuestions();
        } catch (SecurityException e) {
            getLogger().severe(e.toString());
        }

        commandHandler = new CommandHandler(this);
        autoCompleter = new AutoCompleter(commandHandler);
        chatListener = new ChatListener(this);

        final PluginCommand command = getCommand("helperbot");

        if (command != null) {
            command.setExecutor(commandHandler);
            command.setTabCompleter(autoCompleter);
            getServer().getPluginManager().registerEvents(chatListener, this);
        } else {
            getLogger().severe("Error occurred while loading the plugin! Error: Couldn't find helperbot command!");
            this.setEnabled(false);
        }
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

            if (list == null) {
                getLogger().warning("Couldn't load questions!");
                return;
            }

            for (LinkedHashMap<String, Object> map : list) {
                boolean disable = (boolean) map.getOrDefault("disable", false);
                if (disable)
                    continue;
                String question = (String) map.get("question");
                String answer = (String) map.get("answer");
                int cooldown = (int) map.getOrDefault("cooldown", 0);
                boolean broadcast = (boolean) map.getOrDefault("broadcast", true);
                boolean broadcastQuestion = (boolean) map.getOrDefault("broadcast_question", true);
                questions.add(new Question(question, answer, cooldown, broadcast, broadcastQuestion));
            }

            getLogger().info(String.format("Loaded %s questions!", questions.size()));
        }
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public boolean isPlaceHolderApiEnabled() {
        return placeHolderApiEnabled;
    }
}
