# MC-HelperBot
Questions can use regex! Find out what regex is [here](https://www.computerhope.com/jargon/r/regex.htm).

This plugin is a simple chat bot that will answer users questions without any need of a command.
Permissions:
    helperbot.* - Gives access to all helperbot commands
    helperbot.set - Allows user to set the bot name
    helperbot.answer - The bot will answer only users with this permission.

This is how the generated config.yml file looks like:
```yml
BotName: "&6Helperbot&r:&7"
# If set to true it will ignore all "?", see the example image.
IgnoreQuestionMark: true
```

questions.txt
```
How are you: I'm fine thanks :P
How (i|to) spawn: Try /spawn
\b\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\b: Please don't share ips!
\b(?:[-A-Za-z0-9]+\.)+[A-Za-z]{2,6}\b: Please don't share domains!
```

