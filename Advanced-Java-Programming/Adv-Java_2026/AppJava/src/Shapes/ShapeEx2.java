/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shapes;

/**
 *
 * @author STUDENTS
 */
import javax.swing.*;
import java.awt.*;

public class ShapeEx2 extends JFrame {

    /**
     * @param args the command line arguments
     */
    ShapeEx2 () {
        super("Working with Shapes");
        
        setSize(500, 250);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void paint (Graphics g){
        super.paint(g);
        
        g.setColor(Color.RED);
        g.drawLine(55, 50, 350, 50);
        
        g.setColor(Color.BLUE);
        g.drawRect(5, 40, 90, 55);
        g.fillRect(100, 40, 90, 55);
        
        g.setColor(Color.cyan);
        g.fillRoundRect(195, 40, 90, 55, 50, 50); // Lasts are arc width and height
        g.drawRoundRect(290, 40, 90, 55, 20, 20);
        
        g.setColor(Color.YELLOW);
        g.draw3DRect(5, 100, 90, 55, true);
        g.fill3DRect(100, 100, 90, 55, true);
        
        g.setColor(Color.MAGENTA);
        
        g.drawOval(195, 100, 90, 55);
        g.fillOval(290, 100, 90, 55);
    }
    public static void main(String[] args) {
        new ShapeEx2();
    }
    
}
