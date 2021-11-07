package com.pandoaspen.quickplay;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@CommandAlias("quickplay")
public class TestCommand extends BaseCommand {

    private final QuickplayPlugin plugin;

    @Subcommand("ping")
    public void cmdPing(Player sender) {
        sender.sendMessage("Pong!");
    }

}
