package io.github.vicen621.board;

import java.awt.*;

public class Wall {
    private final Point pos1, pos2;

    public Wall(Point pos1, Point pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public boolean isInBounds(Point pos) {
        return pos.x >= pos1.x && pos.x <= pos2.x && pos.y >= pos1.y && pos.y <= pos2.y;
    }
}
