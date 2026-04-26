/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package elyseedev.java.swing;

/**
 *
 * @author Elysee NIYIBIZI
 */

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;


public class JTreeExample {
    JFrame f;
    JTreeExample() {
        f = new JFrame();
        DefaultMutableTreeNode style = new DefaultMutableTreeNode("Style");
        DefaultMutableTreeNode color = new DefaultMutableTreeNode("color");
        DefaultMutableTreeNode font = new DefaultMutableTreeNode("font");
        style.add(color); // add color in style
        style.add(font); // add font in style
        
        // create colors
        DefaultMutableTreeNode red = new DefaultMutableTreeNode("red");
        DefaultMutableTreeNode blue = new DefaultMutableTreeNode("blue");
        DefaultMutableTreeNode black = new DefaultMutableTreeNode("black");
        DefaultMutableTreeNode green = new DefaultMutableTreeNode("green");
        color.add(red); // add red in color
        color.add(black); // add blue in color
        color.add(green); // add greed in color
        
        JTree jt = new JTree(style);
        f.add(jt);
        f.setSize(200, 200);
        f.setVisible(true);
    }
    
    public static void main(String[] args) {
        new JTreeExample();
    }
}
