package io.github.vicen621.pieces.characters.ghosts;

import io.github.vicen621.pieces.characters.PacMan;

import java.awt.*;

public class Inky extends AbstractGhost {

    public Inky(GhostManager ghostManager) {
        super("/assets/characters/ghosts/inky/inky.gif", ghostManager);
    }

    @Override
    public boolean calculateTarget(GhostManager.GhostMode mode) {
        if (super.calculateTarget(mode))
            return true;

        switch (mode) {
            case CHASE -> calculateChaseTarget();
            case SCATTER -> getTarget().setLocation(new Point(0, 35));
        }

        return false;
    }

    public void calculateChaseTarget() {
        PacMan pacMan = getGhostManager().getPacMan();
        Point target = new Point(pacMan.getPos());
        Face face = pacMan.getFace();
        Point blinkyPos = getGhostManager().getBlinky().getPos();

        // Multiply the face by 2 to get the target 2 tiles in front of PacMan because one of the face values is always 0
        target.translate(face.dx * 2, face.dy * 2);

        // 2 * (Intermediate Position) - Blinky Position
        target.translate(target.x * 2, target.y * 2);
        target.translate(-blinkyPos.x, -blinkyPos.y);
        getTarget().setLocation(target);
    }
}

