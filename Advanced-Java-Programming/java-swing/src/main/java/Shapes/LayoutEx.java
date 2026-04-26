/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shapes;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Elysee NIYIBIZI
 */
public class LayoutEx {
    JFrame f = new JFrame();
    JButton b1 = new JButton("NORTH");
    JButton b2 = new JButton("SOUTH");
    JButton b3 = new JButton("EAST");
    JButton b4 = new JButton("WEST");
    JButton b5 = new JButton("CENTER");
    
    {
        f.setLayout(new BorderLayout(20, 20));
        f.add(b1, BorderLayout.NORTH);
        f.add(b2, BorderLayout.SOUTH);
        f.add(b3, BorderLayout.EAST);
        f.add(b4, BorderLayout.WEST);
        f.add(b5, BorderLayout.CENTER);
        
        f.setSize(400, 400);
        f.setVisible(true);
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
        new LayoutEx();
    }
}
