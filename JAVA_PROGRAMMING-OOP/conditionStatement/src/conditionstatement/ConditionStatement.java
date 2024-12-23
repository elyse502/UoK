/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conditionstatement;

import java.util.Scanner;

/**
 *
 * @author elyse
 */
public class ConditionStatement {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("ENTER USERNAME: ");
        String username = scanner.nextLine();

        System.out.print("ENTER PASSWORD: ");
        String password = scanner.nextLine();

        if(username.equals("Admin") && password.equals("password123")){
            System.out.println("YOU HAVE SUCCESSFULLY LOGGED IN");
        }else{
            System.out.println("YOUR CREDENTIALS ARE WRONG, TRY AGAIN");
        }
        
        System.out.println("\n\n");
        System.out.println("Enter your Marks / 100 :".trim());
        double marks = scanner.nextDouble();
        
        System.out.println("> YOUR MARKS OUT OF 100 = " + marks);
        
        if (marks >= 80 && marks <= 100)
            System.out.println(">> You got Grade A");
        else if (marks >= 70)
            System.out.println(">> You got Grade B");
        else if (marks >= 60)
            System.out.println(">> You got Grade c");
        else if (marks >= 50)
            System.out.println(">> You got Grade D");
        else
            System.out.println(">> You got Grade F");
        
        System.out.println("\n\n");
        System.out.println("Enter a number betwee 1 and 7");
        int day = scanner.nextInt();
        
        switch (day){
            case 1:
                System.out.println("We're on Monday!");
                break;
            case 2:
                System.out.println("We're on Tuesday!");
                break;
            case 3:
                System.out.println("We're on Wednesday!");
                break;
            case 4:
                System.out.println("We're on Thursday!");
                break;
            case 5:
                System.out.println("We're on Friday!");
                break;
            case 6:
                System.out.println("We're on Saturday!");
                break;
            case 7:
                System.out.println("We're on Sunday!");
                break;
            default:
                System.out.println("Enter a valid day of the week!");
        }
    }
}
