import javax.swing.*;
import java.awt.*;

public class TitlePage extends MineGame {

    public static void main(String[] args) {

        ImageIcon gameLogoIcon = new ImageIcon("src/gamelogoicon.png");
        ImageIcon gameTitleIcon = new ImageIcon("src/gametitleicon.png");
        ImageIcon backgroundImage = new ImageIcon("src/titlebackground.png");

        JFrame title = new JFrame("Mine Game");
        title.setIconImage(gameLogoIcon.getImage());
        title.setSize(800, 800);
        title.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        title.setLayout(new BorderLayout());
        title.getContentPane().setBackground(Color.DARK_GRAY); // Set the background color

        JLabel gameTitle = new JLabel();
        gameTitle.setIcon(gameTitleIcon);
        gameTitle.setHorizontalAlignment(SwingConstants.CENTER); // Center the title
        title.add(gameTitle, BorderLayout.NORTH); // Add to the top of the frame

        JButton startButton = new JButton("Start Game");
        startButton.setPreferredSize(new Dimension(150, 50));
        startButton.addActionListener(e -> {
            new MineGame();
            title.dispose();
        });
        startButton.setForeground(Color.DARK_GRAY);

        JPanel buttonPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 300)); // Center the button with padding


        buttonPanel.add(startButton);
        title.add(buttonPanel, BorderLayout.CENTER);

        title.setVisible(true);
    }
}
