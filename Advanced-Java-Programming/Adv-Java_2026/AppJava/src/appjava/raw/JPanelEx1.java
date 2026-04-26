package appjava.raw;
import java.awt.*;
import javax.swing.*;

public class JPanelEx1 {
    JPanelEx1(){
        JFrame f = new JFrame();
        JPanel p = new JPanel();
        
        JButton b1 = new JButton("Btn 1");
        JButton b2 = new JButton("Btn 2");
                
        p.setBounds(40, 40, 200, 200);
        p.setBackground(Color.BLUE);
        b1.setBounds(100, 100, 50, 30);
        b2.setBounds(150, 150, 50, 30);
        p.add(b1);
        p.add(b2);
        
        f.add(p);        
        f.setSize(400, 400);
        f.setLayout(null);
        f.setVisible(true);
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
        new JPanelEx1();
    }
    
}
