import javax.swing.*;
import java.util.Random;

public class Grid {

    public static boolean isFirstGrid = false;

    int HEIGHT;
    int WIDTH;
    Square selectedSquare;
    Square startSquare;
    Square targetSquare;
    Square[][] grid;

    Random r;

    public Grid(int height, int width, int startX, int startY, int targetX, int targetY) {

        if (isFirstGrid) {
            isFirstGrid = true;
        } else {
            resetGrid();
        }

        r = new Random();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                grid[i][j] = new Square(i, j);

                grid[i][j].label = new JLabel(" ", SwingConstants.CENTER);

                if (r.nextInt(4) == 0) {
                    grid[i][j].isABomb = true;
                }

            }
        }

    }

    public void getAllNumbersOfNeighboringBombs() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {

                grid[i][j] = new Square(i, j);

                grid[i][j].label = new JLabel(" ", SwingConstants.CENTER);

                if (r.nextInt(4) == 0) {
                    grid[i][j].isABomb = true;
                }

            }
        }
    }

    public int getNumberOfNeighboringBombs(int x, int y) {

        // a square with a bomb doesn't neighbor any other bomb for game logic purposes.
        if (grid[x][y].isABomb) return 0;

        int numberOfBombs = 0;
        if (checkIfCoordsAreInBounds(x + 1, y) && grid[x + 1][y].isABomb) numberOfBombs++;
        if (checkIfCoordsAreInBounds(x - 1, y) && grid[x - 1][y].isABomb) numberOfBombs++;
        if (checkIfCoordsAreInBounds(x, y + 1) && grid[x][y + 1].isABomb) numberOfBombs++;
        if (checkIfCoordsAreInBounds(x, y - 1) && grid[x][y - 1].isABomb) numberOfBombs++;

        return numberOfBombs;
    }

    public boolean checkIfCoordsAreInBounds(int x, int y) {
        return ((x >= 0) && (x < WIDTH) && (y >= 0) && (y < HEIGHT));
    }

    public void resetGrid() {

        selectedSquare = null;
        startSquare = null;
        targetSquare = null;

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                grid[i][j] = null;
            }
        }

    }

}
