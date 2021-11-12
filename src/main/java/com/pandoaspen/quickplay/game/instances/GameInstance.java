package com.pandoaspen.quickplay.game.instances;

import com.pandoaspen.quickplay.QuickplayPlugin;
import com.pandoaspen.quickplay.game.gamemodes.IGamemode;
import com.pandoaspen.quickplay.game.maps.IMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;

@RequiredArgsConstructor
public class GameInstance {

    @Getter private final QuickplayPlugin plugin;
    @Getter private final IGamemode gamemode;
    @Getter private final IMap map;
    @Getter private final World world;

    @Getter private boolean running = false;

    public void tick() {

    }


    @Override
    public String toString() {
        return "GameInstance{" + "plugin=" + plugin + ", gamemode=" + gamemode + ", map=" + map + ", world=" + world + '}';
    }
}
