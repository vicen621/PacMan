package io.github.vicen621.board;

import io.github.vicen621.pieces.AbstractPiece;
import io.github.vicen621.pieces.coins.Coin;
import io.github.vicen621.pieces.characters.PacMan;
import io.github.vicen621.pieces.coins.PowerPellet;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board extends JPanel implements ActionListener, KeyListener {

    private final int DELAY = 25;
    public static final int TILE_SIZE = 24;
    public static final int COLUMNS = 28;
    public static final int ROWS = 36;
    public static final int TUNNEL_ROW = 17;
    public static final Point STARTING_POINT = new Point(13, 26);
    public static final List<Wall> WALLS = List.of(
            // Top part of the map
            new Wall(new Point(2, 5), new Point(5, 7)), // Square upper left
            new Wall(new Point(7, 5), new Point(11, 7)), // square mid left
            new Wall(new Point(13, 4), new Point(14, 7)), // square mid up
            new Wall(new Point(16, 5), new Point(20, 7)), // square mid right
            new Wall(new Point(22, 5), new Point(25, 7)), // square upper right
            new Wall(new Point(2, 9), new Point(5, 10)), // square lower left
            new Wall(new Point(7, 9), new Point(8, 16)), // Vertical T left
            new Wall(new Point(10, 9), new Point(17, 10)), // Horizontal T mid
            new Wall(new Point(19, 9), new Point(20, 16)), // Vertical T right
            new Wall(new Point(22, 9), new Point(25, 10)), // square lower right
            new Wall(new Point(13, 11), new Point(14, 13)), // Vertical T mid
            new Wall(new Point(9, 12), new Point(11, 13)), // Horizontal T left
            new Wall(new Point(16, 12), new Point(18, 13)), // Horizontal T right
            new Wall(new Point(0, 12), new Point(5, 16)), // Square Right
            new Wall(new Point(22, 12), new Point(27, 16)), // Square Left
            new Wall(new Point(10, 15), new Point(17, 19)), // Ghost cage | Mid of the map
            // Bottom part of the map
            new Wall(new Point(0, 18), new Point(5, 22)), // Square up Right
            new Wall(new Point(22, 18), new Point(27, 22)), // Square up Left
            new Wall(new Point(7, 18), new Point(8, 22)), // Square upper left
            new Wall(new Point(19, 18), new Point(20, 22)), // Square upper right
            new Wall(new Point(10, 21), new Point(17, 22)), // Horizontal T mid
            new Wall(new Point(13, 23), new Point(14, 25)), // Vertical T mid
            new Wall(new Point(2, 24), new Point(5, 25)), // Horizontal L left
            new Wall(new Point(4, 26), new Point(5, 28)), // Vertical T left
            new Wall(new Point(7, 24), new Point(11, 25)), // square mid left
            new Wall(new Point(16, 24), new Point(20, 25)), // square mid right
            new Wall(new Point(22, 24), new Point(25, 25)), // Horizontal L right
            new Wall(new Point(22, 26), new Point(23, 28)), // Vertical T right
            new Wall(new Point(1, 27), new Point(2, 28)), // Square mid left
            new Wall(new Point(10, 27), new Point(17, 28)), // Horizontal T mid
            new Wall(new Point(13, 29), new Point(14, 31)), // Vertical T mid
            new Wall(new Point(25, 27), new Point(26, 28)), // Square mid right
            new Wall(new Point(7, 27), new Point(8, 29)), // Vertical T Left
            new Wall(new Point(2, 30), new Point(11, 31)), // Horizontal T left
            new Wall(new Point(19, 27), new Point(20, 29)), // Vertical T right
            new Wall(new Point(16, 30), new Point(25, 31)) // Horizontal T right
    );

    private final PacMan pacMan;
    private final Timer timer;
    private final List<AbstractPiece> coins;
    private final List<PowerPellet> powerPellets;

    public Board() {
        // set the game board size
        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS, TILE_SIZE * ROWS));
        // set the game board background color
        setBackground(new Color(0, 0, 0));

        // initialize the game state
        pacMan = new PacMan();
        coins = new ArrayList<>();
        powerPellets = new ArrayList<>();
        populateCoins();

        // this timer will call the actionPerformed() method every DELAY ms
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // this method is called by the timer every DELAY ms.
        // use this space to update the state of your game or animation
        // before the graphics are redrawn.
        checkWinCondition();

        // prevent the player from disappearing off the board
        pacMan.tick();

        // give the player points for collecting coins
        collectCoins();
        collectPowerPellets();

        // calling repaint() will trigger paintComponent() to run again,
        // which will refresh/redraw the graphics.
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // when calling g.drawImage() we can use "this" for the ImageObserver
        // because Component implements the ImageObserver interface, and JPanel
        // extends from Component. So "this" Board instance, as a Component, can
        // react to imageUpdate() events triggered by g.drawImage()

        // draw our graphics.
        drawBackground(g);

        drawString(
                g,
                String.valueOf(pacMan.getScore()),
                new Rectangle(TILE_SIZE * 15, TILE_SIZE, TILE_SIZE * 4, TILE_SIZE)
        );
        drawString(
                g,
                "SCORE",
                new Rectangle(TILE_SIZE * 15, 0, TILE_SIZE * 4, TILE_SIZE)
        );

        for (AbstractPiece coin : coins) {
            coin.draw(g, this);
        }

        for (PowerPellet powerPellet : powerPellets) {
            powerPellet.draw(g, this);
        }

        pacMan.draw(g, this);

        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // react to key down events
        pacMan.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void drawBackground(Graphics g) {
        BufferedImage image;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResource("/assets/backgrounds/background.png")));
        } catch (Exception e) {
            System.out.println("Error opening image file: " + e.getMessage());
            return;
        }
        g.drawImage(image, 0, 3 * TILE_SIZE, null);
    }

    private void populateCoins() {
        powerPellets.add(new PowerPellet(new Point(1, 6)));
        powerPellets.add(new PowerPellet(new Point(1, 26)));
        powerPellets.add(new PowerPellet(new Point(26, 6)));
        powerPellets.add(new PowerPellet(new Point(26, 26)));

        for (int y = 4; y < ROWS - 3; y++) {
            for (int x = 1; x < COLUMNS - 1; x++) {
                Point point = new Point(x, y);
                if (checkValidCoinLocation(point))
                    coins.add(new Coin(point));
            }
        }
    }

    private boolean checkValidCoinLocation(Point pos) {
        Wall wall = new Wall(new Point(7, 12), new Point(20, 22));

        if (pos.y == 17 && (pos.x < 6 || pos.x > 21))
            return false; // Tunnel

        if ((pos.x == 13 || pos.x == 14) && pos.y == 26)
            return false; // Pacman start location

        if (wall.isInBounds(pos))
            return false; // Ghost cage

        // Power pellets
        for (PowerPellet powerPellet : powerPellets) {
            if (pos.equals(powerPellet.getPos()))
                return false;
        }

        // Walls
        if (checkCollision(pos))
            return false;

        return true;
    }

    private void collectCoins() {
        // allow player to pickup coins
        ArrayList<AbstractPiece> collectedCoins = new ArrayList<>();
        for (AbstractPiece coin : coins) {
            // if the player is on the same tile as a coin, collect it
            if (pacMan.getPos().equals(coin.getPos())) {
                // give the player some points for picking this up
                pacMan.addScore(10);
                collectedCoins.add(coin);
            }
        }
        // remove collected coins from the board
        coins.removeAll(collectedCoins);
    }

    private void collectPowerPellets() {
        // allow player to pickup power pellets
        ArrayList<PowerPellet> collectedCoins = new ArrayList<>();
        for (PowerPellet powerPellet : powerPellets) {
            // if the player is on the same tile as a power pellet, collect it
            if (pacMan.getPos().equals(powerPellet.getPos())) {
                // give the player some points for picking this up
                pacMan.addScore(100);
                collectedCoins.add(powerPellet);
            }
        }

        // remove collected power pellets from the board
        powerPellets.removeAll(collectedCoins);
    }

    private void checkWinCondition() {
        if (coins.isEmpty() && powerPellets.isEmpty()) {
            System.out.println("You win!");
            System.exit(0);
        }
    }

    /**
     * Draw a string centered within a rectangle
     * @param g the graphics object to draw with
     * @param text the text to draw
     * @param rect the rectangle to center the text within
     */
    private void drawString(Graphics g, String text, Rectangle rect) {
        // we need to cast the Graphics to Graphics2D to draw nicer text
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        // set the text color and font
        g2d.setColor(new Color(255, 255, 255));
        g2d.setFont(new Font("ArcadeClassic", Font.PLAIN, 25));
        // draw the score in the bottom center of the screen
        // https://stackoverflow.com/a/27740330/4655368
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        // the text will be contained within the rectangle.
        // determine the x coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // determine the y coordinate for the text
        // (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // draw the string
        g2d.drawString(text, x, y);
    }

    /**
     * Check if the given point is within the bounds of any wall
     * @param point the point to check
     * @return true if the point is within the bounds of any wall
     */
    public static boolean checkCollision(Point point) {
        // prevent the player from moving off the edge of the board vertically
        if (point.y <= 3 || point.y >= Board.ROWS - 3)
            return true;

        // prevent the player from moving off the edge of the board sideways
        if ((point.x <= 0 || point.x >= Board.COLUMNS - 1) && point.y != Board.TUNNEL_ROW)
            return true;

        // prevent the player from moving off the walls
        for (Wall wall : WALLS) {
            if (wall.isInBounds(point))
                return true;
        }

        return false;
    }
}
