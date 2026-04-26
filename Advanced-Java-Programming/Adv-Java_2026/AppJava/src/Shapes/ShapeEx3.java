/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shapes;

/**
 * Working around the Arcs
 * @author STUDENTS
 */

import java.awt.*;
import javax.swing.*;

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
        g.setColor(Color.red);
        g.drawArc(15, 30, 80, 80, 0, 360);

        // Second Arc with Rectangle - 110 degree
        g.setColor(Color.green);
        g.drawRect(100, 35, 80, 80);
        g.setColor(Color.orange);
        g.drawArc(100, 35, 80, 80, 0, 110);
        
        // Third Arc with Rectangle (-ve Direction) - Clockwise
        g.setColor(Color.cyan);
        g.drawRect(185, 35, 80, 80);
        g.setColor(Color.blue);
        g.drawArc(185, 35, 80, 80, 0, -270);
        
        g.fillArc(15, 120, 80, 80, 0, 360);
        g.fillArc(100, 120, 80, 80, 0, -90);
        g.fillArc(185, 120, 80, 80, 0, 270);           
    }
    
    public static void main(String[] args) {
        new ShapeEx3();
    }
    
}
