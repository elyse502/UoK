
package Shapes;

/**
 * Getting Started with 2D Shapes
 * @author Danny Idukundatwese
 */

import java.awt.*;
import javax.swing.*;

public class ShapeEx1 extends JFrame {
    
    public void paint(Graphics g) {
        super.paint(g);
        
        g.drawString("Hello Java", 100, 100);
        g.drawRect(80, 150, 100, 60);
        g.setColor(Color.blue);
        g.fillRect(80, 250, 100, 60);
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        ShapeEx1 s = new ShapeEx1();
        s.setSize(400, 400);
        s.setVisible(true);
        
        s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
