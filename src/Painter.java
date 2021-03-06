import entities.Entity;
import entities.LivingEntity;
import util.Coord;
import util.Resource;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Painter {

    private Game game;
    private int appW;
    private int appH;
    private int visibleMapWidth;
    private int visibleMapHeight;
    private int hCent;
    private int vCent;
    private int squareSize;

    public Painter(Game game, int appW, int appH) {
        this.game = game;
        this.appW = appW;
        this.appH = appH;
        this.visibleMapWidth = Resource.visibleMapWidth;
        this.visibleMapHeight = Resource.visibleMapHeight;
        this.hCent = visibleMapWidth / 2;
        this.vCent = visibleMapHeight / 2;
        this.squareSize = Math.min(appW / visibleMapWidth, appH / visibleMapHeight);
    }

    public void paintVisibleMap(Graphics g, int frameCount, int steps) {
        List<Runnable> executeLater = new ArrayList<>();
        int xShift = 0;
        int yShift = 0;
        int movementShift = (squareSize * steps / Resource.maxSteps);
        if (game.getPlayer().isMoving()) {
            Coord shift = game.getPlayer().getDirection();
            xShift = movementShift * shift.x;
            yShift = movementShift * shift.y;
        }

        for (int x = -1; x < visibleMapWidth + 1; x++) {
            for (int y = -1; y < visibleMapHeight + 1; y++) {
                Coord c = (new Coord(x, y)).virtualToReal(game.getPlayer().pos, hCent, vCent);
                if (game.getMap().isValidCoord(c)) {
                    game.getMap().see(c);
                    int X = x * squareSize - xShift;
                    int Y = y * squareSize - yShift;
                    if (game.getMap().walkableMap[c.x][c.y] == 1) {
                        drawSquare(g, X, Y, Color.GRAY);
                    } else {
                        drawSquare(g, X, Y, Color.LIGHT_GRAY);
                    }
                    Entity occupier = game.getMap().entityMap.get(c);
                    if (occupier != null) {
                        if (occupier.isLivingEntity()) {
                            LivingEntity entity = (LivingEntity) occupier;
                            if (entity.isMoving()) {
                                Coord shift = entity.getDirection();
                                X += movementShift * shift.x;
                                Y += movementShift * shift.y;
                            }
                        }
                        int finX = X;
                        int finY = Y;
                        if (occupier.isPlayer()) {
                            executeLater.add(() -> drawSquare(g, finX, finY, Color.RED.darker().darker()));
                        }
                        if (occupier.isAlly()) {
                            executeLater.add(() -> drawSquare(g, finX, finY, Color.GREEN.darker().darker()));
                        }
                        if (occupier.isEnemy()) {
                            executeLater.add(() -> drawSquare(g, finX, finY, Color.BLUE.darker().darker()));
                        }
                        if (occupier.isStairs()) {
                            executeLater.add(() -> drawSquare(g, finX, finY, Color.YELLOW.darker().darker()));
                        }
                    }
                }
            }
        }
        for (Runnable r : executeLater) {
            r.run();
        }
        g.setColor(Color.GREEN);
        int y = 20;
        g.drawString("Player HP: " + game.getPlayer().getStats().health, 0, y);
        y += 15;
        for (LivingEntity l : game.allies) {
            g.drawString("Ally HP: " + l.getStats().health, 0, y);
            y += 15;
        }
        for (LivingEntity l : game.enemies) {
            g.drawString("Enemy HP: " + l.getStats().health, 0, y);
            y += 15;
        }
    }

    private void drawSquare(Graphics g, int x, int y, Color col) {
        g.setColor(col);
        g.fillRect(x, y, squareSize, squareSize);
    }
}
