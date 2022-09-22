import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.lang.*;


public class FrameEngine {
    static Graphics g;
    static int width = 500, height = 500;
    static ArrayList<GameObject> playerArray = new ArrayList<GameObject>();
    static ArrayList<GameObject> objects = new ArrayList<GameObject>();
    static GameObject player;
    static int playerSpeed = 4;
    static int lastInput;
    static boolean death = false;
    public static void main(String[] args) {
        // create a DrawingPanel object
        DrawingPanel panel = new DrawingPanel(width, height);

        // creates player snake
        playerArray.add(player = new GameObject(100, 200, 20, 20, Color.GREEN));
        playerArray.add(new GameObject(80,200,20,20, Color.GREEN));
        playerArray.add(new GameObject(60,200,20,20,Color.GREEN));

        for(GameObject object : playerArray) {
            objects.add(object);
        }

        // set up listeners
        KeyListener listener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (lastInput == -1) {
                    if (!player.equalsSpeed(new Vector(0, 0)) && e.getKeyCode() == KeyEvent.VK_W && !player.equalsSpeed(new Vector(0, playerSpeed))) {
                        lastInput = KeyEvent.VK_W;
                        //player.setSpeed(new Vector(0, -playerSpeed));
                    }
                    else if (!player.equalsSpeed(new Vector(0, 0)) && e.getKeyCode() == KeyEvent.VK_S && !player.equalsSpeed(new Vector(0, -playerSpeed))) {
                        lastInput = KeyEvent.VK_S;
                        //player.setSpeed(new Vector(0, playerSpeed));
                    }
                    else if (!player.equalsSpeed(new Vector(0, 0)) && e.getKeyCode() == KeyEvent.VK_A && !player.equalsSpeed(new Vector(playerSpeed, 0))) {
                        lastInput = KeyEvent.VK_A;
                        //player.setSpeed(new Vector(-playerSpeed, 0));
                    }
                    else if (e.getKeyCode() == KeyEvent.VK_D && !player.equalsSpeed(new Vector(-playerSpeed, 0))) {
                        lastInput = KeyEvent.VK_D;
                        //player.setSpeed(new Vector(playerSpeed, 0));
                    }
                }
                //System.out.println("keyPressed=" + KeyEvent.getKeyText(e.getKeyCode()));
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("keyReleased=" + KeyEvent.getKeyText(e.getKeyCode()));
            }
        };
        panel.addKeyListener(listener);
        panel.setVisible(true);

        // get the Graphics object from the DrawingPanel
        g = panel.getGraphics();

        // set up the frame loop
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(FrameEngine::frame, 0, 25, TimeUnit.MILLISECONDS);
    }

    public static void frame() {
        // reset the background and stops the removal of "You Lose"
        if(!death) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            g.setColor(Color.BLACK);
            for(int x = 0; x < 5; x++) {
                for(int y = 0; y < 5; y++) {
                    g.drawRect(x * 100, y * 100, 100, 100);
                }
            }
        }
        else {
            g.setFont(new Font("SansSerif", Font.BOLD, 36));
            g.drawString("You Lose!", 150, 250);
        }

        // draw the player
        for (GameObject object : playerArray) {
            if(player.outOfBounds()) {
                player.setSpeed(new Vector(0,0));
                death = true;
                break;
            }
            g.setColor(object.getColor());
            object.move(object.getSpeed());
            g.fillRect(object.getX(), object.getY(), object.getWidth(), object.getHeight());
        }

        if (player.getX() % 20 == 0 && player.getY() % 20 == 0) {
            Vector lastObjectSpeed = new Vector(0, 0);
            if (lastInput == KeyEvent.VK_W) {
                lastObjectSpeed = new Vector(0, -playerSpeed);
            }
            else if (lastInput == KeyEvent.VK_S) {
                lastObjectSpeed = new Vector(0, playerSpeed);
            }
            else if (lastInput == KeyEvent.VK_A) {
                lastObjectSpeed = new Vector(-playerSpeed, 0);
            }
            else if (lastInput == KeyEvent.VK_D) {
                lastObjectSpeed = new Vector(playerSpeed, 0);
            }

            for (GameObject object : playerArray) {
                if (lastObjectSpeed.equals(new Vector(0, 0))) {
                    lastObjectSpeed = player.getSpeed();
                }
                Vector nextSpeed = object.getSpeed();
                object.setSpeed(lastObjectSpeed);
                lastObjectSpeed = nextSpeed;
            }
            lastInput = -1;
        }
    }
}

class Vector{
    public int x, y;

    public Vector(int x, int y) {
        this.x = x;
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

    public GameObject(int x, int y, int w, int h) {
        this.pos = new Vector(x, y);
        this.size = new Vector(w, h);
        this.color = Color.BLACK;
        this.speed = new Vector(0,0);
    }

    public GameObject(int x, int y, int w, int h, Color color) {
        this.pos = new Vector(x, y);
        this.size = new Vector(w, h);
        this.color = color;
        this.speed = new Vector(0,0);
    }

    public int getX() {
        return this.pos.x;
    }

    public int getY() {
        return this.pos.y;
    }

    public void setX(int x) {
        this.pos.x = x;
    }

    public void setY(int y) {
        this.pos.y = y;
    }

    public void setPos(int x, int y) {
        this.pos.x = x;
        this.pos.y = y;
    }

    public int getWidth() {
        return this.size.x;
    }

    public int getHeight() {
        return this.size.y;
    }

    public void setWidth(int x) {
        this.size.x = x;
    }

    public void setHeight(int y) {
        this.size.y = y;
    }

    public void setSize(int x, int y) {
        this.size.x = x;
        this.size.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void move(int x, int y) {
        this.setPos(this.pos.x + x, this.pos.y + y);
    }

    public void move(Vector amt) {
        this.setPos(this.pos.x + amt.x, this.pos.y + amt.y);
    }

    public Vector getSpeed() {
        return speed;
    }

    public void setSpeed(Vector speed) {
        this.speed = speed;
    }

    public boolean equalsSpeed(Vector other) {
        return other.equals(this.speed);
    }

    public boolean outOfBounds() {
        if(this.pos.x >= FrameEngine.width || this.pos.x < 0) {
            return true;
        }
        else if (this.pos.y >= FrameEngine.height || this.pos.y < 0) {
            return true;
        }
        else {
            return false;
        }
    }
}
