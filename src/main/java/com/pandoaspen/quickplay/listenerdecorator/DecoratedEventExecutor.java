package com.pandoaspen.quickplay.listenerdecorator;

import com.pandoaspen.quickplay.game.gamemodes.GameState;
import com.pandoaspen.quickplay.game.instances.GameInstance;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
class DecoratedEventExecutor implements EventExecutor {

    private final GameInstance gameInstance;
    private final GameState[] gameStates;
    private final IGameEventExecutor gameEventExecutor;
    private final WorldProvider worldProvider;

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) {
        try {
            World world = worldProvider.getWorld(event);

            if (world != null && world != gameInstance.getWorld()) {
                return;
            }

            GameState gameState = gameInstance.getGamemode().getState();

            isAcceptable:
            {
                for (GameState state : gameStates) {
                    if (state == gameState) {
                        break isAcceptable;
                    }
                }
                return;
            }
            gameEventExecutor.invoke(gameInstance, event);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}