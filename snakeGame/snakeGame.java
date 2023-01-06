import javax.swing.*;
import java.awt.*;

public class snakeGame {
    JFrame frame;
    snakeGame()
    {
        frame = new JFrame("Snake Board");
        frame.setBounds(10,10,905,700);
        myPanel panel = new myPanel();   // object of myPanel class
        panel.setBackground(Color.white);
        frame.add(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        snakeGame board = new snakeGame();   // creating object to call the snakeGame class
    }
}
