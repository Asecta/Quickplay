# Quickplay

Quickplay is a simple but general platform for managing multi-instance (and potentially multi-server) PvP games. 

# Scope

## **Game instances**

**A game instance** is a combination of a map and gamemode (game logic) that players participate in. Any number of game instances should run on a server at a given time, even if the gamemode is currently being run on the given map. 

*   A game instance will create a copy of the provided map and logic, which can be joined by players.
*   Each game instance will take place in its own world in order to simplify game logic.
*   Once a game instance ends, the game may save scores or stats for future use

Game instances will be defined in a config file as the gamemode, the Map and additional modifiers. This includes: What game mechanics will be enabled or disabled, Configuration overrides for any given game mechanics, etc

Excluding listeners, all gamemode logic and game mechanics will be ticked from the instances game loop. This would looks something like so:

```yaml
tick-instance:
    tick-gamemechanics: 
        tick-teams
        tick-flags
        tick-chests
    tick-gamemode:
        tick-states:
        tick-wincondition
        etc..
    etc..
```

We can consider how they will be orchestrated later.

## Gamemodes

A gamemode is the game logic behind a given game instance. For example King of the hill, One in the chamber, Capture the flag, etc. 

**States:** A game instance will be stateful, consisting of 3 primary states:

*   Pregame: Theperiod of time before the game starts where the player may inspect the map, pick a team, pick a kit, etc.
*   Ingame: When the game is currently being played, and all the major logic is being applied.
*   Postgame: The end of the game, where statistics may be announced and saved, and the next game instance may be voted on by players.

A gamemode may add additional states or sub-states to the base states if they wish. For example, there may be a gamemode that allows a 1 minute grace period before players can begin fighting. 

**Game mechanics:** A gamemode may be provided modifiers/mechanics that will be used to change the game logic in some way. The possible modifiers and their constraints will be defined in the game logic. For example:

*   The number of teams that can participate in the game
*   What blocks can be broken or placed
*   The kits a player starts with, or that the player can pick from

**Requirements:** A gamemode will have a set of requirements that a map must fulfil to be able to run a given game instance with the given modifiers: For example:

*   At least 4 spawn points are required for a 4 team game
*   A flag for each time is required for a game of capture the flag.
*   A minimum number of players may be required

Gamemodes will implement a common abstract class that provides a general outline and logic template for the game.

## Maps

A map is self-explanatory, It is a stage on which a game will run. A map must fulfil the requirements for a given game in order to be able to run that game. A map must be reusable. 

Maps should be mostly defined through in-game features, such that a person running singleplayer vanilla could still create a map. I suggest using named armourstands and/or structure blocks to define where a spawn point is, or where a flag is. The gamemode will use these flags to determine if the map meets its requirements, and may also use them to make decisions about things such as team colours, flag colours etc. 

Maps will implement **game mechanics.** These will be mechanics required by the map, but may also be additional reusable mechanics at the discretion of the map builders.   

## Game Mechanics

Game mechanics are reusable components used to construct the game logic and maps.

*   Game mechanics should be written in an abstract way such they can be reused if needed
*   Game mechanics may be intended to be unique to a specific gamemode (flags in capture the flag, the core in destroy the core)
*   Game mechanics may be used as a requirement for a gamemode. See the above example.

A game mechanic may be a large or tiny inclusion to a given game, ranging from the team system, too a jump pad. Some examples:

*   The team system
*   Chests containing random loot
*   Launchpads (a block that the player can stand on that will launch them in a direction)
*   Zones that give given requirements in order to enter. For example, 'x' team cannot enter 'y' zone.

Game mechanics may be "ticked" on a frequency and may implement listeners. 

Game mechanics may require configuration input in order to be used. For example: Number of teams, the strength of a launch pad, chest loot etc. 

The game mechanic must be able to query the game instance through a given interface. Game mechanics may also be controllable from the instance loop, and may be queryable from the instance loop. 

We will provide common interfaces that can be implemented by game mechanics to make it easier and clearer to query them about their state. For example:

*   ICompletable: returns true or false of the requirements of the mechanic have been fulfilled
*   IStateful: the mechanic has a state that can be queried and/or changed.

## Building

We're using Java 16, and [Gradle](https://gradle.org/) 7.1 as the build system. Running the following will build the project and run the unit tests. 

```shell
./gradlew clean build
```
