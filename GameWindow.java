package Main;
import Main.Music.playMusic;
import javax.swing.*;

public class GameWindow {
    private GamePanel gamePanel;
    private JFrame jframe;
    public GameWindow(){
        int boardWidth = 360;
        int boardHeight = 640;

        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.pack();
        gamePanel.requestFocus();
        playMusic.playMusic("Sources/videoplayback.wav");
        frame.setVisible(true);
    }
}
