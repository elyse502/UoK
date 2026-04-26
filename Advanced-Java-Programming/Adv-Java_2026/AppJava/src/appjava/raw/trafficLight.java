package appjava.raw;
import java.awt.*;
import javax.swing.*;

public class trafficLight {

    {
        JFrame frame = new JFrame("Traffic Example");
        JPanel panel1 = new JPanel();
        
        panel1.setBounds(20, 20, 20, 20);
        panel1.setBackground(Color.BLACK);
        
        for(int i = 0; i <= 12; i++) {
            panel1.setBackground(Color.BLUE);
            System.out.println(i);
        }
        
        panel1.setBackground(Color.RED);
        
        frame.add(panel1);
        frame.setSize(400,400);
        frame.setVisible(true);
        frame.setLayout(null);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
        // TODO code application logic here
        new trafficLight();
    }
    
}
