package com.pandoaspen.quickplay.listenerfactory;

import com.pandoaspen.quickplay.exception.GameListenerRegistrationException;
import com.pandoaspen.quickplay.game.instances.GameInstance;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ListenerFactory {

    private final PluginManager pluginManager;

    public void registerListeners(GameInstance gameInstance, ContextAwareListener listener) {
        Class<? extends ContextAwareListener> clazz = listener.getClass();

        List<IGameEventExecutor> handlerList = new ArrayList<>();

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

            IGameEventExecutor gameEventHandler;
            try {
                gameEventHandler = lookup(listener, method);
            } catch (Throwable e) {
                e.printStackTrace();
            }


        }
    }


    private IGameEventExecutor lookup(ContextAwareListener contextAwareListener, Method method) throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        CallSite callSite = LambdaMetafactory.metafactory(lookup, "invoke",
                MethodType.methodType(IGameEventExecutor.class, ContextAwareListener.class),
                MethodType.methodType(void.class, GameInstance.class, Event.class),
                lookup.findVirtual(ContextAwareListener.class, method.getName(), MethodType.methodType(void.class, GameInstance.class, Event.class)),
                MethodType.methodType(void.class, GameInstance.class, Event.class)
        );

        return (IGameEventExecutor) callSite.getTarget().invoke(contextAwareListener);
    }


    @RequiredArgsConstructor
    class WrappedGameEventHandler<T extends Event> {
        private final ContextAwareListener contextAwareListener;
        private final GameEventHandler gameEventHandlerAnnotation;
        private final IGameEventExecutor gameEventExecutor;
        private final Class<T> eventClass;

        public void invoke(GameInstance gameInstance, T event) {


            gameEventExecutor.invoke(gameInstance, event);


        }
    }

    public interface WorldProvider {

    }


    public interface IGameEventExecutor {
        void invoke(GameInstance gameInstance, Event event);
    }
}
