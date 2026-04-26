/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package JavaBeans;

/**
 * Continuation of Student
 * @author idtda
 */

import java.io.*;

public class MainApp {

    public static void main(String[] args) {
        Student s = new Student();
        s.setNom("Danny");
        
        // 1. Serialize - Saving object to a file
        try {
            // Creating the file output stream (File will be created if not exists)
            FileOutputStream fileOut = new FileOutputStream("student.ser");
            
            // Create object output stream to write to object into file
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            
            // Write (Serialize) the student object into file
            out.writeObject(out);
            
            out.close();
            fileOut.close();
            
            // Message to confirm the saving of object
            System.out.println("Object has been serialized (saved).");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // 2. Deserialize - Reading object from file
        try {
            // Open file input stream to read file
            FileInputStream fileIn = new FileInputStream("student.ser");
            
            // Create object from input stream to read object
            ObjectInputStream in = new ObjectInputStream(fileIn);
            
            // Read (deserialize) object from file and convert back to student
            Student loadStudent = (Student) in.readObject();
            
            in.close();
            fileIn.close();
            
            // Displaying confirmation and data
            System.out.println("Object has been deserialized (loaded).");
            System.out.println("Student name: " + loadStudent.getNom());            
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
}
