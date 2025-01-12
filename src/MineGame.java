import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class MineGame {

    private static final int FIELD_HEIGHT = 10;
    private static final int FIELD_WIDTH = 10;

    private static JFrame frame;
    private static JPanel gridPanel;
    private static JLabel[][] field = new JLabel[FIELD_HEIGHT][FIELD_WIDTH];
    private static boolean[][] wasClicked = new boolean[FIELD_HEIGHT][FIELD_WIDTH];
    private static boolean[][] hasBomb = new boolean[FIELD_HEIGHT][FIELD_WIDTH];

    private static Random r = new Random();

    public MineGame() {
        frame = new JFrame("Mine Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // set the exit button to close the program
        frame.setSize(800, 800); // set dimensions to 800 by 800 px

        frame.setLayout(new BorderLayout()); // there are different layouts used -- BorderLayout is one of them

        JPanel topPanel = new JPanel(); // initialize new JPanel object
        topPanel.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH); // add the topPanel to the top of the frame

        JPanel bottomPanel = new JPanel(); // Use a simpler layout like FlowLayout
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0,0)); // Center the button with padding
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

    public static void main(String[] args) {

        new MineGame();

    }

    public static void initializeGrid() {
        for (int i = 0; i < FIELD_HEIGHT; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {

                wasClicked[i][j] = false;

                JLabel square = new JLabel(" ", SwingConstants.CENTER);

                square.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY)); // border between squares
                square.setOpaque(true);

                if (r.nextInt(5) == 0) {
                    square.setBackground(Color.red);
                } else {
                    square.setBackground(Color.gray);
                }

                square.setPreferredSize(new Dimension(64, 64)); // makes the dimensions of individual squares 128 px
                field[i][j] = square;
                gridPanel.add(square);

                // The below code was INSPIRED by our good friend Chat, who helped us use MouseListener for clicking on a JLabel square
                final int row = i;
                final int col = j;
                square.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        squareClickColor(row, col, "green"); // Call a method when clicked
                    }
                });

                field[i][j] = square;
                gridPanel.add(square);


            }
        }
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

        System.out.println(scenario);

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
        squareClickColor(x, y, "green");
        squareClickColor(x + 1, y, "blue");
        squareClickColor(x - 1, y, "blue");
        squareClickColor(x, y + 1, "blue");
        squareClickColor(x, y - 1, "blue");

    }

    public static void createEndSquare(int x, int y) {
        squareClickColor(x, y, "yellow");
        squareClickColor(x + 1, y, "blue");
        squareClickColor(x - 1, y, "blue");
        squareClickColor(x, y + 1, "blue");
        squareClickColor(x, y - 1, "blue");
    }

    public static void squareClickColor(int x, int y, String color) {

        try {

            System.out.println("DEBUG: x: "  + x + " y: " + y);

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
            }

        } catch (IndexOutOfBoundsException e) {
            System.out.println("generated out of bounds square. x: " + x + " y: " + y);
        }

    }

}
