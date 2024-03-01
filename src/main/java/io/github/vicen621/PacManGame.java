package io.github.vicen621;

import io.github.vicen621.board.Board;

import javax.swing.*;

public class PacManGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(PacManGame::initWindow);
    }

    public static void initWindow() {
        // create a window frame and set the title in the toolbar
        JFrame frame = new JFrame("PacMan");
        // when we close the window, stop the app
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create the JPanel to draw on
        // this also initializes the game loop
        Board board = new Board();
        frame.add(board);
        frame.addKeyListener(board);

        // don't allow the user to resize the window
        frame.setResizable(false);
        // fit the window size around the components (just our jpanel).
        // pack() should be called after setResizable() to avoid issues on some platforms
        frame.pack();
        // open window in the center of the screen
        frame.setLocationRelativeTo(null);
        // display the window
        frame.setVisible(true);
    }
}