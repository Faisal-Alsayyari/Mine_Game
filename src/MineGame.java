import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
// hi
public class MineGame {

    private static final int FIELD_HEIGHT = 10;
    private static final int FIELD_WIDTH = 10;
    public static int prevx = -1;
    public static int prevy = -1;
    public static int endx;
    public static int endy;
    public static int selx;
    public static int sely;

    public static JLabel topPanelLabel = new JLabel();
    private static JFrame frame;
    private static JPanel gridPanel;
    private static JLabel[][] field = new JLabel[FIELD_HEIGHT][FIELD_WIDTH];
    private static boolean[][] wasClicked = new boolean[FIELD_HEIGHT][FIELD_WIDTH];
    private static boolean[][] hasBomb = new boolean[FIELD_HEIGHT][FIELD_WIDTH];
    private static boolean[][] canClickOnSquare = new boolean[FIELD_HEIGHT][FIELD_WIDTH];

    public static final Font numberFont = new Font("Comic Sans", 3, 18);

    private static Random r = new Random();

    public MineGame() {
        frame = new JFrame("Mine Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // set the exit button to close the program
        frame.setSize(800, 800); // set dimensions to 800 by 800 px
        frame.setPreferredSize(new Dimension(800,800));

        ImageIcon gameicon = new ImageIcon("src/gamelogoicon.png");
        frame.setIconImage(gameicon.getImage());

        frame.setLayout(new BorderLayout()); // there are different layouts used -- BorderLayout is one of them

        JPanel topPanel = new JPanel(); // initialize new JPanel object
        topPanel.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH); // add the topPanel to the top of the frame

        topPanelLabel.setLayout(new BorderLayout());
        topPanel.add(topPanelLabel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(); // Use a simpler layout like FlowLayout
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0,0)); // Center the button with padding
        bottomPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add a border to the bottom panel
        bottomPanel.setBackground(Color.LIGHT_GRAY); // Set the background color of the bottom panel
        frame.add(bottomPanel, BorderLayout.SOUTH);

        Button restartButton = new Button("Restart");
        restartButton.setPreferredSize(new Dimension(100, 50)); // Set the size of the button explicitly
        restartButton.addActionListener(e -> restartGame());
        bottomPanel.add(restartButton); // Add the button to the bottom panel

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // gridbaglayout (gbc) is another layout used

        gridPanel = new JPanel(new GridLayout(FIELD_HEIGHT, FIELD_WIDTH, 0, 0)); // determines the border between squares
        gridPanel.setPreferredSize(new Dimension(640, 640)); // the window CAN be resized, but it tries to make the gridPanel element 600x600

        initializeGrid();

        setStartingPoint();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 20, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(gridPanel, gbc);

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.setVisible(true);

    }

//    public static void main(String[] args) {
//
//        new MineGame();
//
//    }

    public static void initializeGrid() {

        for (int i = 0; i < FIELD_HEIGHT; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {

                // these two lines wipe the arrays so if it's regenerated then it's new and shit
                wasClicked[i][j] = false;
                hasBomb[i][j] = false;
                canClickOnSquare[i][j] = false;

                JLabel square = new JLabel(" ", SwingConstants.CENTER);

                square.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY)); // border between squares
                square.setOpaque(true);

                if (r.nextInt(5) == 0) {
                    // square.setBackground(Color.red);
                    square.setBackground(Color.gray);
                    hasBomb[i][j] = true;
                } else {
                    square.setBackground(Color.gray);
                }

                square.setPreferredSize(new Dimension(64, 64)); // makes the dimensions of individual squares 128 px
                field[i][j] = square;
                gridPanel.add(square);

                // The below code was INSPIRED by our good friend Chat, who helped us use MouseListener for clicking on a JLabel square
                final int row = i;
                final int col = j;
                square.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            int x = revealSquare(row, col);
                            System.out.println(x);
                            switch (x) {
                                case -2:
                                    break;
                                case -1:
                                    endGame();
                                    break;
                                case 0:
                                    break;
                                default:
                                    square.setText(String.valueOf(x));
                            }
                            gridPanel.revalidate(); // Revalidate the panel to refresh the layout
                            gridPanel.repaint();
                            selx = row;
                            sely = col;
                            topPanelLabel.setText(String.valueOf(distanceFromObjective(selx,sely)));
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            placeFlag(row, col);
                            gridPanel.revalidate(); // Revalidate the panel to refresh the layout
                            gridPanel.repaint();
                        }
                    }
                });

                field[i][j] = square;
                gridPanel.add(square);


            }
        }
    }

    /*
    returns an int depending on what to do when the user clicks on a square
    -2: do nothing (because the user can't click on a non-adjacent square)
    -1: end the game (because the player clicked on a bomb)
     0 to 4: reveal the number of bombs this square touches (because the user clicked on a valid square)
     */

    public static int revealSquare(int x, int y) {

        // first case: check if user can click on the square.
        if (!canClickOnSquare[x][y]) {
            return -2;
        }


        // first case: if the user clicked on a bomb
        if (hasBomb[x][y]) {
            setSquareColor(x, y, "red");
            return -1; // handle game failure
        }

        // now, reveal the number of squares

        handleSelectedSquare(x, y);

        setSurroundingSquaresAsRevealable(x, y);

        return getNumberOfNeighboringBombs(x, y);
    }

    public static void setSurroundingSquaresAsRevealable(int x, int y) {
        if (checkIfCoordsAreInBounds(x + 1, y)) {
            canClickOnSquare[x + 1][y] = true;
        }

        if (checkIfCoordsAreInBounds(x - 1, y)) {
            canClickOnSquare[x - 1][y] = true;
        }

        if (checkIfCoordsAreInBounds(x, y + 1)) {
            canClickOnSquare[x][y + 1] = true;
        }

        if (checkIfCoordsAreInBounds(x, y - 1)) {
            canClickOnSquare[x][y - 1] = true;
        }
    }

    public static void placeFlag(int x, int y) {
        setSquareColor(x, y, "blue");
    }

    public static void endGame() {
        System.out.println("Placeholder: BOOYA! YOU CLICKED ON A BOMB NOOB! GAME OVER");
    }

    public static void restartGame() {
        gridPanel.removeAll();
        initializeGrid();
        gridPanel.revalidate(); // Revalidate the panel to refresh the layout
        gridPanel.repaint();
        setStartingPoint();

    }

    public static boolean setStartingPoint() {

        // candidates for starting square must be between 0-2 and 8-10 on both x and y

        int scenario = r.nextInt(4);

        int firstRandomLowNumber = r.nextInt(3);
        int secondRandomLowNumber = r.nextInt(3);
        int firstRandomHighNumber = r.nextInt(7, 9);
        int secondRandomHighNumber = r.nextInt(7, 9);

        switch (scenario) {

            case 0: // starting square is top left
                createStartSquare(firstRandomLowNumber, secondRandomLowNumber);
                createEndSquare(firstRandomHighNumber, secondRandomHighNumber);
                break;
            case 1: // starting square is top right
                createStartSquare(firstRandomLowNumber, firstRandomHighNumber);
                createEndSquare(firstRandomHighNumber, secondRandomLowNumber);
                break;
            case 2: // starting square is bottom left
                createStartSquare(firstRandomHighNumber, secondRandomLowNumber);
                createEndSquare(secondRandomLowNumber, secondRandomHighNumber);
                break;
            case 3: // starting square is bottom right
                createStartSquare(firstRandomHighNumber, secondRandomHighNumber);
                createEndSquare(firstRandomLowNumber, secondRandomLowNumber);
                break;

        }


        return true;
    }

    public static void createStartSquare(int x, int y) {

        hasBomb[x][y] = false;
        canClickOnSquare[x][y] = true;

        setSquareColor(x, y, "light_gray");

        if (checkIfCoordsAreInBounds(x + 1, y)) {
            updateSquare(x + 1, y, getNumberOfNeighboringBombs(x + 1, y), "light_gray");
            hasBomb[x + 1][y] = false;
        }

        if (checkIfCoordsAreInBounds(x - 1, y)) {
            updateSquare(x - 1, y, getNumberOfNeighboringBombs(x - 1, y), "light_gray");
            hasBomb[x - 1][y] = false;
        }

        if (checkIfCoordsAreInBounds(x, y + 1)) {
            updateSquare(x, y + 1, getNumberOfNeighboringBombs(x, y + 1), "light_gray");
            hasBomb[x][y + 1] = false;
        }

        if (checkIfCoordsAreInBounds(x, y - 1)) {
            updateSquare(x, y - 1, getNumberOfNeighboringBombs(x, y - 1), "light_gray");
            hasBomb[x][y - 1] = false;
        }
    }

    public static void updateSquare(int x, int y, int num, String color) {
        setSquareColor(x, y, color);
        if (num != 0) {
            field[x][y].setText(String.valueOf(num));
        } else {
            field[x][y].setText(" ");
        }
        setSurroundingSquaresAsRevealable(x, y);
    }

    public static void createEndSquare(int x, int y) {

        hasBomb[x][y] = false;

        setSquareColor(x, y, "gray");

        if (checkIfCoordsAreInBounds(x + 1, y)) {
            setSquareColor(x, y, "gray");
            hasBomb[x + 1][y] = false;
        }

        if (checkIfCoordsAreInBounds(x - 1, y)) {
            setSquareColor(x, y, "gray");
            hasBomb[x - 1][y] = false;
        }

        if (checkIfCoordsAreInBounds(x, y + 1)) {
            setSquareColor(x, y, "gray");
            hasBomb[x][y + 1] = false;
        }

        if (checkIfCoordsAreInBounds(x, y - 1)) {
            setSquareColor(x, y - 1, "gray");
            hasBomb[x][y - 1] = false;
        }

        endx = x;
        endy = y;

    }

    public static boolean checkIfCoordsAreInBounds(int x, int y) {
        return ((x >= 0) && (x <= 9) && (y >= 0) && (y <= 9));
    }

    public static int getNumberOfNeighboringBombs(int x, int y) {

        // a square with a bomb doesn't neighbor any other bomb for game logic purposes.
        if (hasBomb[x][y]) return 0;

        int numberOfBombs = 0;
        if (checkIfCoordsAreInBounds(x + 1, y) && hasBomb[x + 1][y]) numberOfBombs++;
        if (checkIfCoordsAreInBounds(x - 1, y) && hasBomb[x - 1][y]) numberOfBombs++;
        if (checkIfCoordsAreInBounds(x, y + 1) && hasBomb[x][y + 1]) numberOfBombs++;
        if (checkIfCoordsAreInBounds(x, y - 1) && hasBomb[x][y - 1]) numberOfBombs++;

        return numberOfBombs;
    }

    public static void setSquareColor(int x, int y, String color) {

        try {

            color = color.toUpperCase();

            switch (color) {
                case "RED":
                    field[x][y].setBackground(Color.red);
                    break;
                case "GRAY":
                    field[x][y].setBackground(Color.gray);
                    break;
                case "LIGHT_GRAY":
                    field[x][y].setBackground(Color.LIGHT_GRAY);
                    break;
                case "GREEN":
                    field[x][y].setBackground(new Color(0x00AA00));
                    break;
                case "YELLOW":
                    field[x][y].setBackground(Color.yellow);
                    break;
                case "BLUE":
                    field[x][y].setBackground(Color.BLUE);
                    break;
                case "WHITE":
                    field[x][y].setBackground(Color.WHITE);
                    break;
            }

        } catch (IndexOutOfBoundsException e) {
            System.out.println("DEBUG generated out of bounds square. x: " + x + " y: " + y);
        }

    }

    public static void handleSelectedSquare(int x, int y) {

        if (prevx != -1 && prevy != -1) {
            setSquareColor(prevx, prevy, "light_gray");
            System.out.println("Previous selected square: " + prevx + ", " + prevy);
        }

        setSquareColor(x, y, "white");
        System.out.println("Current selected square: " + x + ", " + y);

        prevx = x;
        prevy = y;
    }

    public static double distanceFromObjective(int x, int y) {

        int subtx = Math.abs(x - endx);
        int subty = Math.abs(y - endy);

        int sqx = subtx*subtx;
        int sqy = subty*subty;

        int added = sqx + sqy;
        double distance = Math.sqrt((double)added);
        distance = Math.round(distance);

        return distance;
    }

}
