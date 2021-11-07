package com.pandoaspen.quickplay;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

@RequiredArgsConstructor
public class TestListener implements Listener {

    private final QuickplayPlugin plugin;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.getPlayer().sendMessage("You broke a block!");
    }

}
