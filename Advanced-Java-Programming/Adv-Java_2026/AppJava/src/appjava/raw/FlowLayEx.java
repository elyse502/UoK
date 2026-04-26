/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appjava.raw;
import javax.swing.*;
import java.awt.*;

public class FlowLayEx {
    
    JFrame f = new JFrame();
    String[] s = {"1", "2", "3", "4", "5"};
    JButton b;
    {
        for(int i = 0; i < s.length; i++){
            b = new JButton(s[i]);
            b.setSize(20, 20);
            f.add(b);
        }
        
        f.setSize(400, 400);
        f.setLayout(new FlowLayout(FlowLayout.LEFT , 20, 25));
        f.setVisible(true);
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
        new FlowLayEx();
    }
    
}