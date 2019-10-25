import entities.Entity;
import util.Resource;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Board extends JPanel implements KeyListener {

    private static final long serialVersionUID = 1L;

    private int frameCount;

    private int appW;
    private int appH;

    private Game game;
    private Painter painter;
    private int steps;
    private boolean leftDown, rightDown, upDown, downDown;

    private Timer gameTimer;

    Board(JFrame F, int appW, int appH) {
        super();
        F.setFocusable(true);
        F.setSize(appW, appH);
        F.addKeyListener(this);
        this.setOpaque(false);
        this.appW = appW;
        this.appH = appH;
        frameCount = 0;
        util.Resource.init();
        newGame();
        initGameTimer();
    }

    private void newGame() {
        Resource.init();
        Entity.autoID = 0;
        game = new Game();
        painter = new Painter(game, appW, appH);
        game.InitializeMap();
        steps = 0;
    }

    private void initGameTimer() {
        gameTimer = new Timer(Resource.refreshRate, e -> {
            if (game.isIdle()) {
                game.StartMove(leftDown, rightDown, upDown, downDown);
            } else if (game.isAnimating()) {
                steps++;
                if (steps >= Resource.maxSteps) {
                    steps = 0;
                    game.FinishMove();
                    game.setIdle();
                }
            }
            frameCount++;
            repaint();
        });
        gameTimer.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.DARK_GRAY.darker().darker());
        g.fillRect(0, 0, appW, appH);

        painter.paintVisibleMap(g, frameCount, steps);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftDown = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightDown = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            upDown = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            downDown = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftDown = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightDown = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            upDown = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            downDown = false;
        }
    }
}
