package brickBreaker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePlay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int delay = 8;
    private int playerX = 310;
    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;
    private MapGenerator map;
    private JButton restartButton;

    public GamePlay() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();

        restartButton = new JButton("Restart");
        restartButton.setBounds(250, 400, 200, 50);
        restartButton.setFocusable(false);
        restartButton.setFont(new Font("serif", Font.BOLD, 20));
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        restartButton.setVisible(false);
        add(restartButton);

        setLayout(null); // Use null layout for absolute positioning
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Draw Background Gradient with Stars
        g2d.setPaint(new GradientPaint(0, 0, new Color(0, 0, 20), getWidth(), getHeight(), new Color(0, 0, 80)));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw Stars
        g2d.setColor(Color.white);
        drawStars(g2d);

        // Draw Map
        map.draw(g2d);

        // Draw Borders with Gradient Effect
        g2d.setColor(new Color(255, 215, 0)); // Gold
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(0, 0, getWidth(), 0); // Top border
        g2d.drawLine(0, 0, 0, getHeight()); // Left border

        g2d.setColor(new Color(255, 165, 0)); // Orange
        g2d.drawLine(getWidth(), 0, getWidth(), getHeight()); // Right border
        g2d.drawLine(0, getHeight(), getWidth(), getHeight()); // Bottom border

        // Draw Scores with Glowing Effect
        g2d.setFont(new Font("Arial", Font.BOLD, 25));
        drawGlowingText(g2d, "Score: " + score, getWidth() - 150, 30, Color.cyan, 2);

        // Draw Paddle with Rainbow Gradient
        drawRainbowPaddle(g2d);

        // Draw Ball with Glowing Effect
        drawGlowingBall(g2d);

        // Check if all bricks are destroyed
        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;

            // Draw You Won Text with Animation
            drawYouWonText(g2d);

            // Draw Restart Button with Animation
            drawAnimatedRestartButton(g2d);
        } else if (ballposY > getHeight() - 20) { // Check if ball is out of bounds
            play = false;
            ballXdir = 0;
            ballYdir = 0;

            // Draw Game Over Text with Animation
            drawGameOverText(g2d);

            // Draw Restart Button with Animation
            drawAnimatedRestartButton(g2d);
        } else {
            restartButton.setVisible(false); // Hide the button during play
        }
    }

    // Method to draw animated stars in the background
    private void drawStars(Graphics2D g2d) {
        for (int i = 0; i < 50; i++) {
            int x = (int) (Math.random() * getWidth());
            int y = (int) (Math.random() * getHeight());
            int size = (int) (Math.random() * 3) + 1;

            g2d.fillRect(x, y, size, size);
        }
    }

    // Method to draw glowing text
    private void drawGlowingText(Graphics2D g2d, String text, int x, int y, Color color, int thickness) {
        g2d.setColor(Color.black);
        for (int i = 0; i < thickness; i++) {
            g2d.drawString(text, x - i, y);
            g2d.drawString(text, x + i, y);
            g2d.drawString(text, x, y - i);
            g2d.drawString(text, x, y + i);
        }

        g2d.setColor(color);
        g2d.drawString(text, x, y);
    }

    // Method to draw rainbow gradient paddle
    private void drawRainbowPaddle(Graphics2D g2d) {
        GradientPaint rainbowGradient = new GradientPaint(playerX, 550, Color.red, playerX + 100, 550, Color.blue);
        g2d.setPaint(rainbowGradient);
        g2d.fillRect(playerX, 550, 100, 8);
    }

    // Method to draw glowing ball
    private void drawGlowingBall(Graphics2D g2d) {
        g2d.setColor(Color.white);
        g2d.fillOval(ballposX - 10, ballposY - 10, 40, 40);
        GradientPaint ballGradient = new GradientPaint(ballposX, ballposY, Color.yellow, ballposX + 20, ballposY + 20, Color.orange);
        g2d.setPaint(ballGradient);
        g2d.fillOval(ballposX, ballposY, 20, 20);
    }

    // Method to draw game over text with animation
    private void drawGameOverText(Graphics2D g2d) {
        g2d.setColor(new Color(255, 51, 51)); // Light Red
        g2d.setFont(new Font("Arial", Font.BOLD, 60));
        String gameOverText = "Game Over!";
        int gameOverTextWidth = g2d.getFontMetrics().stringWidth(gameOverText);
        g2d.drawString(gameOverText, getWidth() / 2 - gameOverTextWidth / 2, getHeight() / 2 - 50);

        g2d.setColor(Color.white);
        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        String scoreText = "Score: " + score;
        int scoreTextWidth = g2d.getFontMetrics().stringWidth(scoreText);
        g2d.drawString(scoreText, getWidth() / 2 - scoreTextWidth / 2, getHeight() / 2);
    }

    // Method to draw "You Won" text with animation
    private void drawYouWonText(Graphics2D g2d) {
        g2d.setColor(new Color(0, 204, 102)); // Light Green
        g2d.setFont(new Font("Arial", Font.BOLD, 60));
        String youWonText = "You Won!";
        int youWonTextWidth = g2d.getFontMetrics().stringWidth(youWonText);
        g2d.drawString(youWonText, getWidth() / 2 - youWonTextWidth / 2, getHeight() / 2 - 50);

        g2d.setColor(Color.white);
        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        String scoreText = "Score: " + score;
        int scoreTextWidth = g2d.getFontMetrics().stringWidth(scoreText);
        g2d.drawString(scoreText, getWidth() / 2 - scoreTextWidth / 2, getHeight() / 2);
    }

    // Method to draw animated restart button
    private void drawAnimatedRestartButton(Graphics2D g2d) {
        int buttonX = getWidth() / 2 - 100;
        int buttonY = getHeight() / 2 + 50;
        int buttonWidth = 200;
        int buttonHeight = 50;
        int arcWidth = 15;
        int arcHeight = 15;

        // Draw Button Background with Gradient and Animation
        for (int i = 0; i <= 255; i += 5) {
            Color buttonColor = new Color(30, 144, 255, i);
            GradientPaint buttonGradient = new GradientPaint(0, 0, buttonColor, buttonWidth, buttonHeight, new Color(0, 0, 255));
            g2d.setPaint(buttonGradient);
            g2d.fillRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, arcWidth, arcHeight);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Draw Button Text with Animation
        g2d.setColor(Color.white);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        String buttonText = "Restart";
        int buttonTextWidth = g2d.getFontMetrics().stringWidth(buttonText);
        g2d.drawString(buttonText, buttonX + buttonWidth / 2 - buttonTextWidth / 2, buttonY + 30);

        // Draw Button Border
        g2d.setColor(new Color(0, 0, 139));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, arcWidth, arcHeight);

        restartButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        restartButton.setVisible(true); // Show the button when the game is over
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            // Game logic for ball movement and collision detection
            ballposX += ballXdir;
            ballposY += ballYdir;

            if (ballposX < 0 || ballposX > 670) {
                ballXdir = -ballXdir;
            }

            if (ballposY < 0) {
                ballYdir = -ballYdir;
            }

            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir;
            }

            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }

                            break A;
                        }
                    }
                }
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                restartGame();
            }
        }
    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }

    private void restartGame() {
        play = true;
        ballposX = 120;
        ballposY = 350;
        ballXdir = -1;
        ballYdir = -2;
        playerX = 310;
        score = 0;
        totalBricks = 21;
        map = new MapGenerator(3, 7);
        repaint();
        restartButton.setVisible(false);
    }

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

    // Implement other KeyListener methods if necessary
}
