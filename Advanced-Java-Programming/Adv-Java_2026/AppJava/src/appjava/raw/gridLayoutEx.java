package appjava.raw;
import javax.swing.*;
import java.awt.*;

public class gridLayoutEx {

    {
        JButton btn1 = new JButton("1");
        JButton btn2 = new JButton("2");
        JButton btn3 = new JButton("3");
        JButton btn4 = new JButton("4");
        JButton btn5 = new JButton("5");
        JButton btn6 = new JButton("6");
        JButton btn7 = new JButton("7");
        JButton btn8 = new JButton("8");
        JButton btn9 = new JButton("9");
        
        JFrame frame = new JFrame("Border Layout Management Example");
        
        frame.add(btn1); frame.add(btn2); frame.add(btn3);
        frame.add(btn4); frame.add(btn5); frame.add(btn6);
        frame.add(btn7); frame.add(btn8); frame.add(btn9);
        
        // frame.setLayout(new GridLayout(3,2));
        
        // (new GridLayout(row, column, gap-x, gap-y)
        frame.setLayout(new GridLayout(3, 3, 5, 5));
        frame.setVisible(true);
        frame.setSize(400,400);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
        // TODO code application logic here
        new gridLayoutEx();
    }
    
}
