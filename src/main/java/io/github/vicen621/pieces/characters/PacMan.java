package io.github.vicen621.pieces.characters;

import io.github.vicen621.board.Board;
import io.github.vicen621.pieces.FacedPiece;
import io.github.vicen621.pieces.Tickable;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PacMan extends FacedPiece implements Tickable {
    // Move speed calculation: 1 tile per (MOVE_SPEED * Board.DELAY)ms
    private static final int MOVE_SPEED = 5;
    private Face nextMove;
    private int score;
    private int moveCounter;
    private int lives;

    public PacMan() {
        super("/assets/characters/pacman/pacman.gif");

        this.nextMove = getFace();
        this.score = 0;
        this.moveCounter = 0;
        this.lives = 2;
        getPos().setLocation(Board.STARTING_POINT);
    }

    public void keyPressed(KeyEvent e) {
        // every keyboard get has a certain code. get the value of that code from the
        // keyboard event so that we can compare it to KeyEvent constants
        int key = e.getKeyCode();

        // depending on which arrow key was pressed, we're going to move the player by
        // one whole tile for this input
        switch (key) {
            case KeyEvent.VK_UP -> nextMove = Face.UP;
            case KeyEvent.VK_RIGHT -> nextMove = Face.RIGHT;
            case KeyEvent.VK_DOWN -> nextMove = Face.DOWN;
            case KeyEvent.VK_LEFT -> nextMove = Face.LEFT;
        }
    }

    @Override
    public void tick() {
        // this gets called once every tick, before the repainting process happens.
        // so we can do anything needed in here to update the state of the player.

        this.moveCounter += 1;
        if (this.moveCounter < MOVE_SPEED)
            return;

        this.moveCounter = 0;

        Point pos = new Point(getPos());
        getPos().translate(nextMove.dx, nextMove.dy);

        if (!Board.checkCollision(getPos()))
            setFace(nextMove);
        else {
            getPos().setLocation(pos);
            getPos().translate(getFace().dx, getFace().dy);
        }

        if (getPos().y == Board.TUNNEL_ROW) {
            if (getPos().x < 0) {
                getPos().x = Board.COLUMNS - 1;
            } else if (getPos().x >= Board.COLUMNS) {
                getPos().x = 0;
            }
        }

        if (Board.checkCollision(getPos()))
            getPos().setLocation(pos);
    }

    public void kill() {
        if (lives == 0) {
            System.out.println("You lost!");
            System.exit(0);
            return;
        }

        lives -= 1;
        reset();
    }

    private void reset() {
        getPos().setLocation(Board.STARTING_POINT);
        setFace(Face.UP);
        nextMove = getFace();
        moveCounter = 0;
    }

    public String getScore() {
        return String.valueOf(score);
    }

    public void addScore(int amount) {
        score += amount;
    }
}
