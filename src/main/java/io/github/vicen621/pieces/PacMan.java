package io.github.vicen621.pieces;

import io.github.vicen621.board.Board;
import io.github.vicen621.board.Wall;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PacMan extends AbstractTickablePiece {
    private int score;

    public PacMan() {
        super("/assets/characters/pacman.png");

        this.score = 0;
    }

    public void keyPressed(KeyEvent e) {
        // every keyboard get has a certain code. get the value of that code from the
        // keyboard event so that we can compare it to KeyEvent constants
        int key = e.getKeyCode();
        Point pos = (Point) getPos().clone();

        // depending on which arrow key was pressed, we're going to move the player by
        // one whole tile for this input
        if (key == KeyEvent.VK_UP) {
            getPos().translate(0, -1);
        }
        if (key == KeyEvent.VK_RIGHT) {
            getPos().translate(1, 0);
        }
        if (key == KeyEvent.VK_DOWN) {
            getPos().translate(0, 1);
        }
        if (key == KeyEvent.VK_LEFT) {
            getPos().translate(-1, 0);
        }

        // If the player moves towards a wall, return to previous position
        for (Wall wall : Board.WALLS) {
            if (wall.isInBounds(getPos())) {
                getPos().setLocation(pos);
            }
        }
    }

    @Override
    public void tick() {
        // this gets called once every tick, before the repainting process happens.
        // so we can do anything needed in here to update the state of the player.

        // prevent the player from moving off the edge of the board vertically
        if (getPos().y <= 3) {
            getPos().y = 4;
        } else if (getPos().y >= Board.ROWS - 3) {
            getPos().y = Board.ROWS - 4;
        }

        if (getPos().y == Board.TUNNEL_ROW) {
            if (getPos().x < 0) {
                getPos().x = Board.COLUMNS - 1;
            } else if (getPos().x >= Board.COLUMNS) {
                getPos().x = 0;
            }
        } else {
            // prevent the player from moving off the edge of the board sideways
            if (getPos().x <= 1) {
                getPos().x = 1;
            } else if (getPos().x >= Board.COLUMNS - 1) {
                getPos().x = Board.COLUMNS - 2;
            }
        }
    }

    public String getScore() {
        return String.valueOf(score);
    }

    public void addScore(int amount) {
        score += amount;
    }
}
