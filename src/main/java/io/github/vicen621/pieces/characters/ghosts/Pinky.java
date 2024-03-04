package io.github.vicen621.pieces.characters.ghosts;

import io.github.vicen621.pieces.characters.PacMan;

import java.awt.*;

public class Pinky extends AbstractGhost {

    public Pinky(GhostManager ghostManager) {
        super("/assets/characters/ghosts/pinky/pinky.gif", ghostManager);
    }

    @Override
    public boolean calculateTarget(GhostManager.GhostMode mode) {
        if (super.calculateTarget(mode))
            return true;

        switch (mode) {
            case CHASE -> calculateChaseTarget();
            case SCATTER -> getTarget().setLocation(new Point(2, 0));
        }

        return false;
    }

    public void calculateChaseTarget() {
        PacMan pacMan = getGhostManager().getPacMan();
        Point target = new Point(pacMan.getPos());
        Face face = pacMan.getFace();

        // Multiply the face by 4 to get the target 4 tiles in front of PacMan because one of the face values is always 0
        target.translate(face.dx * 4, face.dy * 4);
        getTarget().setLocation(target);
    }
}

