package com.pandoaspen.quickplay.listenerdecorator;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.pandoaspen.quickplay.QuickplayPlugin;
import com.pandoaspen.quickplay.game.GameManager;
import com.pandoaspen.quickplay.game.gamemodes.AbstractGamemode;
import com.pandoaspen.quickplay.game.gamemodes.GameState;
import com.pandoaspen.quickplay.game.gamemodes.IGamemode;
import com.pandoaspen.quickplay.game.instances.GameInstance;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

public class ListenerRegisterTest {

    private static ServerMock server;
    private static QuickplayPlugin plugin;

    public ListenerRegisterTest() {
        this.server = MockBukkit.getOrCreateMock();
        this.plugin = MockBukkit.load(QuickplayPlugin.class);

        this.server.getLogger().setLevel(Level.ALL);
        this.plugin.getLogger().setLevel(Level.ALL);
    }


    public static class TestListener implements ContextAwareListener {

        @GameEventHandler(priority = EventPriority.NORMAL, onStates = {GameState.INGAME})
        public void onBreak(GameInstance ga, BlockBreakEvent event) {
            plugin.getLogger().info("Block Broken!");
        }

    }

    public static class MockGamemode extends AbstractGamemode {

        public MockGamemode(QuickplayPlugin plugin, GameManager gameManager) {
            super(plugin, gameManager);
        }

        @Override
        protected void tickPreGame() {}

        @Override
        protected void tickInGame() {}

        @Override
        protected void tickPostGame() {}
    }


    @Test
    void registerListeners() {
        server.addSimpleWorld("World");
        World world = server.addSimpleWorld("TestWorld");

        IGamemode gameMode = new MockGamemode(plugin, new GameManager());
        gameMode.setState(GameState.INGAME);

        GameInstance gameInstance = new GameInstance(plugin, gameMode, null, world);

        gameInstance.setRunning(true);

        DecoratedListenerManager listenerManager = new DecoratedListenerManager(plugin);
        listenerManager.registerListeners(gameInstance, new TestListener());

        PlayerMock playerMock = server.addPlayer("Jack");
        playerMock.teleport(new Location(world, 0, 5, 0));

        Block block = new Location(world, 1, 4, 0).getBlock();
        playerMock.simulateBlockBreak(block);


    }

    @Test
    void unregisterListeners() {


    }
}