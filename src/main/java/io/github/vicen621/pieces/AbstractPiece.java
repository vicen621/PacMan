package io.github.vicen621.pieces;

import io.github.vicen621.board.Board;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.Objects;

public abstract class AbstractPiece {
    // image that represents this piece's position on the board
    private BufferedImage image;
    // current position of the piece on the board grid
    private final Point pos;

    public AbstractPiece(String imagePath) {
        // load the assets
        this.loadImage(imagePath);

        // set the initial position
        this.pos = new Point(0, 0);
    }

    public void loadImage(String imagePath) {
        try {
            // you can use just the filename if the image file is in your
            // project folder, otherwise you need to provide the file path.
            image = ImageIO.read(Objects.requireNonNull(getClass().getResource(imagePath)));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }

    public void draw(Graphics g, ImageObserver observer) {
        // with the Point class, note that pos.getX() returns a double, but
        // pos.x reliably returns an int. https://stackoverflow.com/a/30220114/4655368
        // this is also where we translate board grid position into a canvas pixel
        // position by multiplying by the tile size.
        g.drawImage(
                image,
                pos.x * Board.TILE_SIZE,
                pos.y * Board.TILE_SIZE,
                observer
        );
    }

    public Point getPos() {
        return pos;
    }
}
