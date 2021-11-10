package com.pandoaspen.quickplay.game.gamemodes;

import lombok.Getter;

public class AbstractGamemode implements IGamemode {

    @Getter private String name;
    @Getter private GameState state;

    @Override
    public void tick() {

    }
}
