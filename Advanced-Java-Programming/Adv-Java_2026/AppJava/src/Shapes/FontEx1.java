package Shapes;

/**
 * Font in Graphics
 * @author Danny Idukundatwese
 */
import java.awt.*;
import javax.swing.*;

public class FontEx1 extends JFrame {

    FontEx1 (){
        super("Using Fonts");
        
        setSize(500, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        
        g.setFont(new Font("Century Gothic", Font.BOLD, 12));
        g.drawString("Century Gothic 12 point Bold", 20, 50);
        
        g.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g.drawString("Monospaced 14 point Plain", 20, 70);
        
        g.setFont(new Font("Serif", Font.BOLD + Font.ITALIC, 18));
        g.setColor(Color.BLUE);
        g.drawString("Serif 18 point Bold", 20, 90);
        
        g.drawString(g.getFont().getName(), 20, 110); // getting the last set font
    }
    
    public static void main(String[] args) {
        new FontEx1();
    }
}
