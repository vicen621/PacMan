package io.github.vicen621.pieces.characters.ghosts;

import io.github.vicen621.pieces.characters.PacMan;

import java.awt.*;

public class Clyde extends AbstractGhost {

    public Clyde(GhostManager ghostManager) {
        super("/assets/characters/ghosts/clyde/clyde.gif", ghostManager, new Point(15, 17), 6);
    }

    @Override
    public boolean calculateTarget(GhostManager.GhostMode mode) {
        if (super.calculateTarget(mode))
            return true;

        switch (mode) {
            case CHASE -> calculateChaseTarget();
            case SCATTER -> getTarget().setLocation(new Point(27, 35));
        }

        return false;
    }

    public void calculateChaseTarget() {
        PacMan pacMan = getGhostManager().getPacMan();
        Point target = new Point(pacMan.getPos());

        if (pacMan.getPos().distanceSq(getPos()) < 64) {
            getTarget().setLocation(new Point(27, 35));
        } else {
            getTarget().setLocation(target);
        }
    }
}

