package io.github.vicen621.pieces.characters.ghosts;

import io.github.vicen621.board.Board;
import io.github.vicen621.pieces.characters.PacMan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.util.Arrays;

public class GhostManager implements ActionListener {
    private final Timer ghostTimer;
    private final Timer energizerTimer;
    private final AbstractGhost[] ghosts;
    private final PacMan pacMan;
    private GhostMode mode;
    private GhostMode modeBeforeFrightened;
    private int modeCounter;
    private int waveCounter;
    private int frightenedCounter;
    private int ghostEatCounter;

    public GhostManager(PacMan pacMan) {
        this.pacMan = pacMan;
        this.mode = GhostMode.SCATTER;
        this.ghosts = new AbstractGhost[4];

        this.ghosts[0] = new Blinky(this);
        this.ghosts[1] = new Pinky(this);
        this.ghosts[2] = new Inky(this);
        this.ghosts[3] = new Clyde(this);

        this.modeCounter = 0;
        this.waveCounter = 0;
        this.frightenedCounter = 0;
        this.ghostEatCounter = 0;

        this.ghostTimer = new Timer(1000, this);
        this.energizerTimer = new Timer(1000, this);
        this.ghostTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(energizerTimer)) {
            if (frightenedCounter < Board.ENERGIZER_DURATION) {
                frightenedCounter++;
                return;
            }

            setMode(modeBeforeFrightened);
            this.frightenedCounter = 0;
            this.ghostEatCounter = 0;
            Arrays.stream(this.ghosts).forEach(AbstractGhost::resetEaten);
            this.energizerTimer.stop();
            return;
        }

        if (waveCounter == 4)
            this.ghostTimer.stop();

        modeCounter++;
        switch (waveCounter) {
            case 0, 1 -> {
                if (modeCounter == 7)
                    setMode(GhostMode.CHASE);
                if (modeCounter == 27) {
                    setMode(GhostMode.SCATTER);
                    modeCounter = 0;
                    waveCounter++;
                }
            }
            case 2, 3 -> {
                if (modeCounter == 5)
                    setMode(GhostMode.CHASE);
                if (modeCounter == 25) {
                    setMode(GhostMode.SCATTER);
                    modeCounter = 0;
                    waveCounter++;
                }
            }
        }
    }

    public void drawGhosts(Graphics g, ImageObserver observer) {
        Arrays.stream(ghosts).forEach(ghost -> ghost.draw(g, observer));
    }

    public void tickGhosts() {
        Arrays.stream(ghosts).forEach(AbstractGhost::tick);
    }

    public void setMode(GhostMode mode) {
        if (mode == GhostMode.FRIGHTENED) {
            this.modeBeforeFrightened = this.mode;
            this.energizerTimer.start();
            this.ghostTimer.stop(); // stop the timer if entering frightened mode
        } else if (this.mode == GhostMode.FRIGHTENED) {
            this.energizerTimer.stop();
            this.ghostTimer.start(); // only start the timer if leaving frightened mode
        }

        this.mode = mode;
    }

    public void eatGhosts() {
        if (getMode() != GhostMode.FRIGHTENED)
            return;

        for (AbstractGhost ghost : ghosts) {
            if (ghost.getPos().equals(pacMan.getPos()) && ghost.isAlive() && !ghost.isEaten()) {
                ghost.eat();
                pacMan.addScore((int) (Math.pow(2, ghostEatCounter + 1) * 100));
            }
        }
    }

    public void resetGhosts() {
        this.mode = GhostMode.SCATTER;
        this.modeCounter = 0;
        this.waveCounter = 0;
        this.frightenedCounter = 0;
        this.ghostEatCounter = 0;

        Arrays.stream(this.ghosts).forEach(AbstractGhost::reset);
        this.energizerTimer.stop();
        this.ghostTimer.stop();

        this.ghostTimer.start();
    }

    // Blinky no me mata
    public boolean checkDeath() {
        for (AbstractGhost ghost : ghosts) {
            if (ghost.getPos().equals(pacMan.getPos()) && ghost.isAlive())
                return true;
        }

        return false;
    }

    public Blinky getBlinky() {
        return (Blinky) ghosts[0];
    }

    public Pinky getPinky() {
        return (Pinky) ghosts[1];
    }

    public Inky getInky() {
        return (Inky) ghosts[2];
    }

    public Clyde getClyde() {
        return (Clyde) ghosts[3];
    }

    public GhostMode getMode() {
        return mode;
    }

    public PacMan getPacMan() {
        return pacMan;
    }

    public int getFrightenedCounter() {
        return frightenedCounter;
    }

    public enum GhostMode {
        CHASE, SCATTER, FRIGHTENED
    }
}
