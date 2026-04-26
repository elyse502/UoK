package JavaBeans.BeansGUI;

/**
 *
 * @author STUDENTS
 */
import java.io.Serializable;
import java.awt.*;

public class FirstBeanShapes extends Canvas implements Serializable {
    
    public FirstBeanShapes() {
        setSize(200, 200);
        setBackground(Color.white);
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(20, 20, 80, 40);
        
        g.setColor(Color.GREEN);
        g.fillOval(110, 70, 60, 60);
        
        g.setColor(Color.blue);
        
        int[] xPoints = {40, 10, 70};
        int[] yPoints = {150, 190, 190};
        
        g.fillPolygon(yPoints, xPoints, 3);
    }
    
    public static void main(String[] args){
        Frame f = new Frame("Separated Shapes Bean");
        
        FirstBeanShapes bean = new FirstBeanShapes();
        
        f.add(bean);
        
        f.setSize(300, 300);
        f.setLayout(null);
        f.setVisible(true);
        
        f.addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
