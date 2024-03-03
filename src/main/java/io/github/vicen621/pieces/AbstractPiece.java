package io.github.vicen621.pieces;

import java.awt.*;
import java.awt.image.ImageObserver;

public abstract class AbstractPiece {
    // current position of the piece on the board grid
    private final Point pos;

    public AbstractPiece() {
        // set the initial position
        this.pos = new Point(0, 0);
    }

    public abstract void draw(Graphics g, ImageObserver observer);

    public Point getPos() {
        return pos;
    }
}
