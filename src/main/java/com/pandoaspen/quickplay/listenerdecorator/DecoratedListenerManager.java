package com.pandoaspen.quickplay.listenerdecorator;

import com.pandoaspen.quickplay.exception.GameListenerRegistrationException;
import com.pandoaspen.quickplay.game.instances.GameInstance;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@RequiredArgsConstructor
public class DecoratedListenerManager {

    private final Plugin plugin;

    public void registerListeners(GameInstance gameInstance, ContextAwareListener listener) {
        Class<? extends ContextAwareListener> clazz = listener.getClass();

        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(com.pandoaspen.quickplay.listenerdecorator.GameEventHandler.class)) continue;

            com.pandoaspen.quickplay.listenerdecorator.GameEventHandler annotation = method.getAnnotation(com.pandoaspen.quickplay.listenerdecorator.GameEventHandler.class);

            if (method.getParameterCount() != 2) {
                throw new GameListenerRegistrationException(gameInstance, listener, method, "Listener methods should only have 2 parameters");
            }

            Parameter[] parameters = method.getParameters();

            if (!GameInstance.class.isAssignableFrom(parameters[0].getType())) {
                throw new GameListenerRegistrationException(gameInstance, listener, method, "A handler's first parameter should always be assignable from GameInstance");
            }


            if (!Event.class.isAssignableFrom(parameters[1].getType())) {
                throw new GameListenerRegistrationException(gameInstance, listener, method, "A handler's second parameter should always be assignable from a bukkit event");
            }

            Class<? extends Event> eventClass = (Class<? extends Event>) parameters[1].getType();

            IGameEventExecutor gameEventHandler = lookup(listener, method);
            WorldProvider worldProvider = WorldProvider.getFor(eventClass);

            DecoratedEventExecutor eventExecutor = new DecoratedEventExecutor(gameInstance, annotation.onStates(), gameEventHandler, worldProvider);
            plugin.getServer().getPluginManager().registerEvent(eventClass, gameInstance, annotation.priority(), eventExecutor, plugin);
        }
    }

    public void unregisterListeners(GameInstance gameInstance) {
        HandlerList.unregisterAll(gameInstance);
    }

    private IGameEventExecutor lookup(ContextAwareListener contextAwareListener, Method method) {

        Class<? extends ContextAwareListener> listenerClass = contextAwareListener.getClass();
        Class<? extends Event> eventClass = (Class<? extends Event>) method.getParameterTypes()[1];
        String methodName = method.getName();

        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();

            MethodType invokedType = MethodType.methodType(IGameEventExecutor.class, listenerClass);
            MethodType samMethodType = MethodType.methodType(void.class, GameInstance.class, eventClass);
            MethodHandle implMethod = lookup.findVirtual(listenerClass, methodName, MethodType.methodType(void.class, GameInstance.class, eventClass));
            MethodType instantiatedMethodType = MethodType.methodType(void.class, GameInstance.class, eventClass);

            CallSite callSite = LambdaMetafactory.metafactory(lookup, "invoke", invokedType, samMethodType, implMethod, instantiatedMethodType);

            return (IGameEventExecutor) callSite.getTarget().invoke(contextAwareListener);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }


}