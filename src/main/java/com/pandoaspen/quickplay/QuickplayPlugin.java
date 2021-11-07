package com.pandoaspen.quickplay;

import co.aikar.commands.CommandManager;
import co.aikar.commands.PaperCommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public class QuickplayPlugin extends JavaPlugin {

    private CommandManager commandManager;

    @Override
    public void onEnable() {
        this.setupCommands();
        this.setupListeners();
    }

    private void setupCommands() {
        this.commandManager = new PaperCommandManager(this);
        this.commandManager.registerCommand(new TestCommand(this));
    }

    private void setupListeners() {
        getServer().getPluginManager().registerEvents(new TestListener(this), this);
    }
}