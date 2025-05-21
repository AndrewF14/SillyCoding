import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends JPanel implements KeyListener, ActionListener {

    // Play and Score
    private boolean play = false;
    private boolean firstPlay = true;
    private int score = 0;
    private int highScore = 0;
    private boolean wonLastGame = false;
    private boolean justWon = false;

    // Timer
    private final Timer timer;
    private int delay = 8;

    // Paddle Variables
    private int paddleX = 310;
    private int paddleWidth = 100;
    private int longPaddleWidth = 150;
    private boolean left = false, right = false, f_Pressable = true;

    // Paddle Constants
    private final int startingPaddleX = 310;
    private final int paddleY = 550;
    private final int paddleHeight = 8;

    // Ball Variables
    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballDirX = 0;
    private int ballDirY = 0;

    // Ball Constants
    private final int ballWidth = 20;
    private final int ballHeight = 20;
    private final int startingBallX = 120;
    private final int startingBallY = 350;
    private final int startingBallDirX = -2;
    private final int startingBallDirY = -4;

    // Brick Generator
    private BrickGenerator map;
    private int brickRows = 3;     // standard: 3 | alts: 6, 9, 12
    private int brickColumns = 7; // standard: 7  | alts: 14, 21, 28
    private int totalBricks = brickRows * brickColumns;

    // Frame Constants
    private int frameHeight = Constants.frameHeight;
    private int frameWidth = Constants.frameWidth;

    // Speed Variables
    private int speedMulti = 1;
    private final int defaultSpeed = 1;

    // Powerup variables
    private int multiGamesLeft = 0;
    private boolean longPaddle = false;
    private int lives = 1;





    public Breakout() {
        map = new BrickGenerator(brickRows, brickColumns);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        // Background
        g.setColor(Color.black);
        g.fillRect(0, 0, frameWidth, frameHeight);

        // Drawing bricks
        map.draw((Graphics2D) g);

        // Borders
        g.setColor(Color.magenta);
        g.fillRect(0, 0, 5, frameHeight);
        g.fillRect(0, 0, frameWidth, 5);
        g.fillRect(frameWidth - 20, 0, 5, frameHeight);

        // Score
        
        if(multiGamesLeft > 0){
            g.setColor(Color.yellow);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
            g.drawString("x2 Games Left: " + multiGamesLeft, 530, 45);
        }else{
            g.setColor(Color.white);
        }
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
        g.drawString("Score: " + score, 540, 30);

        g.setColor(Color.white);
        if(score > highScore) highScore = score;
        g.drawString("Best:" + highScore, frameWidth / 2 - 50, 30);

        // Speed multiplier
        g.setColor(Color.white);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
        if(speedMulti != 5){
            g.drawString("Speed: " + speedMulti + "x", 10, 30);
        }else{
            g.drawString("Speed: " + speedMulti + "x (max!)", 10, 30);
        }

        // Paddle
        g.setColor(Color.magenta);
        if(longPaddle) {
            g.fillRect(paddleX, paddleY, longPaddleWidth, paddleHeight);  
        }else{
            g.fillRect(paddleX, paddleY, paddleWidth, paddleHeight);
        }

        // Ball
        switch(lives){
            case 1: g.setColor(Color.red); break;
            case 2: g.setColor(Color.orange); break;
            case 3: g.setColor(Color.yellow); break;
            default: g.setColor(Color.green); break;
        }
        g.fillOval(ballPosX, ballPosY, ballWidth, ballHeight);

        // Win condition
        if (totalBricks <= 0 && play) {
            play = false;
            ballDirX = ballDirY = 0;
            justWon = true;
            wonLastGame = true;
            if(multiGamesLeft > 0) multiGamesLeft --;
            longPaddle = false;
        }

        if(justWon){
            g.setColor(Color.green);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            g.drawString("you won!", frameWidth / 3 + 50, frameHeight / 2);

            g.setColor(Color.white);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
            g.drawString("press 1 for an extra life (up to green ball)", frameWidth / 7, frameHeight / 2 + 50);
            g.drawString("press 2 for a longer paddle (1 game)", frameWidth / 7, frameHeight / 2 + 100);
            g.drawString("press 3 for 2 games of double score (stackable)", frameWidth / 7, frameHeight / 2 + 150);

        }

        if(!justWon && wonLastGame){
            g.setColor(Color.white);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
            g.drawString("press [ENTER] to play again and increase your score", frameWidth / 7, frameHeight / 2 + 50);
        }

            
            

        

        // Game over
        if (ballPosY > 570 && lives == 1) {
            play = false;
            ballDirX = ballDirY = 0;
            g.setColor(Color.red);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            g.drawString("game over.", frameWidth / 3  + 50, frameHeight / 2);

            g.setColor(Color.white);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
            g.drawString("press [ENTER] to restart.", frameWidth / 3, frameHeight / 2 + 50);

            multiGamesLeft = 0;
            longPaddle = false;

        }else if(ballPosY > 570){
            lives--;
            ballDirY = -ballDirY;
        }

        if(firstPlay) {
            g.setColor(Color.white);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            g.drawString("press [ENTER] to start.", frameWidth / 3 - 50, frameHeight / 2);

            g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
            g.drawString("press 'F' to increase speed.", frameWidth / 3 - 20, frameHeight / 2 + 33);

            g.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
            g.drawString("higher speed = higher score", frameWidth / 3 + 10, frameHeight / 2 + 66);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if (play) {
            if(longPaddle){
                if (new Rectangle(ballPosX, ballPosY, ballWidth, ballHeight)
                        .intersects(new Rectangle(paddleX, 550, longPaddleWidth, 8))) {
                    ballDirY = -ballDirY;
                }
            }else{
                if (new Rectangle(ballPosX, ballPosY, ballWidth, ballHeight)
                        .intersects(new Rectangle(paddleX, 550, paddleWidth, 8))) {
                    ballDirY = -ballDirY;
                }
            }

            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, ballWidth, ballHeight);
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;

                            if (multiGamesLeft > 0) {
                                score += 5 * speedMulti * 2;
                            }else{
                                score += 5 * speedMulti;
                            }
                            

                            if (ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width) {
                                ballDirX = -ballDirX;
                            } else {
                                ballDirY = -ballDirY;
                            }

                            break A;
                        }
                    }
                }
            }

            // move paddle
            if (left && paddleX > 2.5) paddleX -= (5 * (speedMulti));

            if(longPaddle){
                if (right && paddleX < 689.5 - longPaddleWidth) paddleX += (5 * (speedMulti));
            }else{
                if (right && paddleX < 689.5 - paddleWidth) paddleX += (5 * (speedMulti));
            }

            // continue ball speed
            ballPosX += ballDirX * speedMulti;
            ballPosY += ballDirY * speedMulti;

            // bounce ball if wall hit
            if (ballPosX < 0) ballDirX = -ballDirX;
            if (ballPosY < 0) ballDirY = -ballDirY;
            if (ballPosX > 666) ballDirX = -ballDirX;
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // left / right
        if (key == KeyEvent.VK_A) left = true;
        if (key == KeyEvent.VK_D) right = true;

        // speed up
        if (key == KeyEvent.VK_F && f_Pressable) {
            speedMulti += 1;
            f_Pressable = false;
        }

        // START GAME!!!
        if (key == KeyEvent.VK_ENTER) {
            if (!play && !justWon) {
                speedMulti = defaultSpeed;
                play = true;
                firstPlay = false;
                f_Pressable = true;
                ballPosX = startingBallX;
                ballPosY = startingBallY;
                ballDirX = startingBallDirX;
                ballDirY = startingBallDirY;
                paddleX = startingPaddleX;

                if(!wonLastGame) score = 0;
                wonLastGame = false;

                totalBricks = brickRows * brickColumns;
                map = new BrickGenerator(brickRows, brickColumns);

                
                

                repaint();
            }
        }

        if (key == KeyEvent.VK_1 && justWon){
            if (lives < 4) {
                lives++;
                justWon = false;
            }
        }

        if (key == KeyEvent.VK_2 && justWon){
            justWon = false;
            longPaddle = true;
        }

        if (key == KeyEvent.VK_3 && justWon){
            justWon = false;
            multiGamesLeft = 2;
        }


        if (key == KeyEvent.VK_ESCAPE) System.exit(0);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) left = false;
        if (key == KeyEvent.VK_D) right = false;
        if (key == KeyEvent.VK_F && speedMulti < 5) f_Pressable = true;
    }

    @Override public void keyTyped(KeyEvent e) {}

}
