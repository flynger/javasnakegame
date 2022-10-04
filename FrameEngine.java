import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.lang.*;
import java.util.random.*;


public class FrameEngine {
    static JFrame panel;
    static Graphics g;
    static final int cellSize = 20;
    static final int width = 25 * cellSize;
    static final int height = 25 * cellSize;
    static final int xOffset = 8;
    static final int yOffset = 30;
    static ArrayList<GameObject> playerArray = new ArrayList<GameObject>();
    static ArrayList<GameObject> player2Array = new ArrayList<GameObject>();
    static ArrayList<GameObject> objects = new ArrayList<GameObject>();
    static GameObject player;
    static GameObject player2;
    static final int playerSpeed = cellSize / 10;
    static int lastPlayerInput;
    static int lastPlayer2Input;
    static boolean playerDeath;
    static boolean player2Death;
    public static void main(String[] args) {
        // create a DrawingPanel object
//        DrawingPanel panel = new DrawingPanel(width, height);
        panel = new JFrame("Snake");
        panel.setSize(new Dimension(width + 16, height + 38));
        panel.setLocation(710, 290);
        panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set up listeners
        KeyListener listener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (!playerDeath && lastPlayerInput == -1) {
                    if (e.getKeyCode() == KeyEvent.VK_W && player.getSpeed().getX() != 0) {
                        lastPlayerInput = KeyEvent.VK_W;
                    } else if (e.getKeyCode() == KeyEvent.VK_S && player.getSpeed().getX() != 0) {
                        lastPlayerInput = KeyEvent.VK_S;
                    } else if (e.getKeyCode() == KeyEvent.VK_A && player.getSpeed().getY() != 0) {
                        lastPlayerInput = KeyEvent.VK_A;
                    } else if (e.getKeyCode() == KeyEvent.VK_D && (player.getSpeed().getY() != 0 || player.equalsSpeed(new Vector(0, 0)))) {
                        lastPlayerInput = KeyEvent.VK_D;
                        if (player.equalsSpeed(new Vector(0, 0))) {
                            lastPlayer2Input = KeyEvent.VK_LEFT;
                        }
                    }
                } if (!player2Death && lastPlayer2Input == -1) {
                    if (e.getKeyCode() == KeyEvent.VK_UP && player2.getSpeed().getX() != 0) {
                        lastPlayer2Input = KeyEvent.VK_UP;
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN && player2.getSpeed().getX() != 0) {
                        lastPlayer2Input = KeyEvent.VK_DOWN;
                    } else if (e.getKeyCode() == KeyEvent.VK_LEFT && (player2.getSpeed().getY() != 0 || player2.equalsSpeed(new Vector(0, 0)))) {
                        if (player2.equalsSpeed(new Vector(0, 0))) {
                            lastPlayerInput = KeyEvent.VK_D;
                        }
                        lastPlayer2Input = KeyEvent.VK_LEFT;
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && player2.getSpeed().getY() != 0) {
                        lastPlayer2Input = KeyEvent.VK_RIGHT;
                    }
                }
                if ((playerDeath || player2Death) && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    restartGame();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };
        panel.addKeyListener(listener);
        panel.setVisible(true);

        // get the Graphics object from the DrawingPanel
        g = panel.getGraphics();
        g.setColor(new Color(238, 238, 238));
        g.fillRect(xOffset, yOffset, 500, 500);

        // creates player snake
        playerArray.add(player = new GameObject(100, 200, cellSize, cellSize, new Color(0, 220, 0)));
        playerArray.add(new GameObject(80, 200, cellSize, cellSize, Color.GREEN));
        playerArray.add(new GameObject(60, 200, cellSize, cellSize, Color.GREEN));

        // creates player2 snake
        player2Array.add(player2 = new GameObject(380, 200, cellSize, cellSize, new Color(0, 220, 220)));
        player2Array.add(new GameObject(400, 200, cellSize, cellSize, Color.CYAN));
        player2Array.add(new GameObject(420, 200, cellSize, cellSize, Color.CYAN));

        objects.add(Apple.getApple());
        objects.addAll(playerArray);
        objects.addAll(player2Array);

        // set up the frame loop
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> frame(), 0, 10, TimeUnit.MILLISECONDS);
    }

    public static void frame() {
        int justDied = 0;
        game:
        if (!playerDeath && !player2Death) {
            // update the player's speed
            if (player.getX() % cellSize == 0 && player.getY() % cellSize == 0) {
                // player 1
                Vector lastObjectSpeed = new Vector(0, 0);
                if (lastPlayerInput == KeyEvent.VK_W) {
                    lastObjectSpeed = new Vector(0, -playerSpeed);
                } else if (lastPlayerInput == KeyEvent.VK_S) {
                    lastObjectSpeed = new Vector(0, playerSpeed);
                } else if (lastPlayerInput == KeyEvent.VK_A) {
                    lastObjectSpeed = new Vector(-playerSpeed, 0);
                } else if (lastPlayerInput == KeyEvent.VK_D) {
                    lastObjectSpeed = new Vector(playerSpeed, 0);
                }
                lastPlayerInput = -1;
                for (GameObject object : playerArray) {
                    if (lastObjectSpeed.equals(new Vector(0, 0))) {
                        lastObjectSpeed = player.getSpeed();
                    }
                    Vector nextSpeed = object.getSpeed();
                    object.setSpeed(lastObjectSpeed);
                    lastObjectSpeed = nextSpeed;
                }
                // player 2
                lastObjectSpeed = new Vector(0, 0);
                if (lastPlayer2Input == KeyEvent.VK_UP) {
                    lastObjectSpeed = new Vector(0, -playerSpeed);
                } else if (lastPlayer2Input == KeyEvent.VK_DOWN) {
                    lastObjectSpeed = new Vector(0, playerSpeed);
                } else if (lastPlayer2Input == KeyEvent.VK_LEFT) {
                    lastObjectSpeed = new Vector(-playerSpeed, 0);
                } else if (lastPlayer2Input == KeyEvent.VK_RIGHT) {
                    lastObjectSpeed = new Vector(playerSpeed, 0);
                }
                lastPlayer2Input = -1;

                for (GameObject object : player2Array) {
                    if (lastObjectSpeed.equals(new Vector(0, 0))) {
                        lastObjectSpeed = player2.getSpeed();
                    }
                    Vector nextSpeed = object.getSpeed();
                    object.setSpeed(lastObjectSpeed);
                    lastObjectSpeed = nextSpeed;
                }
                for (GameObject object : playerArray) {
                    if (object != player) {
                        if (player.isTouching(object)) {
                            justDied += 1;
                        }
                    }
                    if (player2.isTouching(object)) {
                        if (object == player) {
                            if (player2.getSpeed().getX() * (player.getX() - player2.getX()) > 0) {
                                 justDied += 2;
                            } else if (player2.getSpeed().getY() * (player.getY() - player2.getY()) > 0) {
                                justDied += 2;
                            }
                        }
                        else justDied += 2;
                    }
                }
                for (GameObject object : player2Array) {
                    if (object != player2) {
                        if (player2.isTouching(object)) {
                            justDied += 2;
                        }
                    }
                    if (player.isTouching(object)) {
                        if (object == player2) {
                            if (player.getSpeed().getX() * (player2.getX() - player.getX()) > 0) {
                                justDied += 1;
                            } else if (player.getSpeed().getY() * (player2.getY() - player.getY()) > 0) {
                                justDied += 1;
                            }
                        }
                        else justDied += 1;
                    }
                }
                if (player.outOfBounds()) {
                    justDied += 1;
                }
                if (player2.outOfBounds()) {
                    justDied += 2;
                }
                if (justDied > 0) {
                    break game;
                }


                // }
                if (player.isTouching(Apple.getApple())) {
                    GameObject newSnakeCell;
                    playerArray.add(newSnakeCell = new GameObject(playerArray.get(playerArray.size() - 1).getX(), playerArray.get(playerArray.size() - 1).getY(), cellSize, cellSize, Color.GREEN));
                    objects.add(newSnakeCell);
                    Apple.getApple().randomizePosition();
                }
                if (player2.isTouching(Apple.getApple())) {
                    GameObject newSnakeCell;
                    player2Array.add(newSnakeCell = new GameObject(player2Array.get(player2Array.size() - 1).getX(), player2Array.get(player2Array.size() - 1).getY(), cellSize, cellSize, Color.CYAN));
                    objects.add(newSnakeCell);
                    Apple.getApple().randomizePosition();
                }
            }
            // reset the background and stops the removal of "You Lose"
            playerArray.get(playerArray.size() - 1).eraseTail();
            player2Array.get(player2Array.size() - 1).eraseTail();

            // draw all the objects
            for (GameObject object : objects) {
                g.setColor(object.getColor());
                object.move(object.getSpeed());
                g.fillRect(object.getX() + xOffset, object.getY() + yOffset, object.getWidth(), object.getHeight());
            }
        }
        playerDeath:
        if (justDied > 0) {
            String text = "Player " + (3 - justDied) + " won!";
            g.setFont(new Font("SansSerif", Font.BOLD, 36));
            if (justDied == 1) {
                g.setColor(Color.CYAN);
                playerDeath = true;
            } else if(justDied == 2) {
                g.setColor(Color.GREEN);
                player2Death = true;
            } else {
                playerDeath = true;
                player2Death = true;
                g.setColor(Color.YELLOW);
                text = "    Tie!";
            }
            g.drawString(text, 150 + xOffset, 250 + yOffset);
        }
    }

    public static void restartGame() {
        objects.removeAll(playerArray);
        objects.removeAll(player2Array);

        g.setColor(new Color(238, 238, 238));
        g.fillRect(xOffset, yOffset, 500, 500);

        // recreates player snake
        playerArray = new ArrayList<>();
        playerArray.add(player = new GameObject(100, 200, cellSize, cellSize, new Color(0, 220, 0)));
        playerArray.add(new GameObject(80, 200, cellSize, cellSize, Color.GREEN));
        playerArray.add(new GameObject(60, 200, cellSize, cellSize, Color.GREEN));

        // recreates player2 snake
        player2Array = new ArrayList<>();
        player2Array.add(player2 = new GameObject(380, 200, cellSize, cellSize, new Color(0, 220, 220)));
        player2Array.add(new GameObject(400, 200, cellSize, cellSize, Color.CYAN));
        player2Array.add(new GameObject(420, 200, cellSize, cellSize, Color.CYAN));

        objects.addAll(playerArray);
        objects.addAll(player2Array);
        Apple.getApple().setPos(240, 200);
        playerDeath = false;
        player2Death = false;
    }
}

class Vector {
    private int x, y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean equals(Vector other) {
        return this.x == other.x && this.y == other.y;
    }

    public String toString() {
        return "x: " + x + ",y: " + y;
    }
}

class GameObject {
    private Vector pos, size, speed;
    private Color color;

    public GameObject(int x, int y, int w, int h, Color color) {
        this.pos = new Vector(x, y);
        this.size = new Vector(w, h);
        this.color = color;
        this.speed = new Vector(0, 0);
    }

    public int getX() {
        return this.pos.getX();
    }

    public int getY() {
        return this.pos.getY();
    }

    public Vector getPos() {
        return this.pos;
    }

    public void setX(int x) {
        this.pos.setX(x);
    }

    public void setY(int y) {
        this.pos.setY(y);
    }

    public void setPos(int x, int y) {
        this.pos.setX(x);
        this.pos.setY(y);
    }

    public void setPos(Vector pos) {
        this.pos = pos;
    }

    public int getWidth() {
        return this.size.getX();
    }

    public int getHeight() {
        return this.size.getY();
    }

    public void setWidth(int x) {
        this.size.setX(x);
    }

    public void setHeight(int y) {
        this.size.setY(y);
    }

    public void setSize(int x, int y) {
        this.size.setX(x);
        this.size.setY(y);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void move(int x, int y) {
        this.setPos(this.pos.getX() + x, this.pos.getY() + y);
    }

    public void move(Vector amt) {
        this.setPos(this.pos.getX() + amt.getX(), this.pos.getY() + amt.getY());
    }

    public Vector getSpeed() {
        return speed;
    }

    public void setSpeed(Vector speed) {
        this.speed = speed;
    }

    public boolean equalsSpeed(Vector other) {
        return this.speed.equals(other);
    }

    public boolean isTouching(GameObject other) {
        return this.getX() + this.getSpeed().getX() * FrameEngine.cellSize / FrameEngine.playerSpeed == other.getX() + other.getSpeed().getX() * FrameEngine.cellSize / FrameEngine.playerSpeed && this.getY() + this.getSpeed().getY() * FrameEngine.cellSize / FrameEngine.playerSpeed == other.getY() + other.getSpeed().getY() * FrameEngine.cellSize / FrameEngine.playerSpeed;
    }

    // public boolean isTouching(GameObject other) {
    // int leftX = this.getX(), rightX = this.getX() + getWidth();
    // int otherLeftX = other.getX(), otherRightX = other.getX() + other.getWidth();
    // int upY = this.getY(), downY = this.getY() + getHeight();
    // int otherUpY = other.getY(), otherDownY = other.getY() + other.getHeight();
    // return ((rightX > otherLeftX && rightX <= otherRightX)||(leftX < otherRightX
    // && leftX >= otherLeftX)) && ((downY > otherUpY && downY <= otherDownY)||(upY
    // < otherDownY && upY >= otherUpY));
    // }

    public boolean outOfBounds() {
        if ((this.pos.getX() == FrameEngine.width - getWidth() && this.getSpeed().getX() > 0) || (this.pos.getX() == 0 && this.getSpeed().getX() < 0)) {
            return true;
        } else if ((this.pos.getY() == FrameEngine.height - getHeight() && this.getSpeed().getY() > 0) || (this.pos.getY() == 0 && this.getSpeed().getY() < 0)) {
            return true;
        }
        return false;
    }

    public void eraseTail() {
        FrameEngine.g.setColor(new Color(238, 238, 238));
        int width = FrameEngine.cellSize, height = FrameEngine.cellSize, x = this.getX(), y = this.getY();
        if (this.getSpeed().getX() != 0) {
            width = FrameEngine.playerSpeed;
            if (this.getSpeed().getX() < 0) {
                x = this.getX() + FrameEngine.cellSize - FrameEngine.playerSpeed;
            }
            FrameEngine.g.fillRect(x + 8, y + 30, width, height);
        } else if (this.getSpeed().getY() != 0) {
            height = FrameEngine.playerSpeed;
            if (this.getSpeed().getY() < 0) {
                y = this.getY() + FrameEngine.cellSize - FrameEngine.playerSpeed;
            }
            FrameEngine.g.fillRect(x + FrameEngine.xOffset, y + FrameEngine.yOffset, width, height);
        }
    }
}

class Apple extends GameObject {

    private static Apple single_instance = null;

    private Apple(int x, int y, int w, int h) {
        super(x, y, w, h, Color.RED);
    }

    public Vector getRandomPos() {
        return new Vector((int) (Math.random() * (FrameEngine.width / getApple().getWidth())) * getApple().getWidth(), (int) (Math.random() * (FrameEngine.height / getApple().getHeight())) * getApple().getHeight());
    }

    public void randomizePosition() {
        Vector randomPos = getRandomPos();
        for(int i = 0; i < FrameEngine.objects.size(); i++) {
            if (FrameEngine.objects.get(i).getPos().equals(randomPos)) {
                randomPos = getRandomPos();
                i = 0;
            }
        }
        super.setPos(randomPos);
    }

    public static Apple getApple() {
        if (single_instance == null)
            single_instance = new Apple(240, 200, 20, 20);

        return single_instance;
    }
}
