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

import java.awt.*;
import javax.swing.*;

public class ColorEx1 extends JFrame {

    ColorEx1() {    
        super("Using colors"); // Setting JFrame Title

        setSize(400, 180);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void paint(Graphics g){
        super.paint(g);
        
        g.setColor(new Color(255, 0, 0)); // Set RedColor
        g.fillRect(25, 40, 80, 20); // Draw a filled Red Rectangle
        g.drawString("Current RGB: " + g.getColor(), 130, 55);
        
        g.setColor(new Color(0.0f, 1.0f, 0.0f));
        g.fillRect(25, 65, 80, 20);
        g.drawString("Current RGB: " + g.getColor(), 130, 80);
        
        g.setColor(Color.blue);
        g.fillRect(25, 90, 80, 20);
        g.drawString("Current RGB: " + g.getColor(), 130, 105);
        
        g.setColor(Color.magenta);
        g.fillRect(25, 115, 80, 20);
        g.drawString("Current RGB: " + g.getColor(), 130, 130);
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        ColorEx1 c = new ColorEx1();
    }
    
}
