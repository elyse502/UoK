/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package car;

/**
 *
 * @author elyse
 */
public class Car {
    // Instance variables(attributes)
    String model;
    int year;
    // Constructor to initialize the car object
    public Car(String model, int year) {
        this.model = model;  // Initialize model
        this.year = year;  // Initialize year
    }
    
    // Methods to display the details of the car
    public void displayDetails() {
        System.out.println("Car Model: " + model);
        System.out.println("Car Year: " + year);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Creating a car object using the constructor
        Car myCar = new Car("Toyota Corolla", 2020);
        
        // Calling the method to display car details
        myCar.displayDetails();
    }
    
}
