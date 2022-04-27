package com.pandoaspen.quickplay;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class QuickplayPlugin extends JavaPlugin {

    public QuickplayPlugin() {
    }

    protected QuickplayPlugin(@NotNull final JavaPluginLoader loader, @NotNull final PluginDescriptionFile description, @NotNull final File dataFolder, @NotNull final File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onEnable() {
        this.setupCommands();
        this.setupListeners();
    }

    private void setupCommands() {
        getLogger().info("Setting up commands...");
    }

    private void setupListeners() {

    }
}
