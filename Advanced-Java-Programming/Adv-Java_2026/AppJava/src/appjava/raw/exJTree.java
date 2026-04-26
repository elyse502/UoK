package appjava.raw;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class exJTree {

    JFrame f = new JFrame("JTree Example");
    {
        DefaultMutableTreeNode style = new DefaultMutableTreeNode("Style");
        DefaultMutableTreeNode color = new DefaultMutableTreeNode("Color");
        DefaultMutableTreeNode font = new DefaultMutableTreeNode("font");
        style.add(color); style.add(font);
        
        DefaultMutableTreeNode red = new DefaultMutableTreeNode("red");
        DefaultMutableTreeNode blue = new DefaultMutableTreeNode("blue");
        DefaultMutableTreeNode green = new DefaultMutableTreeNode("green");
        color.add(red); color.add(blue); color.add(green);
        
        DefaultMutableTreeNode times = new DefaultMutableTreeNode("Times");
        DefaultMutableTreeNode aria = new DefaultMutableTreeNode("Arial");
        font.add(times); font.add(aria);
        
        JTree jt = new JTree(style);
        
        f.add(jt);
        f.setSize(400, 400);
        f.setVisible(true);
        f.setLayout(null);
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
            new exJTree();
    }
    
}
