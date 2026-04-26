
package appjava.raw;

public class Teacher extends Person{
    private String subject;
    
    public Teacher(String name, int age, String subject) {
        super(name,age);
        this.subject = subject;
    }
    
    public String getSubject() {
        return subject;
    }
}
