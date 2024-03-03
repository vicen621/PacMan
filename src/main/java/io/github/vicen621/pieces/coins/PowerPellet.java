package io.github.vicen621.pieces.coins;

import io.github.vicen621.pieces.ImagePiece;

import java.awt.*;

public class PowerPellet extends ImagePiece {
    public PowerPellet(Point pos) {
        super("/assets/coins/power_pellet.gif");

        getPos().setLocation(pos);
    }
}
