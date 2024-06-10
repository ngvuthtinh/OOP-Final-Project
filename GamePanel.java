package Main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;
import Main.Element.Bird;
import Main.Element.Pipe;
import Main.GameInputs.GameKeyListener;
import Main.Music.playMusic;

public class GamePanel extends JPanel {
    int boardWidth = 360;
    int boardHeight = 640;
    //Image
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    //Bird
    int birdX = boardWidth / 8;
    int birdY = boardHeight / 2;
    int birdWidth = 34;
    int birdHeight = 24;
    Bird bird;


    //Pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    ArrayList<Pipe> pipes;


    //game logic
    int VelocityX = -4; // move pipes to the Left speed (stimulate bird moving Right)
    int velocityY = 0;
    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }
    double gravity = 1;

    //game loop
    Timer gameLoop;
    Timer placePipesTimer;
    boolean gameOver = false;
    double score = 0;

    public GamePanel() {
        //Panel
        setPreferredSize((new Dimension(boardWidth, boardHeight)));
        setFocusable(true);
        //Timers
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        gameLoop = new Timer(1000 / 60, null);
        //Inputs
        GameKeyListener gameKeyListener = new GameKeyListener(this, gameLoop, placePipesTimer);
        addKeyListener(gameKeyListener);
        gameLoop.addActionListener(gameKeyListener);
        //load images
        backgroundImg = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("flappybirdbg.png"))).getImage();
        birdImg = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("flappybird.png"))).getImage();
        topPipeImg = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("toppipe.png"))).getImage();
        bottomPipeImg = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("bottompipe.png"))).getImage();
        //bird
        bird = new Bird(birdX, birdY, birdWidth, birdHeight, birdImg);
        //pipes
        pipes = new ArrayList<Pipe>();
        // place pipes timer
        placePipesTimer.start();
        //game timer
        gameLoop.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        //background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);
        //bird
        g.drawImage(bird.getImg(), bird.getX(), bird.getY(), bird.getWidth(), bird.getHeight(), null);
        //pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.getImg(), pipe.getX(), pipe.getY(), pipe.getWidth(), pipe.getHeight(), null);
        }
        //score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void placePipes() {
        int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = boardHeight / 4;

        Pipe topPipe = new Pipe(pipeX, randomPipeY, pipeWidth, pipeHeight, topPipeImg);
        topPipe.setY(randomPipeY);
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(pipeX, randomPipeY, pipeWidth, pipeHeight, bottomPipeImg);
        bottomPipe.setY(topPipe.getY() + pipeHeight + openingSpace); // place the bottom pipe below the top pipe
        pipes.add(bottomPipe);
    }



    public void move() {
        //bird
        velocityY += gravity;
        bird.setY(bird.getY() + velocityY);
        bird.setY(Math.max(bird.getY(), 0)); //bird cannot go above the screen

        //pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setX(pipe.getX() + VelocityX); // move pipes to the left
            if (!pipe.isPassed() && bird.getX() > pipe.getX() + pipe.getWidth()) {
                pipe.setPassed(true);
                playMusic.playSound("Res/getpoint.wav");
                score += 0.5; // because there are 2 pipes! so 0.5*2 = 1, 1 for each set of pipes
            }
            if (collision(bird, pipe)) {
                playMusic.playSound("Res/Die.wav");
                gameOver = true;
            }
        }

        if (bird.getY() > boardHeight) {
            playMusic.playSound("Res/Die.wav");
            gameOver = true;
        }

    }

    public boolean collision(Bird a, Pipe b) {
        boolean collision = a.getX() < b.getX() + b.getWidth() && a.getX() + a.getWidth() > b.getX() && a.getY() < b.getY() + b.getHeight() && a.getY() + a.getHeight() > b.getY();
        if (collision) {
            playMusic.playSound("Res/Die.wav");
        }
        return collision;    }

    public boolean isGameOver() {
        return gameOver;
    }
    public void resetGame() {
        bird.setY(birdY);
        velocityY = 0;
        pipes.clear();
        score = 0;
        gameOver = false;
        gameLoop.start();
        placePipesTimer.start();
    }

}

