package com.pandoaspen.quickplay.exception;

import com.pandoaspen.quickplay.game.instances.GameInstance;
import com.pandoaspen.quickplay.listenerdecorator.ContextAwareListener;

public class GameListenerException extends RuntimeException {
    public GameListenerException(GameInstance gameInstance, ContextAwareListener listener, String message) {
        super(String.format("[%s || %s] -- %s", gameInstance.toString(), String.format(listener.getClass().getSimpleName()), message));
    }
}
