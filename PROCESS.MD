# Pac-Man Project

I'm making this project only for fun and to learn more about the game development process. I'm not a professional game developer, so I'm not following any specific methodology or best practices. I'm just doing what I think is best for me and for the project. I'm also not following any specific game design document, I'm just writing down some ideas and concepts that I think are important for the game.
I'm taking inspiration in the [Minecraft Pacman video](https://youtu.be/49B7L22dxiE?si=dDXwIIaFBRCfXo22) from the YouTuber [mattbatwings](https://www.youtube.com/@mattbatwings), and the original Pac-Man video game, but I'm not following his project, I'm doing my own thing.


## First Day - 2024-02-29 (Thursday, February 29)
Today I wrote the Board and the Pacman classes. The Board class is responsible for creating the game board and the Pacman class is responsible for creating the Pacman character. I also created the main game loop and the game window.
Also, I wrote half of the walls coordinates and the Pacman movement.

## Second Day - 2024-03-02 (Saturday, March 2)
So, I modified the PacMan movement to move alone, you only change the direction. Also, I added the coins and power pellets to the game board, with that the collision detection for the coins and the score to the game window.

## Third Day - 2024-03-03 (Sunday, March 3)
Currently, I'm adding the ghosts to the game. The way it will work is that the ghosts have a target position that would be calculated every tick, and they would move to that position. I'm also adding the collision detection for the ghosts and the Pacman.

### Ghosts
There are 4 ghosts:
* Blinky - Red: The most aggressive ghost, it will always try to move to the Pacman position. Scatter: (25, 0)
* Pinky - Pink: The most strategic ghost, it will try to move to a position 4 tiles in front of the Pacman. Scatter: (2, 0)
* Inky - Cyan: The most unpredictable ghost. Get the tile 2 tiles in front of pacman, then draw a vector from blinky to that tile, and double the length of the vector. The result is the target tile. Scatter: (0, 35). Target in chase mode: 2 * (Intermediate Position) - Blinky Position
* Clyde - Yellow: The most passive ghost, it will try to move to the Pacman position, but if it gets too close, the target tile changes to the same in scatter mode. Scatter: (27, 35). If sqrDistamce > 64, target = Pacman. Else, target = Scatter.

#### Ghost modes

There are 3 modes for the ghosts:
* Scatter: The ghosts will move to their respective corners of the board.
* Chase: The ghosts will move to their respective targets.
* Frightened: The ghosts will move to a random position.

The timings for the modes are:
1. Scatter for 7 seconds, then chase for 20 seconds
2. Scatter for 7 seconds, then chase for 20 seconds
3. Scatter for 5 seconds, then chase for 20 seconds
4. Scatter for 5 seconds, then chase for permanent

The timer pauses if the Ghosts enter in Frightened mode. The timer resumes when the Ghosts leave the Frightened mode.


#### Target tile choose

The ghost will choose the target tile based on the distance from the target point.
Cant move through walls, and the target tile is the closest to the target point.

To define which tile is the closest to the target point I should use a distance squared algorithm. If two tiles ties in distance the ghost chooses by the following order: Up, Left, Down, Right.

Also, the ghosts can't move to the opposite direction of the current direction.

## Fourth day - 2024-03-05 (Tuesday, March 5)

There are a few things that I need to do in the next days
* Add the lives to the game window
* Add the game over screen
* Add the win screen
* Add the death animation for the Pacman
* See why sometimes pac-man don't eat the ghosts
* Add levels? (I don't know if I will do this)
* Add the fruits
* Add the sound effects