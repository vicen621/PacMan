package io.github.vicen621.pieces.characters.ghosts;

import io.github.vicen621.board.Board;
import io.github.vicen621.board.Wall;
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
    private final Point initialPos;
    private final Image frightenedGhost;
    private final Image frightenedFlashes;
    private final Image[] frightenedEyes;
    private final int timeToLeaveCage;
    private int leaveCageTimer;
    private int moveCounter;
    private boolean alive;
    private boolean eaten;

    public AbstractGhost(String imagePath, GhostManager ghostManager, Point pos, int timeToLeaveCage) {
        super(imagePath);

        this.ghostManager = ghostManager;
        this.initialPos = pos;
        this.target = new Point();
        this.timeToLeaveCage = timeToLeaveCage * 40; // because the tick method is called 40 times per second
        this.alive = true;
        this.eaten = false;
        getPos().setLocation(pos);

        this.frightenedGhost = loadImage("/assets/characters/ghosts/frightened/frightened.gif");
        this.frightenedFlashes = loadImage("/assets/characters/ghosts/frightened/frightened_flashes.gif");
        this.frightenedEyes = new Image[Face.values().length];
        loadImages("/assets/characters/ghosts/frightened/eyes/frightened_eyes.png", frightenedEyes);
    }

    @Override
    public void tick() {
        if (this.leaveCageTimer < timeToLeaveCage) {
            this.leaveCageTimer += 1;
            return;
        }

        this.moveCounter += 1;

        if (this.moveCounter < getMoveSpeed())
            return;

        this.moveCounter = 0;

        // Si esta muerto y ya llego al punto de respawn
        if (!isAlive() && getPos().equals(new Point(14, 17)))
            setAlive(true);

        calculateTarget(ghostManager.getMode());

        int distance = 30000;
        Face nextFace = Face.UP;

        if (ghostManager.getMode() == GhostManager.GhostMode.FRIGHTENED && !isEaten()) {
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

                if (checkBoxCageDoor(nextPos) || !Board.checkCollision(nextPos)) {
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
        else if (ghostManager.getMode() == GhostManager.GhostMode.FRIGHTENED && !isEaten())
            return ghostManager.getFrightenedCounter() < Board.ENERGIZER_DURATION - 1 ? frightenedGhost : frightenedFlashes;
        else
            return super.getImage();
    }

    private int getMoveSpeed() {
        if (!isAlive())
            return DEAD_SPEED;

        if (ghostManager.getMode() == GhostManager.GhostMode.FRIGHTENED)
            return FRIGHTENED_SPEED;

        return MOVE_SPEED;
    }

    private boolean checkBoxCageDoor(Point pos) {
        Wall ghostCageDoor = new Wall(new Point(14, 15), new Point(15, 15));

        if (!isAlive() || isInCage()) {
            if (ghostCageDoor.isInBounds(pos))
                return true;
        }
        return false;
    }

    private boolean isInCage() {
        return getPos().x >= 10 && getPos().x <= 17 && getPos().y >= 15 && getPos().y <= 19;
    }

    public boolean calculateTarget(GhostManager.GhostMode mode) {
        // If it's dead, I make it go to the respawn point
        if (!alive) {
            getTarget().setLocation(new Point(14, 17));
            return true;
        }

        // If it's in the cage, I make it go through the door
        if (isInCage()) {
            getTarget().setLocation(new Point(13, 14));
            return true;
        }

        return false;
    }

    public void reset() {
        getPos().setLocation(this.initialPos);
        setFace(Face.UP);
        moveCounter = 0;
        leaveCageTimer = 0;
        alive = true;
        eaten = false;
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

    public boolean isEaten() {
        return eaten;
    }

    public void eat() {
        alive = false;
        eaten = true;
        getTarget().setLocation(new Point(14, 16));
    }

    public void resetEaten() {
        eaten = false;
    }
}
