package com.pandoaspen.quickplay.game.gamemodes;

public interface IGamemode {
    String getName();

    GameState getState();
    void setState(GameState gameState);

    void tick();
}
