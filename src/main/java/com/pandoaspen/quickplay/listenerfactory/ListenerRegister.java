package com.pandoaspen.quickplay.listenerfactory;

import com.pandoaspen.quickplay.exception.GameListenerRegistrationException;
import com.pandoaspen.quickplay.game.gamemodes.GameState;
import com.pandoaspen.quickplay.game.instances.GameInstance;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.hanging.HangingEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.event.weather.WeatherEvent;
import org.bukkit.event.world.WorldEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@RequiredArgsConstructor
public class ListenerRegister {

    private final PluginManager pluginManager;
    private final Plugin plugin;

    public void registerListeners(GameInstance gameInstance, ContextAwareListener listener) {
        Class<? extends ContextAwareListener> clazz = listener.getClass();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(com.pandoaspen.quickplay.listenerfactory.GameEventHandler.class)) continue;

            com.pandoaspen.quickplay.listenerfactory.GameEventHandler annotation = method.getAnnotation(com.pandoaspen.quickplay.listenerfactory.GameEventHandler.class);

            if (method.getParameterCount() != 2) {
                throw new GameListenerRegistrationException(gameInstance, listener, method, "Listener methods should only have 2 parameters");
            }

            Parameter[] parameters = method.getParameters();

            if (!parameters[0].getType().isAssignableFrom(GameInstance.class)) {
                throw new GameListenerRegistrationException(gameInstance, listener, method, "A handler's first parameter should always be assignable from GameInstance");
            }

            if (!parameters[1].getType().isAssignableFrom(Event.class)) {
                throw new GameListenerRegistrationException(gameInstance, listener, method, "A handler's second parameter should always be assignable from a bukkit event");
            }

            Class<? extends Event> eventClass = (Class<? extends Event>) parameters[1].getType();

            IGameEventExecutor gameEventHandler = lookup(listener, method);
            WorldProvider worldProvider = WorldProvider.getFor(eventClass);

            WrappedGameEventHandler wrappedHandler = new WrappedGameEventHandler(gameInstance, annotation.onStates(), gameEventHandler, worldProvider);
            pluginManager.registerEvent(eventClass, gameInstance, annotation.priority(), wrappedHandler::invoke, plugin);
        }
    }

    public void unregisterListeners(GameInstance gameInstance) {
        HandlerList.unregisterAll(gameInstance);
    }

    private IGameEventExecutor lookup(ContextAwareListener contextAwareListener, Method method) {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodType invokedType = MethodType.methodType(IGameEventExecutor.class, ContextAwareListener.class);
            MethodType samMethodType = MethodType.methodType(void.class, GameInstance.class, Event.class);
            MethodHandle implMethod = lookup.findVirtual(ContextAwareListener.class, method.getName(), MethodType.methodType(void.class, GameInstance.class, Event.class));
            MethodType instantiatedMethodType = MethodType.methodType(void.class, GameInstance.class, Event.class);

            CallSite callSite = LambdaMetafactory.metafactory(lookup, "invoke", invokedType, samMethodType, implMethod, instantiatedMethodType);

            return (IGameEventExecutor) callSite.getTarget().invoke(contextAwareListener);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }


    public interface WorldProvider {
        World getWorld(Event event);

        static WorldProvider getFor(Class<? extends Event> eventClass) {
            if (eventClass.isAssignableFrom(BlockEvent.class)) {
                return event -> ((BlockEvent) event).getBlock().getWorld();
            }

            if (eventClass.isAssignableFrom(EntityEvent.class)) {
                return event -> ((EntityEvent) event).getEntity().getWorld();
            }

            if (eventClass.isAssignableFrom(HangingEvent.class)) {
                return event -> ((HangingEvent) event).getEntity().getWorld();
            }

            if (eventClass.isAssignableFrom(InventoryEvent.class)) {
                return event -> ((InventoryEvent) event).getInventory().getLocation().getWorld();
            }

            if (eventClass.isAssignableFrom(InventoryMoveItemEvent.class)) {
                return event -> ((InventoryMoveItemEvent) event).getInitiator().getLocation().getWorld();
            }

            if (eventClass.isAssignableFrom(InventoryPickupItemEvent.class)) {
                return event -> ((InventoryPickupItemEvent) event).getInventory().getLocation().getWorld();
            }

            if (eventClass.isAssignableFrom(PlayerEvent.class)) {
                return event -> ((PlayerEvent) event).getPlayer().getWorld();
            }

            if (eventClass.isAssignableFrom(PlayerLeashEntityEvent.class)) {
                return event -> ((PlayerLeashEntityEvent) event).getPlayer().getWorld();
            }

            if (eventClass.isAssignableFrom(TabCompleteEvent.class)) {
                return event -> ((TabCompleteEvent) event).getLocation().getWorld();
            }

            if (eventClass.isAssignableFrom(VehicleEvent.class)) {
                return event -> ((VehicleEvent) event).getVehicle().getWorld();
            }

            if (eventClass.isAssignableFrom(WeatherEvent.class)) {
                return event -> ((WeatherEvent) event).getWorld();
            }

            if (eventClass.isAssignableFrom(WorldEvent.class)) {
                return event -> ((WorldEvent) event).getWorld();
            }

            return event -> null;
        }
    }

    public interface IGameEventExecutor {
        void invoke(GameInstance gameInstance, Event event);
    }

    @RequiredArgsConstructor
    public class WrappedGameEventHandler {

        private final GameInstance gameInstance;
        private final GameState[] gameStates;
        private final IGameEventExecutor gameEventExecutor;
        private final WorldProvider worldProvider;


        public void invoke(Listener listener, Event event) {
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
        }
    }
}