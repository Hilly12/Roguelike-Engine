import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Roguelike {
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static void main(String[] args) {

        JFrame F = new JFrame("Explorer Hat");
        F.setResizable(false);

        Board app = new Board(F, screenSize.width, screenSize.height);

        F.add(app, BorderLayout.CENTER);
        F.setVisible(true);

        F.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
//
//    public static void display(Game game, int w, int h) {
//        int hCent = w / 2;
//        int vCent = h / 2;
//        for (int y = -1; y < h + 1; y++) {
//            for (int x = -1; x < w + 1; x++) {
//                Coord c = (new Coord(x, y)).virtualToReal(game.getPlayer().pos, hCent, vCent);
//                if (game.getMap().isValidCoord(c)) {
//                    game.getMap().see(c);
//                    Entity occupee = game.getMap().entityMap.get(c);
//                    if (occupee == null) {
//                        switch (game.getMap().walkableMap[c.x][c.y]) {
//                            case 0:
//                                System.out.print("-");
//                                break;
//                            case 1:
//                                System.out.print("#");
//                                break;
//                            default:
//                                break;
//                        }
//                    } else {
//                        if (occupee.isLivingEntity()) {
//                            if (occupee.isPlayer()) {
//                                System.out.print("p");
//                            }
//                            if (occupee.isEnemy()) {
//                                System.out.print("e");
//                            }
//                            if (occupee.isStairs()) {
//                                System.out.print("s");
//                            }
//                        }
//                    }
////                    Entity occupee = game.getMap().entityMap.get(c);
////                    if (occupee != null) {
////                        if (occupee.isLivingEntity()) {
////                            LivingEntity entity = (LivingEntity) occupee;
////                            if (entity.isMoving()) {
////                                Coord shift = Direction.directionToCoord(entity.getDirection());
////                                X += movementShift * shift.x;
////                                Y += movementShift * shift.y;
////                            }
////                        }
////                        else {
////                            if (occupee instanceof WallInterface) {
////                                map.seeWall(c);
////                            } // else if (occupee instanceof Item) {
////                            // }
////                        }
////                        int finX = X;
////                        int finY = Y;
////                        scheduler.add(() -> occupee.paint(g, finX, finY, squareSize, squareSize, progress));
////                    }
////                    int X = x * squareSize - xShift;
////                    int Y = y * squareSize - yShift;
////                    g.drawImage(floorSprites.get(floorMap[r.x][r.y]),
////                            X, Y, squareSize, squareSize, null);
//                } else {
//                    System.out.print("*");
//                }
//            }
//            System.out.println();
//        }
//    }

}
