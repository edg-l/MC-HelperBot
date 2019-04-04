package xyz.ryozuki.helperbot;


import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class HelperBot extends JavaPlugin {
    private Map<String, String> questions = new HashMap<>();
    final Pattern pattern = Pattern.compile("(.+)[:](.+)");

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        Metrics metrics = new Metrics(this);
        metrics.addCustomChart(new Metrics.SimplePie("version", () -> getDescription().getVersion()));

        try {
            reload_questions();
        } catch (IOException | SecurityException e) {
            getLogger().severe(e.toString());
        }

        getCommand("helperbot").setExecutor(new CommandHandler(this));
        getCommand("helperbot").setTabCompleter(new AutoCompleter());
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    void reload_questions() throws IOException {
        getDataFolder().mkdir();
        File questionsFile = new File(getDataFolder(), "questions.txt");

        if (!questionsFile.exists()) {
            String[] qas = {
                    "How are you: I'm fine thanks :P",
                    "How (i|to) spawn: Try /spawn",
                    "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b: Please don't share ips!",
                    "\\b(?:[-A-Za-z0-9]+\\.)+[A-Za-z]{2,6}\\b: Please don't share domains!"};
            add_questions(qas);
        } else {
            read_questions();
        }
    }

    boolean shouldIgnoreQuestionMark() {
        return getConfig().getBoolean("IgnoreQuestionMark");
    }

    String getBotName() {
        return getConfig().getString("BotName");
    }

    void setBotName(String newName) {
        getConfig().set("BotName", newName);
        saveConfig();
    }

    private void add_questions(String[] qas) throws IOException {
        File questionsFile = new File(getDataFolder(), "questions.txt");
        questionsFile.createNewFile();

        FileWriter writer = new FileWriter(questionsFile, true);
        BufferedWriter buff = new BufferedWriter(writer);
        for (String qa : qas) {
            if (!is_valid_question(qa)) {
                getLogger().warning(
                        String.format(
                                "Found a question/answer incorrectly formated, please correct this. (Format -> 'question: answer')\n" +
                                        "Value: %s", qa)
                );
                continue;
            }
            buff.write(qa + "\n");
        }
        buff.flush();
        buff.close();
    }

    void read_questions() throws IOException {
        File questionsFile = new File(getDataFolder(), "questions.txt");
        Stream<String> stream = Files.lines(Paths.get(questionsFile.getPath()));

        stream.forEach((line) -> {
            final Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                questions.put(matcher.group(1), matcher.group(2));
            }
        });
    }

    private boolean is_valid_question(String qa) {
        return pattern.matcher(qa).matches();
    }

    public Map<String, String> getQuestions() {
        return questions;
    }
}
