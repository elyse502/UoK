package appjava.raw;
import javax.swing.*;
import java.awt.*;

public class feedBackForm {

    feedBackForm(){
        JFrame frame = new JFrame("FeedBack");
        Label title = new Label("FeedBack Now");
        
        title.setAlignment(Label.CENTER);
        title.setBounds(20, 20, 340, 20);
        
        JLabel name = new JLabel("Your Name:");
        name.setBounds(20, 50, 120, 30);
        
        JTextField inputName = new JTextField("");
        inputName.setBounds(20, 85, 300, 30);
        
        JLabel email = new JLabel("Your Email:");
        email.setBounds(20, 120, 300, 30);
        
        JTextField inputEmail = new JTextField("");
        inputEmail.setBounds(20, 155, 300, 30);
        
        JLabel feed = new JLabel("Your Feedback:");
        feed.setBounds(20, 190, 300, 30);
        
        JTextArea inputFeed = new JTextArea("");
        inputFeed.setBounds(20, 225, 300, 100);
        
        JButton submit = new JButton("Submit");
        submit.setBounds(20, 335, 120, 30);
        
        frame.add(title);
        frame.add(name);
        frame.add(inputName);
        frame.add(email);
        frame.add(feed);
        frame.add(inputEmail);
        frame.add(inputFeed);
        frame.add(submit);
        
        frame.setSize(380, 500);
        frame.setLayout(null);
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        new feedBackForm();
    }
    
}
