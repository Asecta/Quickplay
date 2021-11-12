package com.pandoaspen.quickplay.exception;

import com.pandoaspen.quickplay.game.instances.GameInstance;
import com.pandoaspen.quickplay.listenerfactory.ContextAwareListener;

import java.lang.reflect.Method;

public class GameListenerRegistrationException extends GameListenerException {
    public GameListenerRegistrationException(GameInstance gameInstance, ContextAwareListener listener, String message) {
        super(gameInstance, listener, message);
    }

    public GameListenerRegistrationException(GameInstance gameInstance, ContextAwareListener listener, Method method, String message) {
        super(gameInstance, listener, String.format("Unable to register method %s. %s", method.getName(), method));
    }
}
