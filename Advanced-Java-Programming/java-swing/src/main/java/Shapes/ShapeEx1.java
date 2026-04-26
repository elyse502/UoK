/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shapes;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Elysee NIYIBIZI
 */

public class ShapeEx1 extends JFrame {
    public void paint(Graphics g) {
        super.paint(g);
        
        g.drawString("Hello Java", 100, 100);
        g.drawRect(80, 150, 100, 60);
        g.setColor(Color.BLUE);
        g.fillRect(80, 250, 100, 60);
    }
    
    public static void main(String[] args) {
        ShapeEx1 s = new ShapeEx1();
        s.setSize(400, 400);
        s.setVisible(true);
        
        s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
