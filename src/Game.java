/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

/**
 *
 * @author branc2347
 */
// make sure you rename this class if you are doing a copy/paste
public class Game extends JComponent implements KeyListener {

    // Height and Width of game
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static int entireWidth = 10000; // actual width of the game (not the window)
    public static int entireHeight = 7500;// actual heigh of the game (not the window)
    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;
    //movement variables
    boolean p1Up = false; // up 
    boolean p1Down = false; // down
    boolean p1Right = false; // right
    boolean p1Left = false; // left
    //size boost variables.. debug mode
    boolean p1Add = false; // add width+height p1
    boolean p1Minus = false; // subtract width+height p1
    boolean p2Add = false; // add width+height p2
    boolean p2Minus = false;// subtract width+height p2
    // character variables
    static final double playerWidth = 20; // constant of default player width/height
    static final int defaultSpeed = 4; // initial player speed
    Player player1 = new Player(400, 400, playerWidth, playerWidth); // player 1
    Player player2 = new Player(300, 300, playerWidth, playerWidth); // test player(scope of game is multiplayer)
    // Menu, Logic, Death - screens
    int screen = 0; //variable to determine the current screen. starts at beginning
    final int startScreen = 0; // specifies start screen
    final int gameScreen = 1; // specifies game screen
    final int endScreen = 2; // specifies end screen
    boolean enterPressed = false; // tests whether enter is pressed @ startScreen
    public String playerName = ""; // user input, player name
    Font playerFont = new Font(null, Font.BOLD, 20); // font for text
    int textScroll = 0; // endScreen scrolling int
    // Random for objects
    Random random = new Random(); // used for generating food
    //food gen variables
    int amountFood = 20000; // amount of food to be generated
    Food[] food = new Food[amountFood]; //array storing all food created
    final int foodWidth = 4; // constant used for both width and height because it's a circle
    int[] foodTimer = new int[amountFood]; // timer to respawn food after a certain time period
    //camera correction + zoom
    static final double defaultZoom = 4; // default zoom on on p1
    static double zoomFactor = defaultZoom; // factor that changes according to p1.width
    static double camWidth = WIDTH / zoomFactor; // the width of the camera using zoom
    static double camHeight = HEIGHT / zoomFactor; // the height of the camera using zoom
    double camx = (player1.getCenterX() - camWidth / 2.0); // x component of camera
    double camy = (player1.getCenterY() - camHeight / 2.0); // y component of camera

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g; // implements 2D graphics, to draw Doubles
        g2.clearRect(0, 0, entireWidth, entireHeight); // clear screen

        //if startScreen ( beginning of the game )
        if (screen == startScreen) {
            g2.setColor(Color.BLACK); // background colour 
            g2.fillRect(0, 0, WIDTH, HEIGHT); // fill background
            g2.setColor(Color.WHITE); //text colorur
            g2.setFont(playerFont); // text font
            drawCenteredString(g2, "Please enter your player name:", WIDTH / 2, WIDTH / 4, HEIGHT / 2); // instructional string drawn
            drawCenteredString(g2, playerName, WIDTH / 2, WIDTH / 4, HEIGHT / 2 + 30); // user input drawn

        } else if (screen == gameScreen) {
            //scale or zoom in
            g2.scale(zoomFactor, zoomFactor);

            // GAME DRAWING GOES HERE 
            //draw player 1, player 2, and food
            player2.draw(g2, camx, camy, Player.russianPlayer, "");
            player1.draw(g2, camx, camy, Player.obamaPlayer, playerName);
            for (Food f : food) {
                f.draw(g2, camx, camy);
            }
        } else if (screen == endScreen) {
            // draw end screen
            g2.clearRect(0, 0, entireWidth, entireHeight);
            g2.setFont(playerFont);
            g2.drawString("THANKS OBAMA ...", (int) camWidth + textScroll, (int) camHeight / 2);
            g2.drawString(" my name is Reek ", (int) camWidth - (int) camWidth * 4 + textScroll, (int) camHeight);
        }
    }

    //draws any string using the word idea of 'centering text'
    private void drawCenteredString(Graphics2D g2, String string, int width, int XPos, int YPos) {
        int stringLen = (int) g2.getFontMetrics().getStringBounds(string, g2).getWidth(); // find length of string based on font
        int start = width / 2 - stringLen / 2; //set the start position of drawing string
        g2.drawString(string, start + XPos, YPos); // center string
    }
    // The main game loop
    // In here is where all the logic for my game will go

    public void run() {
        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;
        long deltaTime;

        // the main game loop section
        // game will end if you set done = false;

        genFood(amountFood); // generate food location once, out of the loop

        boolean done = false;
        while (!done) {
            // determines when  started to keep a framerate

            startTime = System.currentTimeMillis();
            if (screen == startScreen) { // if start screen, beginning of game
            } else if (screen == gameScreen) { //if game screen do gameLogic
                gameLogic(); // gameLogic method 
            } else if (screen == endScreen) { 
                // if it's the end screen, reset player variables and scroll text
                player1.width = playerWidth;
                player1.height = playerWidth;
                player1.x = 400;
                player2.x = 300;
                player2.width = playerWidth;
                player2.height = playerWidth;
                textScroll += 1;
            }
            // update the drawing (calls paintComponent)
            repaint();
            // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
            // USING SOME SIMPLE MATH
            deltaTime = System.currentTimeMillis() - startTime;
            if (deltaTime > desiredTime) {
                //took too much time, don't wait
            } else {
                try {
                    Thread.sleep(desiredTime - deltaTime);
                } catch (Exception e) {
                };
            }
        }
    }

    void gameLogic() {
        // GAME LOGIC STARTS HERE 

        if (p1Add) { // adding width/height p1
            player1.width += 2;
            player1.height += 2;
            player1.x -= 1;
            player1.y -= 1;
        }
        if (p1Minus) { // subtracting width/height p1
            player1.width -= 2;
            player1.height -= 2;
            player1.x += 1;
            player1.y += 1;
        }
        if (p2Add) { // adding width/height p2
            player2.width += 2;
            player2.height += 2;
            player2.x -= 1;
            player2.y -= 1;
        }
        if (p2Minus) { // subtracting width/height p2
            player2.width -= 2;
            player2.height -= 2;
            player2.x += 1;
            player2.y += 1;
        }
        if (player2.width < 0 || player2.height < 0) { // does not allow p2 width/height below 0
            player2.width = 0;
            player2.height = 0;
        }
        if (player1.width < 1 || player1.height < 1) { // does not allow p1 width/height below 0
            player1.width = 0;
            player1.height = 0;
        }
        handleCollisionPlayer(player1, player2); // call collision method for players
        handleCollisionFood(player1, food); // call collision method for food vs player1
        handleCollisionFood(player2, food); // call collision method for food vs player2
        foodRespawn(player1, food); // respawn method in accordance to p1
        foodRespawn(player2, food); // respawn method in accordance to p2

        //updates variables
        player1.scaleFactor(); // sets the speed, zoom factor, and text zoom based on p1.width
        player1.move(p1Up, p1Left, p1Down, p1Right); // movement variables of p1
        camWidth = WIDTH / zoomFactor; //updates camWidth
        camHeight = HEIGHT / zoomFactor; //updates camHeight
        camx = (player1.getCenterX() - camWidth / 2.0); //updates x
        camy = (player1.getCenterY() - camHeight / 2.0); //updates y
        setBoundaries(player1, entireWidth, entireHeight);
        // game ends if player 1 is dead
        if (player1.width < playerWidth && player1.height < playerWidth) {
            screen = 2;
        }
        // GAME LOGIC ENDS HERE 
    }

    public void foodRespawn(Player player, Rectangle[] food) {
// for each food, if player does not contain the food and the timer array ran out, respawn food
        for (int i = 0; i < food.length; i++) {

            if (!player.contains(food[i].x, food[i].y) && foodTimer[i] < 1) { //actual respawn of food
                // if player does not contain the food, and the timer has expired
                food[i].width = foodWidth; // reset width and height to original value
                food[i].height = foodWidth;
                foodTimer[i] = 15 * 60; // reset timer
            }
            if (food[i].width < 0) { // subtract time from food timer, to respawn after 15 sec
                if (foodTimer[i] > 0) {
                    foodTimer[i]--;
                }
            }
        }
    }

    public void handleCollisionPlayer(Player p1, Player p2) {
        int xDiff = (int) (p1.getCenterX() - p2.getCenterX()); // 'a' side of right triangle, difference in x position
        int yDiff = (int) (p1.getCenterY() - p2.getCenterY()); // 'b' side of right triangle, difference in y position
        double p1Radius = p1.width / 2; // p1 radius
        double p2Radius = p2.width / 2; // p2 radius
        double centerDiff = (p1Radius + p2Radius); // 'c' side of right triangle, distance from center to center

        //if the side of one circle is past the center of the other circle
        if (Math.sqrt(xDiff * xDiff + yDiff * yDiff) <= centerDiff - p2Radius
                || Math.sqrt(xDiff * xDiff + yDiff * yDiff) <= centerDiff - p1Radius) {
            //if player 1 is bigger than player 2 by a factor of 1.1
            if (0.9 * p1.width >= p2.width) {
                //if p2 has a height and width
                if (p2.height > 0 && p2.width > 0) {
                    //add width and height to p1, subtract from p2
                    // add/subtract to x/y to ensure center of circle does not move. smooth animation
                    p1.width += 2;
                    p1.x -= 1;
                    p1.height += 2;
                    p1.y -= 1;
                    p2.height -= 4;
                    p2.y += 2;
                    p2.width -= 4;
                    p2.x += 2;
                }
                // if player 2 is bigger than p1
            } else if (0.9 * p2.width >= p1.width) {
                // if p1 has a height and width 
                if (p1.height > 0 && p1.width > 0) {
                    //add width and height to p2, subtract from p1
                    // add/subtract to x/y to ensure center of circle does not move. smooth animation
                    p2.width += 2;
                    p2.x -= 1;
                    p2.height += 2;
                    p2.y -= 1;
                    p1.height -= 4;
                    p1.y += 2;
                    p1.width -= 4;
                    p1.x += 2;
                }
            }
        }

    }

    public void handleCollisionFood(Player player, Rectangle[] food) {
        // same as player collision, but is applied to each food object. Uses pythagorean to find distance, and if its over the centre
        // slowly increase player size, remove food
        for (Rectangle f : food) {
            int xDiff = (int) (player.getCenterX() - f.getCenterX());
            int yDiff = (int) (player.getCenterY() - f.getCenterY());
            double p1Radius = player.width / 2;
            int p2Radius = f.width / 2;
            double centerDiff = (p1Radius + p2Radius);
            if (Math.sqrt(xDiff * xDiff + yDiff * yDiff) <= centerDiff - p2Radius && f.width > 0) {
                player.width += f.width / 2;
                player.x -= f.width / 4;
                player.height += f.height / 2;
                player.y -= f.height / 4;
                f.height -= 2 * f.height;
                f.width -= 2 * f.width;
                repaint();
            }
        }
    }

    public void setBoundaries(Player player1, int width, int height) {
        //if player moves past left side
        if (player1.x < 0) {
            player1.x = 0;
        }
        //if player moves past right side
        if (player1.x + player1.width > width) {
            player1.x = width - player1.width;
        }
        //if player moves past top
        if (player1.y < 0) {
            player1.y = 0;
        }
        //if player moves past bottom
        if (player1.y + player1.height > height) {
            player1.y = height - player1.height;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates a windows to show my game
        JFrame frame = new JFrame("My Game");

        // creates an instance of my game
        Game game = new Game();
        // sets the size of my game
        game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(game);

        // sets some options and size of the window automatically
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);
        frame.addKeyListener(game);
        // starts my game loop
        game.run();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (screen == startScreen) {
            // if it is on  start screen
            // take any chacter between A - z and draw it on screen. User input
            if (e.getKeyChar() >= 'A' && e.getKeyChar() <= 'z' || e.getKeyChar() == ' ') {
                playerName += e.getKeyChar(); // Typing player name
            } else if (key == KeyEvent.VK_BACK_SPACE && playerName.length() != 0) { //deleting last char
                playerName = playerName.substring(0, playerName.length() - 1);
            } else if (key == KeyEvent.VK_ENTER) {
                screen = 1; // name is done
            }
        } else if (screen == gameScreen) {
            // if on game screen
            //movement, up, down, left, right
            //debug : adding width/height, subtracting width/height
            if (key == KeyEvent.VK_W) {
                p1Up = true;
            }
            if (key == KeyEvent.VK_S) {
                p1Down = true;
            }
            if (key == KeyEvent.VK_A) {
                p1Left = true;
            }
            if (key == KeyEvent.VK_D) {
                p1Right = true;
            }
            if (key == KeyEvent.VK_P) {
                p1Add = true;
            }
            if (key == KeyEvent.VK_O) {
                p1Minus = true;
            }
            if (key == KeyEvent.VK_L) {
                p2Add = true;
            }
            if (key == KeyEvent.VK_K) {
                p2Minus = true;
            }
        } else if (screen == endScreen) {
            //if end screen, press space to restart game from startScreen
            if (key == KeyEvent.VK_SPACE) {
                screen = 0;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        //same as above, but release the movement and size adding/subtracting
        if (key == KeyEvent.VK_W) {
            p1Up = false;
        }
        if (key == KeyEvent.VK_S) {
            p1Down = false;
        }
        if (key == KeyEvent.VK_A) {
            p1Left = false;
        }
        if (key == KeyEvent.VK_D) {
            p1Right = false;
        }
        if (key == KeyEvent.VK_P) {
            p1Add = false;
        }
        if (key == KeyEvent.VK_O) {
            p1Minus = false;
        }
        if (key == KeyEvent.VK_L) {
            p2Add = false;
        }
        if (key == KeyEvent.VK_K) {
            p2Minus = false;
        }

    }

    public void genFood(int amountFood) {
        // generates the food, first creating the food, then assigning it a random x, y point. 
        // creates and sets the timer
        for (int i = 0; i < amountFood; i++) {
            food[i] = new Food(1, 1, foodWidth, foodWidth);
            food[i].x = random.nextInt(Game.entireWidth - foodWidth) + 1;
            food[i].y = random.nextInt(Game.entireHeight - foodWidth) + 1;
            foodTimer[i] = 15 * 60;
        }
    }
}
