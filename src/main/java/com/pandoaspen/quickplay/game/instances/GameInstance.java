package com.pandoaspen.quickplay.game.instances;

import com.pandoaspen.quickplay.QuickplayPlugin;
import com.pandoaspen.quickplay.game.gamemodes.IGamemode;
import com.pandoaspen.quickplay.game.maps.IMap;
import com.pandoaspen.quickplay.listenerdecorator.ContextAwareListener;
import lombok.*;
import org.bukkit.World;

import java.util.UUID;

@RequiredArgsConstructor
public class GameInstance implements ContextAwareListener {

    @Getter private final QuickplayPlugin plugin;
    @Getter private final IGamemode gamemode;
    @Getter private final IMap map;
    @Getter private final World world;

    @Getter private final UUID uuid = UUID.randomUUID();

    @Setter
    @Getter
    private boolean running = false;

    public void tick() {

    }

    @Override
    public String toString() {
        return "GameInstance{uuid=" + uuid + "}";
    }
}
