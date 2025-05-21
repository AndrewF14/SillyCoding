import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Main {
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        JFrame frame = new JFrame();
        Breakout game = new Breakout();
        frame.setBounds((screenWidth / 2) - (Constants.frameWidth / 2), (screenHeight / 2) - (Constants.frameHeight / 2), Constants.frameWidth, Constants.frameHeight);
        frame.setTitle("Breakout");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);
        frame.setVisible(true);
    }
}
