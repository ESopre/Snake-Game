import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Board extends JPanel implements ActionListener {

// TODO: Implement a way for the player to win

    // Height and width of the game screen
    private final static int BOARDWIDTH = 1000;
    private final static int BOARDHEIGHT = 980;

    // Used to represent pixel size of food & our snake's joints
    private final static int PIXELSIZE = 25;

// The total amount of pixels allowed


    private final static int TOTALPIXELS = (BOARDWIDTH * BOARDHEIGHT)
            / (PIXELSIZE * PIXELSIZE);

    // A check to se that the game is running
    private boolean inGame = true;

    // Timer used to record tick times
    private Timer timer;

    // Sets the movementspeed of the snake, lower values means faster and higher means slower

    private static int speed = 45;

    // Instances of the snake and the food
    private Snake snake = new Snake();
    private Food food = new Food();

    public Board() {

        addKeyListener(new Keys());
        setBackground(Color.BLACK);
        setFocusable(true);

        setPreferredSize(new Dimension(BOARDWIDTH, BOARDHEIGHT));

        initializeGame();
    }

    // Paints the componentes to the screen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        draw(g);
    }

    // Draws the snake and food on the game screen
    void draw(Graphics g) {
        // Only draw if the game is running / the snake is alive
        if (inGame == true) {
            g.setColor(Color.green);
            g.fillRect(food.getFoodX(), food.getFoodY(), PIXELSIZE, PIXELSIZE); // food

            // Draw the snake.
            for (int i = 0; i < snake.getJoints(); i++) {
                // Head of the snake which is the part that the player controls
                if (i == 0) {
                    g.setColor(Color.RED);
                    g.fillRect(snake.getSnakeX(i), snake.getSnakeY(i),
                            PIXELSIZE, PIXELSIZE);
                    // Body of the sanke that follows the head
                } else {
                    g.fillRect(snake.getSnakeX(i), snake.getSnakeY(i),
                            PIXELSIZE, PIXELSIZE);
                }
            }

            // Syncs the graphic
            Toolkit.getDefaultToolkit().sync();
        } else {
            // Game ends when the snake dies
            endGame(g);
        }
    }

    void initializeGame() {
        snake.setJoints(3); // Size of the snake at the beginin of the game

        // Creates the body of the sanke
        for (int i = 0; i < snake.getJoints(); i++) {
            snake.setSnakeX(BOARDWIDTH / 2);
            snake.setSnakeY(BOARDHEIGHT / 2);
        }
        // The snakes moves right in the begining of the game
        snake.setMovingRight(true);

        // THe first foow is generated
        food.createFood();

        // set a timer to the game.
        timer = new Timer(speed, this);
        timer.start();
    }

    // if the snake overlaps with food on game screen
    void checkFoodCollisions() {

        if ((proximity(snake.getSnakeX(0), food.getFoodX(), 20))
                && (proximity(snake.getSnakeY(0), food.getFoodY(), 20))) {

            System.out.println("intersection");
            // Adds a joint when the sankes eats a food
            snake.setJoints(snake.getJoints() + 1);
            // Create a new food
            food.createFood();
        }
    }

    // Check collision with snake body or edges of the game screen
    void checkCollisions() {

        // If the snake hits it self
        for (int i = snake.getJoints(); i > 0; i--) {

            // Snake cant overlapp if it is samler that 5 joints
            if ((i > 5)
                    && (snake.getSnakeX(0) == snake.getSnakeX(i) && (snake
                    .getSnakeY(0) == snake.getSnakeY(i)))) {
                inGame = false; // game ends id snake colides with it self
            }
        }

        // If the snake collides with the edge
        if (snake.getSnakeY(0) >= BOARDHEIGHT) {
            inGame = false;
        }

        if (snake.getSnakeY(0) < 0) {
            inGame = false;
        }

        if (snake.getSnakeX(0) >= BOARDWIDTH) {
            inGame = false;
        }

        if (snake.getSnakeX(0) < 0) {
            inGame = false;
        }

        // If the game has ended, stop the timer
        if (!inGame) {
            timer.stop();
        }
    }

    void endGame(Graphics g) {

        // Game over screen
        String message = "Game over";
        Font font = new Font("Times New Roman", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(font);
        g.setColor(Color.red);
        g.setFont(font);

        // Draw the message to the board
        g.drawString(message, (BOARDWIDTH - metrics.stringWidth(message)) / 2,
                BOARDHEIGHT / 2);

        System.out.println("Game Ended");

    }

    // Run constantly as long as we're in game.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame == true) {

            checkFoodCollisions();
            checkCollisions();
            snake.move();

            System.out.println(snake.getSnakeX(0) + " " + snake.getSnakeY(0)
                    + " " + food.getFoodX() + ", " + food.getFoodY());
        }
        repaint();
    }

    private class Keys extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!snake.isMovingRight())) {
                snake.setMovingLeft(true);
                snake.setMovingUp(false);
                snake.setMovingDown(false);
            }

            if ((key == KeyEvent.VK_RIGHT) && (!snake.isMovingLeft())) {
                snake.setMovingRight(true);
                snake.setMovingUp(false);
                snake.setMovingDown(false);
            }

            if ((key == KeyEvent.VK_UP) && (!snake.isMovingDown())) {
                snake.setMovingUp(true);
                snake.setMovingRight(false);
                snake.setMovingLeft(false);
            }

            if ((key == KeyEvent.VK_DOWN) && (!snake.isMovingUp())) {
                snake.setMovingDown(true);
                snake.setMovingRight(false);
                snake.setMovingLeft(false);
            }

            if ((key == KeyEvent.VK_ENTER) && (inGame == false)) {

                inGame = true;
                snake.setMovingDown(false);
                snake.setMovingRight(false);
                snake.setMovingLeft(false);
                snake.setMovingUp(false);

                initializeGame();
            }
        }
    }

    private boolean proximity(int a, int b, int closeness) {
        return Math.abs((long) a - b) <= closeness;
    }

    public static int getAllDots() {
        return TOTALPIXELS;
    }

    public static int getDotSize() {
        return PIXELSIZE;
    }
}