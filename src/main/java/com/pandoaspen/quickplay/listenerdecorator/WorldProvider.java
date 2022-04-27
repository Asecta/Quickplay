package com.pandoaspen.quickplay.listenerdecorator;

import org.bukkit.World;
import org.bukkit.event.Event;
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

interface WorldProvider {
    World getWorld(Event event);

    static WorldProvider getFor(Class<? extends Event> eventClass) {


        if (BlockEvent.class.isAssignableFrom(eventClass)) {
            return event -> ((BlockEvent) event).getBlock().getWorld();
        }

        if (EntityEvent.class.isAssignableFrom(eventClass)) {
            return event -> ((EntityEvent) event).getEntity().getWorld();
        }

        if (HangingEvent.class.isAssignableFrom(eventClass)) {
            return event -> ((HangingEvent) event).getEntity().getWorld();
        }

        if (InventoryEvent.class.isAssignableFrom(eventClass)) {
            return event -> ((InventoryEvent) event).getInventory().getLocation().getWorld();
        }

        if (InventoryMoveItemEvent.class.isAssignableFrom(eventClass)) {
            return event -> ((InventoryMoveItemEvent) event).getInitiator().getLocation().getWorld();
        }

        if (InventoryPickupItemEvent.class.isAssignableFrom(eventClass)) {
            return event -> ((InventoryPickupItemEvent) event).getInventory().getLocation().getWorld();
        }

        if (PlayerEvent.class.isAssignableFrom(eventClass)) {
            return event -> ((PlayerEvent) event).getPlayer().getWorld();
        }

        if (PlayerLeashEntityEvent.class.isAssignableFrom(eventClass)) {
            return event -> ((PlayerLeashEntityEvent) event).getPlayer().getWorld();
        }

        if (TabCompleteEvent.class.isAssignableFrom(eventClass)) {
            return event -> ((TabCompleteEvent) event).getLocation().getWorld();
        }

        if (VehicleEvent.class.isAssignableFrom(eventClass)) {
            return event -> ((VehicleEvent) event).getVehicle().getWorld();
        }

        if (WeatherEvent.class.isAssignableFrom(eventClass)) {
            return event -> ((WeatherEvent) event).getWorld();
        }

        if (WorldEvent.class.isAssignableFrom(eventClass)) {
            return event -> ((WorldEvent) event).getWorld();
        }

        return event -> null;
    }
}
