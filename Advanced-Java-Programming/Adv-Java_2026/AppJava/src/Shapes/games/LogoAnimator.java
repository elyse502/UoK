/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Shapes.games;

/**
 *
 * @author idtda
 */

//LogoAnimator2.java
//Animated juggling bean character - drawn programmatically


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import javax.swing.*;

public class LogoAnimator extends JPanel
     implements ActionListener, Serializable {

 private static final long serialVersionUID = 1L;

 protected int totalImages = 20;
 protected int currentImage = 0;
 protected int animationDelay = 50;
 protected Timer animationTimer;

 // Bean positions for juggling animation
 private double[] beanAngles = {0, Math.PI / 3, 2 * Math.PI / 3};

 public LogoAnimator() {
     setSize(getPreferredSize());
     setBackground(new Color(220, 220, 220));
     startAnimation();
 }

 @Override
 public void paintComponent(Graphics g) {
     super.paintComponent(g);

     Graphics2D g2d = (Graphics2D) g;
     g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
             RenderingHints.VALUE_ANTIALIAS_ON);

     int cx = getWidth() / 2;
     int cy = getHeight() / 2;

     // --- Dashed border (like BeanBox) ---
     g2d.setColor(new Color(150, 150, 150));
     float[] dash = {6f, 4f};
     g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT,
             BasicStroke.JOIN_MITER, 10f, dash, 0f));
     g2d.drawRect(5, 5, getWidth() - 10, getHeight() - 10);
     g2d.setStroke(new BasicStroke(1f));

     // --- Juggling phase ---
     double phase = (currentImage / (double) totalImages) * 2 * Math.PI;

     // === Draw 3 juggling beans (coffee bean shaped) ===
     for (int i = 0; i < 3; i++) {
         double angle = beanAngles[i] + phase;
         int bx = cx + (int) (55 * Math.cos(angle));
         int by = (cy - 55) + (int) (30 * Math.sin(angle));
         drawCoffeeBean(g2d, bx, by, angle);
     }

     // === Body (white ghost-like) ===
     g2d.setColor(Color.WHITE);
     // Main body oval
     g2d.fillOval(cx - 28, cy - 20, 56, 70);

     // Rounded head
     g2d.fillOval(cx - 24, cy - 50, 48, 48);

     // Outline
     g2d.setColor(new Color(80, 80, 80));
     g2d.setStroke(new BasicStroke(2f));
     g2d.drawOval(cx - 28, cy - 20, 56, 70);
     g2d.drawOval(cx - 24, cy - 50, 48, 48);
     g2d.setStroke(new BasicStroke(1f));

     // === Red nose ===
     g2d.setColor(new Color(220, 50, 50));
     g2d.fillOval(cx - 8, cy - 20, 16, 16);

     // === Eyes (small black dots) ===
     g2d.setColor(Color.BLACK);
     g2d.fillOval(cx - 14, cy - 38, 7, 7);
     g2d.fillOval(cx + 7, cy - 38, 7, 7);

     // === Arms (animated waving) ===
     g2d.setColor(new Color(80, 80, 80));
     g2d.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND,
             BasicStroke.JOIN_ROUND));

     // Left arm
     double leftWave = Math.sin(phase) * 15;
     g2d.drawLine(cx - 28, cy + 5,
             cx - 55, cy - 10 + (int) leftWave);
     // Left hand fingers
     g2d.drawLine(cx - 55, cy - 10 + (int) leftWave,
             cx - 62, cy - 20 + (int) leftWave);
     g2d.drawLine(cx - 55, cy - 10 + (int) leftWave,
             cx - 63, cy - 10 + (int) leftWave);
     g2d.drawLine(cx - 55, cy - 10 + (int) leftWave,
             cx - 60, cy - 2 + (int) leftWave);

     // Right arm
     double rightWave = Math.sin(phase + Math.PI) * 15;
     g2d.drawLine(cx + 28, cy + 5,
             cx + 55, cy - 10 + (int) rightWave);
     // Right hand fingers
     g2d.drawLine(cx + 55, cy - 10 + (int) rightWave,
             cx + 62, cy - 20 + (int) rightWave);
     g2d.drawLine(cx + 55, cy - 10 + (int) rightWave,
             cx + 63, cy - 10 + (int) rightWave);
     g2d.drawLine(cx + 55, cy - 10 + (int) rightWave,
             cx + 60, cy - 2 + (int) rightWave);

     g2d.setStroke(new BasicStroke(1f));

     // === Legs (two thin triangular legs) ===
     g2d.setColor(new Color(80, 80, 80));
     g2d.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND,
             BasicStroke.JOIN_ROUND));
     g2d.drawLine(cx - 10, cy + 50, cx - 14, cy + 80);
     g2d.drawLine(cx + 10, cy + 50, cx + 14, cy + 80);
     g2d.setStroke(new BasicStroke(1f));

     // Advance animation frame
     currentImage = (currentImage + 1) % totalImages;
 }

 /**
  * Draws a coffee-bean shape (two overlapping ellipses with a crease line)
  */
 private void drawCoffeeBean(Graphics2D g2d, int x, int y, double angle) {
     Graphics2D bg = (Graphics2D) g2d.create();
     bg.translate(x, y);
     bg.rotate(angle + Math.PI / 4);

     // Bean body
     bg.setColor(new Color(101, 55, 0));
     bg.fillOval(-14, -9, 28, 18);

     // Crease line
     bg.setColor(new Color(70, 35, 0));
     bg.setStroke(new BasicStroke(1.5f));
     bg.drawLine(0, -8, 0, 8);

     bg.dispose();
 }

 @Override
 public void actionPerformed(ActionEvent e) {
     repaint();
 }

 public void startAnimation() {
     if (animationTimer == null) {
         currentImage = 0;
         animationTimer = new Timer(animationDelay, this);
         animationTimer.start();
     }
 }

 public void stopAnimation() {
     if (animationTimer != null) {
         animationTimer.stop();
         animationTimer = null;
     }
 }

 @Override
 public Dimension getPreferredSize() {
     return new Dimension(200, 220);
 }

 public static void main(String[] args) {
     SwingUtilities.invokeLater(() -> {
         LogoAnimator anim = new LogoAnimator();

         JFrame app = new JFrame("BeanBox - Logo Animator");
         app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

         // --- Slider panel (like BeanBox) ---
         JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
         topPanel.setBackground(new Color(200, 200, 200));

         JSlider slider = new JSlider(0, 100, 60);
         slider.setPreferredSize(new Dimension(200, 30));
         JTextField valueField = new JTextField("60", 4);

         slider.addChangeListener(e -> {
             int val = slider.getValue();
             valueField.setText(String.valueOf(val));
             // Map slider to animation delay (faster = lower delay)
             anim.stopAnimation();
             anim.animationDelay = Math.max(10, 110 - val);
             anim.startAnimation();
         });

         topPanel.add(slider);
         topPanel.add(valueField);

         // --- Menu bar (like BeanBox) ---
         JMenuBar menuBar = new JMenuBar();
         menuBar.add(new JMenu("File"));
         menuBar.add(new JMenu("Edit"));
         menuBar.add(new JMenu("View"));
         menuBar.add(new JMenu("Services"));
         menuBar.add(new JMenu("Help"));
         app.setJMenuBar(menuBar);

         app.add(topPanel, BorderLayout.NORTH);
         app.add(anim, BorderLayout.CENTER);

         app.pack();
         app.setLocationRelativeTo(null);
         app.setVisible(true);
     });
 }
}