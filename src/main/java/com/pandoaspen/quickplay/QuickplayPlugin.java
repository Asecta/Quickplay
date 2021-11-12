package com.pandoaspen.quickplay;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class QuickplayPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        this.setupCommands();
        this.setupListeners();
    }

    private void setupCommands() {

    }

    private void setupListeners() {
        getServer().getPluginManager().registerEvents(new TestListener(this), this);
    }
}
