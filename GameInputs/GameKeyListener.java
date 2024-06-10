package Main.GameInputs;

import Main.GamePanel;
import Main.Music.playMusic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

public class GameKeyListener implements KeyListener, ActionListener {
    private GamePanel gamePanel;
    private Timer gameLoop;
    private Timer placePipesTimer;
    public GameKeyListener(GamePanel gamePanel, Timer gameLoop, Timer placePipesTimer){
        this.gamePanel = gamePanel;
        this.gameLoop = gameLoop;
        this.placePipesTimer = placePipesTimer;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            gamePanel.setVelocityY(-9);
            playMusic.playMusic("Sources/Fly.wav");
            if(gamePanel.isGameOver()){
                gamePanel.resetGame();
            }
        }
    }
        public void keyReleased (KeyEvent e){
        }

        public void actionPerformed (ActionEvent e){
            gamePanel.move();
            gamePanel.repaint();
            if(gamePanel.isGameOver()){
                placePipesTimer.stop();
                gameLoop.stop();
            }
        }
}

