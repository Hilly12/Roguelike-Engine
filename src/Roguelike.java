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
}
