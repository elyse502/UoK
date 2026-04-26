
package appjava.raw;

public class Student extends Person {
    private int roll_number;
    
    public Student(String name, int age, int roll_number) {
        super(name, age);
        this.roll_number = roll_number;
    }
    
    public int getRollNumber() {
        return roll_number;
    }
}
