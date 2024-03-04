package io.github.vicen621.pieces;

import io.github.vicen621.board.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.net.URL;

public abstract class FacedPiece extends AbstractPiece {

    // image that represents this piece's position on the board
    private final Image[] images;
    private Face face;

    public FacedPiece(String imagePath) {
        super();

        this.images = new Image[Face.values().length];
        this.face = Face.UP;

        this.loadImages(imagePath, images);
    }

    public void loadImages(final String imagePath, Image[] images) {
        for (Face face : Face.values()) {
            String path = imagePath
                    .replace(".gif", "_" + face.name().toLowerCase() + ".gif")
                    .replace(".png", "_" + face.name().toLowerCase() + ".png");

            images[face.ordinal()] = loadImage(path);
        }
    }

    public Image loadImage(final String imagePath) {
        URL url = getClass().getResource(imagePath);
        assert url != null;

        return new ImageIcon(url).getImage();
    }

    @Override
    public void draw(Graphics g, ImageObserver observer) {
        // with the Point class, note that pos.getX() returns a double, but
        // pos.x reliably returns an int. https://stackoverflow.com/a/30220114/4655368
        // this is also where we translate board grid position into a canvas pixel
        // position by multiplying by the tile size.
        g.drawImage(
                getImage(),
                getPos().x * Board.TILE_SIZE,
                getPos().y * Board.TILE_SIZE,
                observer
        );
    }

    public Image getImage() {
        return images[face.ordinal()];
    }

    public Face getFace() {
        return face;
    }

    public void setFace(Face face) {
        this.face = face;
    }

    public enum Face {
        UP(0, -1),
        RIGHT(1, 0),
        DOWN(0, 1),
        LEFT(-1, 0);

        public final int dx;
        public final int dy;

        Face(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public Face getOpposite() {
            switch (this) {
                case UP -> { return DOWN; }
                case RIGHT -> { return LEFT; }
                case DOWN -> { return UP; }
                case LEFT -> { return RIGHT; }
            }

            return null;
        }
    }
}
