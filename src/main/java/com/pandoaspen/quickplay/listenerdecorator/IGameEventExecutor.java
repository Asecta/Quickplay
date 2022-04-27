package com.pandoaspen.quickplay.listenerdecorator;

import com.pandoaspen.quickplay.game.instances.GameInstance;
import org.bukkit.event.Event;

interface IGameEventExecutor<T extends Event> {
    void invoke(GameInstance gameInstance, T event);
}
