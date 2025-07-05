import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private final int TILE_SIZE = 25;
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int TOTAL_TILES = (WIDTH * HEIGHT) / (TILE_SIZE * TILE_SIZE);
    private final int[] x = new int[TOTAL_TILES];
    private final int[] y = new int[TOTAL_TILES];

    private int bodyParts = 6;
    private int foodX, foodY;
    private int score = 0;

    private char direction = 'R';
    private boolean running = false;
    private Timer timer;

    SnakeGame() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(this);
        startGame();
    }

    public void startGame() {
        placeFood();
        running = true;
        timer = new Timer(100, this);
        timer.start();
    }

    public void placeFood() {
        Random rand = new Random();
        foodX = rand.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
        foodY = rand.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            // draw grid (optional)
            /*
            for (int i = 0; i < WIDTH / TILE_SIZE; i++) {
                g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, HEIGHT);
                g.drawLine(0, i * TILE_SIZE, WIDTH, i * TILE_SIZE);
            }
            */

            // draw food
            g.setColor(Color.red);
            g.fillOval(foodX, foodY, TILE_SIZE, TILE_SIZE);

            // draw snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
            }

            // draw score
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString("Score: " + score, 10, 20);
        } else {
            gameOver(g);
        }
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U' -> y[0] -= TILE_SIZE;
            case 'D' -> y[0] += TILE_SIZE;
            case 'L' -> x[0] -= TILE_SIZE;
            case 'R' -> x[0] += TILE_SIZE;
        }
    }

    public void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            bodyParts++;
            score++;
            placeFood();
        }
    }

    public void checkCollision() {
        // hits itself
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }

        // hits wall
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        // display score
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Game Over", WIDTH / 2 - 120, HEIGHT / 2 - 20);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, WIDTH / 2 - 30, HEIGHT / 2 + 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkCollision();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> {
                if (direction != 'R') direction = 'L';
            }
            case KeyEvent.VK_RIGHT -> {
                if (direction != 'L') direction = 'R';
            }
            case KeyEvent.VK_UP -> {
                if (direction != 'D') direction = 'U';
            }
            case KeyEvent.VK_DOWN -> {
                if (direction != 'U') direction = 'D';
            }
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game - Java");
        SnakeGame gamePanel = new SnakeGame();

        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
