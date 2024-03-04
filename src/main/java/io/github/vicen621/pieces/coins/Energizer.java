package io.github.vicen621.pieces.coins;

import io.github.vicen621.pieces.ImagePiece;

import java.awt.Point;

public class Energizer extends ImagePiece {
    public Energizer(Point pos) {
        super("/assets/coins/energizer.gif");

        getPos().setLocation(pos);
    }
}
