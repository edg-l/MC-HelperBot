package com.ryozuki.helperbot;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class Main extends JavaPlugin {
    private FileConfiguration config = null;
    private Map<String, String> QA = null;
    private File questionsFile = null;
    private Path questionAnswerFilePath = null;
    private final String ENDLINE = "\r\n";

    @Override
    public void onEnable() {
        super.onEnable();

        this.config = this.getConfig();
        config.addDefault("BotName", "<HelperBot>:");
        config.addDefault("IgnoreQuestionMark", true);
        config.addDefault("BotColor", "&3");

        saveConfig();

        questionAnswerFilePath = Paths.get(this.getConfig().getCurrentPath(), "plugins", this.getName(), "questions.txt");
        questionsFile = questionAnswerFilePath.toFile();
        if(!questionsFile.exists())
        {
            try {
                questionsFile.createNewFile();
                String[] qas = {
                        "How are you: I'm fine thanks :P",
                        "How (i|to) spawn: Try /spawn",
                        "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b: Please don't share ips!",
                        "\\b(?:[-A-Za-z0-9]+\\.)+[A-Za-z]{2,6}\\b: Please don't share domains!"};
                WriteQA(qas);
            }
            catch (IOException e) {
                getLogger().warning(e.toString());
            }
        }

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

    String getColor() {
        return config.getString("BotColor");
    }

    boolean getIgnore() {
        return  config.getBoolean("IgnoreQuestionMark");
    }

    String getBotName() {
        return config.getString("BotName");
    }

    void setBotName(String newName) {
        config.set("BotName", newName + " ");
        saveConfig();
    }

    void WriteQA(String[] qas) throws IOException {
        if(!questionsFile.exists())
            return;

        FileWriter writer = new FileWriter(questionsFile.getPath(), true);
        BufferedWriter buff = new BufferedWriter(writer);
        for (String qa: qas) {
            buff.write(qa+ENDLINE);
        }
        buff.flush();
        buff.close();
    }

    Map<String, String> ReadQA() throws IOException {
        final String patternStr = "(.+)[:](.+)";
        final Pattern pattern = Pattern.compile(patternStr);
        Map<String, String> questionAnswers = new HashMap<>();
        Stream<String> stream = Files.lines(Paths.get(questionsFile.getPath()));

        stream.forEach((line) -> {
            final Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                questionAnswers.put(matcher.group(1), matcher.group(2));
            }
        });
        return questionAnswers;
    }

    void setConfig(FileConfiguration config) {
        this.config = config;
    }


}