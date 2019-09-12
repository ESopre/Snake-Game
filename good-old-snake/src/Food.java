public class Food {

    private Snake snake = new Snake();
    private int foodX;
    private int foodY;


    private final int RANDOMPOSITION = 40;

    public void createFood() {

        // places the food in a random x and y value

        int location = (int) (Math.random() * RANDOMPOSITION);
        foodX = ((location * Board.getDotSize()));

        location = (int) (Math.random() * RANDOMPOSITION);
        foodY = ((location * Board.getDotSize()));

        if ((foodX == snake.getSnakeX(0)) && (foodY == snake.getSnakeY(0))) {
            createFood();
        }
    }

    public int getFoodX() {

        return foodX;
    }

    public int getFoodY() {
        return foodY;
    }
}