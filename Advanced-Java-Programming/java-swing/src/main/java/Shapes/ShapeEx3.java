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

public class ShapeEx3 extends JFrame {
    public ShapeEx3 () {
        super("Drawing Arcs");
        setSize(300, 230);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void paint (Graphics g) {
        // Full Circle Arc
        g.setColor(Color.YELLOW);
        g.drawRect(15, 30, 80, 80);
        g.setColor(Color.RED);
        g.drawArc(15, 30, 80, 80, 0, 360);
        
        // second Arc with Rectangle - 110 degree
        g.setColor(Color.GREEN);
        g.drawRect(100, 35, 80, 80);
        g.setColor(Color.orange);
        g.drawArc(185, 35, 80, 80, 0, 110);
        
        // Third Arc with Rectangle (-ve Direction) - Clockwise
        g.setColor(Color.CYAN);
        g.drawRect(185, 35, 80, 80);
        g.setColor(Color.BLUE);
        g.drawArc(185, 35, 80, 80, 0, -270);
        
        g.fillArc(15, 120, 80, 80, 0, 360);
        g.fillArc(100, 120, 80, 80, 0, 270);
    }
    
    public static void main(String[] args) {
        new ShapeEx3();
    }
}
