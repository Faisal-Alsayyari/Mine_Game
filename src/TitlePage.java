import javax.swing.*;
import java.awt.*;

public class TitlePage extends MineGame {

    public static void main(String[] args) {

        ImageIcon gameicon = new ImageIcon("src/gamelogoicon.png");

        JFrame title = new JFrame("Mine Game");
        title.setIconImage(gameicon.getImage());
        title.setVisible(true);
        title.setSize(800,800);
        title.setPreferredSize(new Dimension(800,800));
        title.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        title.setLayout(new FlowLayout());
        title.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 450));

        JButton startButton = new JButton("Start Game");
        startButton.setPreferredSize(new Dimension(120, 50)); // Set button size
        startButton.addActionListener(e -> {
            new MineGame();

            title.dispose();
        });

//        ImageIcon gameTitleIcon = new ImageIcon("src/gametitleicon.png");
//        JLabel gameTitle = new JLabel();
//        gameTitle.setLayout(new FlowLayout(FlowLayout.CENTER));
//        gameTitle.setIcon(gameicon);
//
        title.add(startButton);
//        title.add(gameTitle);


    }
}
