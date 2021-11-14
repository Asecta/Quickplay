package com.pandoaspen.quickplay.game.gamemodes;

import com.pandoaspen.quickplay.QuickplayPlugin;
import com.pandoaspen.quickplay.game.GameManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public abstract class AbstractGamemode implements IGamemode {

    @Getter private final QuickplayPlugin plugin;
    @Getter private final GameManager gameManager;

    @Getter private String name;

    @Getter
    @Setter
    private GameState state = GameState.PREGAME;

    @Override
    public void tick() {
        switch (state) {
            case PREGAME -> tickPreGame();
            case INGAME -> tickInGame();
            case POSTGAME -> tickPostGame();
        }
    }


    protected abstract void tickPreGame();

    protected abstract void tickInGame();

    protected abstract void tickPostGame();

}
