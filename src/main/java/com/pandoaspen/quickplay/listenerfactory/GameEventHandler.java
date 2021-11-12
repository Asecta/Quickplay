package com.pandoaspen.quickplay.listenerfactory;

import com.pandoaspen.quickplay.game.gamemodes.GameState;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GameEventHandler {
    int priority();

    GameState[] onStates();
}