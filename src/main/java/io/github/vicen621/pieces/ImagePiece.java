package io.github.vicen621.pieces;

import io.github.vicen621.board.Board;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URL;

public abstract class ImagePiece extends AbstractPiece {
    private Image image;
    public ImagePiece(String imagePath) {
        super();
        this.loadImage(imagePath);
    }

    public void loadImage(final String imagePath) {
        URL url = getClass().getResource(imagePath);
        assert url != null;

        try {
            if (imagePath.endsWith(".gif"))
                image = new ImageIcon(url).getImage();
            else
                image = ImageIO.read(url);
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }

    @Override
    public void draw(Graphics g, ImageObserver observer) {
        // with the Point class, note that pos.getX() returns a double, but
        // pos.x reliably returns an int. https://stackoverflow.com/a/30220114/4655368
        // this is also where we translate board grid position into a canvas pixel
        // position by multiplying by the tile size.
        g.drawImage(
                image,
                getPos().x * Board.TILE_SIZE,
                getPos().y * Board.TILE_SIZE,
                observer
        );
    }
}
