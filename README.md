# MC-HelperBot
Questions can use regex! Find out what regex is [here](https://www.computerhope.com/jargon/r/regex.htm).

This plugin is a simple chat bot that will answer users questions without any need of a command.

Permissions:
* helperbot.* - Gives access to all helperbot commands
* helperbot.set - Allows user to set the bot name
* helperbot.answer - The bot will answer only users with this permission.

## Generated config.yaml
```yml
BotName: "&6Helperbot&r:&7"
```

## Generated questions.yaml
```yaml
questions:
 -
    question: 'How (i|to) spawn'
    answer: Try /spawn
    # Cooldown is in seconds
    cooldown: 5
    # If broadcast is true, the cooldown is global, if not, its per-player
    # Broadcast true = all players see the bot message.
    broadcast: yes
    # You can add this on any question to disable it without removing it from the file.
    # disable: yes
  -
    question: 'How are you'
    answer: I'm fine thanks :P
    cooldown: 5
    broadcast: yes
  -
    # When using regex, it's recommended to use single quotes in yaml to avoid escaping.
    question: '\b\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\b'
    answer: Please don't share ips!
    cooldown: 5
    broadcast: yes
  -
    # Remember, to escape a ' you need to repeat it like: ''
    # https://github.com/dotmaster/toYaml/issues/1
    question: '(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]'
    answer: Please don't share urls!
    cooldown: 5
    broadcast: yes
```

