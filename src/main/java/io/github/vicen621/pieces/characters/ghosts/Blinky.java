package io.github.vicen621.pieces.characters.ghosts;

import java.awt.*;

public class Blinky extends AbstractGhost {

    public Blinky(GhostManager ghostManager) {
        super("/assets/characters/ghosts/blinky/blinky.gif", ghostManager);
    }

    @Override
    public boolean calculateTarget(GhostManager.GhostMode mode) {
        if (super.calculateTarget(mode))
            return true;

        switch (mode) {
            case CHASE -> getTarget().setLocation(getGhostManager().getPacMan().getPos());
            case SCATTER -> getTarget().setLocation(new Point(25, 0));
        }

        return false;
    }
}
