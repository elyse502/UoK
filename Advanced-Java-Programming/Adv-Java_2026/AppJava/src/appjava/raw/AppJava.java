package appjava.raw;
import javax.swing.*;
public class AppJava {
    public static void main(String[] args) {
        JFrame f = new JFrame("Button Ex");
        
        JButton b = new JButton("Click Me");
        b.setBounds(100, 50, 100, 40); // x, y, width, height
        
        f.add(b);
        
        f.setSize(400, 400);
        f.setLayout(null);
        f.setVisible(true);
    }
}
