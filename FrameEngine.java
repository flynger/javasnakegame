import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FrameEngine {
    static int x, y = 0;
    static Graphics g;
    static int width = 400, height = 400;
    static ArrayList<GameObject> objects;
    static GameObject player;

    public static void main(String[] args) {
        // create a DrawingPanel object
        DrawingPanel panel = new DrawingPanel(width, height);

        // set up game objects
        objects = new ArrayList<GameObject>();
        objects.add(player = new GameObject(200, 200, 20, 20, Color.RED));

        // set up listeners
        KeyListener listener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    player.setSpeed(new Vector(0, -4));
                } if (e.getKeyCode() == KeyEvent.VK_S) {
                    player.setSpeed(new Vector(0, 4));
                } if (e.getKeyCode() == KeyEvent.VK_A) {
                    player.setSpeed(new Vector(-4, 0));
                } if (e.getKeyCode() == KeyEvent.VK_D) {
                    player.setSpeed(new Vector(4, 0));
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
        scheduledExecutorService.scheduleAtFixedRate(FrameEngine::frame, 0, 17, TimeUnit.MILLISECONDS);
    }

    public static void frame() {
        // reset the background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // draw the objects
        for (GameObject object : objects) {
            g.setColor(object.getColor());
            object.move(object.getSpeed());
            g.fillRect(object.getX(), object.getY(), object.getWidth(), object.getHeight());
        }

        // movement
//
//        x++;
//        y++;
//        g.setColor(Color.BLACK);
//        g.fillRect(x, y, 20, 20);
    }
}

class Vector {
    public int x, y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
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
}
