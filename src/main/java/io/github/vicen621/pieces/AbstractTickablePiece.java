package io.github.vicen621.pieces;

public abstract class AbstractTickablePiece extends AbstractPiece {
    public AbstractTickablePiece(String imagePath) {
        super(imagePath);
    }

    public abstract void tick();
}
