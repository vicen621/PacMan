package io.github.vicen621.pieces.coins;

import io.github.vicen621.pieces.ImagePiece;

import java.awt.*;

public class Coin extends ImagePiece {
    public Coin(Point pos) {
        super("/assets/coins/coin.png");
        getPos().setLocation(pos);
    }
}
