package appjava.raw;
import javax.swing.*;
import java.awt.*;

public class layoutBorder {

    {
        JButton btn1 = new JButton("North");
        JButton btn2 = new JButton("East");
        JButton btn3 = new JButton("West");
        JButton btn4 = new JButton("South");
        JButton btn5 = new JButton("Center");
        
        JFrame frame = new JFrame("Border Layout Management Example");
        
        // (new BorderLayout(gap-x, gap-y);
        frame.setLayout(new BorderLayout(20,20));
        
        frame.add(btn1, BorderLayout.NORTH);
        frame.add(btn2, BorderLayout.EAST);
        frame.add(btn3, BorderLayout.WEST);
        frame.add(btn4, BorderLayout.SOUTH);
        frame.add(btn5, BorderLayout.CENTER);
        
        frame.setVisible(true);
        frame.setSize(400,400);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
        // TODO code application logic here
        new layoutBorder();
    }
    
}
