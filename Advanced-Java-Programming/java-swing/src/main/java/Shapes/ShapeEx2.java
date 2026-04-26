/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shapes;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

/**
 *
 * @author Elysee NIYIBIZI
 */

public class ShapeEx2 extends JFrame {
    ShapeEx2 () {
        super("Working with Shapes");
        
        setSize(500, 250);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void paint (Graphics g) {
        super.paint(g);
        
        g.setColor(Color.RED);
        g.drawLine(55, 50, 350, 50);
        
        g.setColor(Color.BLUE);
        g.drawRect(5, 40, 90, 55);
        g.fillRect(100, 40, 90, 55);
        
        g.setColor(Color.CYAN);
        g.fillRoundRect(195, 40, 90, 55, 20, 20);
        
        g.setColor(Color.YELLOW);
        g.draw3DRect(5, 100, 90, 55, true);
        g.fill3DRect(100, 100, 90, 55, true);
        
        g.setColor(Color.magenta);
        
        g.drawOval(195, 100, 90, 55);
        g.fillOval(290, 100, 90, 55);
    }
    
    public static void main(String[] args) {
        new ShapeEx2();
    }
}
