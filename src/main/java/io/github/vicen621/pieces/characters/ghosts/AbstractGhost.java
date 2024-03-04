package io.github.vicen621.pieces.characters.ghosts;

import io.github.vicen621.board.Board;
import io.github.vicen621.pieces.FacedPiece;
import io.github.vicen621.pieces.Tickable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractGhost extends FacedPiece implements Tickable {

    private static final int MOVE_SPEED = 5;
    private static final int FRIGHTENED_SPEED = 8;
    private static final int DEAD_SPEED = 3;


    private final GhostManager ghostManager;
    private final Point target;
    private final Image frightenedGhost;
    private final Image frightenedFlashes;
    private final Image[] frightenedEyes;
    private int moveCounter;
    private boolean alive;

    public AbstractGhost(String imagePath, GhostManager ghostManager) {
        super(imagePath);

        this.ghostManager = ghostManager;
        this.target = new Point();
        this.alive = true;
        getPos().setLocation(new Point(13, 14));

        this.frightenedGhost = loadImage("/assets/characters/ghosts/frightened/frightened.gif");
        this.frightenedFlashes = loadImage("/assets/characters/ghosts/frightened/frightened_flashes.gif");
        this.frightenedEyes = new Image[Face.values().length];
        loadImages("/assets/characters/ghosts/frightened/eyes/frightened_eyes.png", frightenedEyes);
    }

    @Override
    public void tick() {
        this.moveCounter += 1;
        if (this.moveCounter < (!isAlive() ? DEAD_SPEED : ghostManager.getMode() == GhostManager.GhostMode.FRIGHTENED ? FRIGHTENED_SPEED : MOVE_SPEED))
            return;

        this.moveCounter = 0;

        calculateTarget(ghostManager.getMode());

        int distance = 30000;
        Face nextFace = Face.UP;

        if (ghostManager.getMode() == GhostManager.GhostMode.FRIGHTENED) {
            // If in frightened mode, choose random location
            ArrayList<Face> faces = new ArrayList<>();
            for (Face face : Face.values()) {
                if (face.getOpposite() == getFace())
                    continue; // The ghost can't go back

                Point nextPos = new Point(getPos());
                nextPos.translate(face.dx, face.dy);

                if (!Board.checkCollision(nextPos))
                    faces.add(face);
            }

            nextFace = faces.get(new Random().nextInt(faces.size()));
        } else {
            for (Face face : Face.values()) {
                if (getFace().getOpposite() == face)
                    continue; // The ghost can't go back

                Point nextPos = new Point(getPos());
                nextPos.translate(face.dx, face.dy);

                if (!Board.checkCollision(nextPos)) {
                    if (nextPos.distanceSq(target) < distance) {
                        distance = (int) nextPos.distanceSq(target);
                        nextFace = face;
                    }
                }
            }
        }

        if (getPos().y == Board.TUNNEL_ROW) {
            if (getPos().x < 0) {
                getPos().x = Board.COLUMNS - 1;
            } else if (getPos().x >= Board.COLUMNS) {
                getPos().x = 0;
            }
        }

        setFace(nextFace);
        getPos().translate(getFace().dx, getFace().dy);
    }

    @Override
    public Image getImage() {
        if (!alive)
            return frightenedEyes[getFace().ordinal()];
        else if (ghostManager.getMode() == GhostManager.GhostMode.FRIGHTENED)
            return ghostManager.getFrightenedCounter() < 4 ? frightenedGhost : frightenedFlashes;
        else
            return super.getImage();
    }

    public boolean calculateTarget(GhostManager.GhostMode mode) {
        if (!alive) {
            getTarget().setLocation(new Point(13, 14));
            return true;
        }

        return false;
    }

    public Point getTarget() {
        return target;
    }

    public GhostManager getGhostManager() {
        return ghostManager;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
