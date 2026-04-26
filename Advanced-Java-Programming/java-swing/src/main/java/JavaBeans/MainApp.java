/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JavaBeans;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 *
 * @author Elysee NIYIBIZI
 */

public class MainApp {

    public static void main(String[] args) {

        // Create object
        Student student = new Student();
        student.setName("Mugabo Mohammed");

        // File name
        String filename = "student.ser";

        // -------- SERIALIZATION (Saving) --------
        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(student);
            out.close();
            fileOut.close();

            System.out.println("Object has been serialized (saved).");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // -------- DESERIALIZATION (Loading) --------
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            Student loadedStudent = (Student) in.readObject();

            in.close();
            fileIn.close();

            System.out.println("Object has been deserialized (loaded).");
            System.out.println("Student Name: " + loadedStudent.getName());

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
