# Quickplay

Quickplay is a simple but general platform for managing multi-instance (and potentially multi-server) PvP games. 

## Scope

### Game instances

**A game instance** is a combination of a map and gamemode (game logic) that players participate in. Any number of game instances should run on a server at a given time, even if the gamemode is currently being run on the given map. 

*   A game instance will create a copy of the provided map and logic, which can be joined by players.  
*   Each game instance will take place in its own world in order to simplify game logic.
*   Once a game instance ends, the game may save scores or stats for future use

Game instances will be defined in a config file as pairs of maps and gamemodes, and any other modifiers that should be applied. We can consider how they will be orchestrated later.

### Gamemodes

A gamemode is the game logic behind a given game instance. For example King of the hill, One in the chamber, Capture the flag, etc. 

**States:** A game instance will be stateful, consisting of 3 primary states: 

*   Pregame: The period of time before the game starts where the player may inspect the map, pick a team, pick a kit, etc.
*   Ingame: When the game is currently being played, and all the major logic is being applied. 
*   Postgame: The end of the game, where statistics may be announced and saved, and the next game instance may be voted on by players.

A gamemode may add additional states or sub-states to the base states if they wish. For example, there may be a gamemode that allows a 1 minute grace period before players can begin fighting. 

**Modifiers:** A gamemode may be provided modifiers that will be used to change the game logic in some way. The possible modifiers and their constraints will be defined in the game logic. For example: 

*   The number of teams that can participate in the game
*   What blocks can be broken or placed
*   The kits a player starts with, or that the player can pick from

**Requirements:** A gamemode will have a set of requirements that a map must fulfil to be able to run a given game instance with the given modifiers: For example: 

*   At least 4 spawn points are required for a 4 team game
*   A flag for each time is required for a game of capture the flag.  

## Building

We're using Java 16, and [Gradle](https://gradle.org/) 7.1 as the build system. Running the following will build the project and run the unit tests. 

```shell
./gradlew clean build
```
