/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arrays;

/**
 *
 * @author elyse
 */
public class Arrays {
    static String helloWorld() {
        return "Hello World!";
}

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String[] cars = {"Volvo", "BMW", "Ford", "Mazd", "Dodge"};
        for (int i = 0; i < cars.length; i++)
            System.out.println(cars[i]);
        System.out.println("");
        int[] nums = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println("Fifth Element from an ARRAY: " + nums[4]);
        for (int j = 0; j < nums.length; j++)
            System.out.println(nums[j]);
        System.out.println("");
        for (int k : nums)
            System.out.println(k);
        System.out.println("");
        String[][] candidates = {
            {"John", "Peter", "Mary"},
            {"Leo", "Fifi", "Noel"}
        };
        System.out.println("Candidate at index [0][1] in our ARRAY: " + candidates[0][1]);
        System.out.println("Candidate at index [1][2] in our ARRAY: " + candidates[1][2]);
        System.out.println("\n(TWO Dimension ARRAY), ALL ELEMENTS IN OUR ARRAY:");
        for (int l = 0;l < candidates.length; l++)
            for (int m = 0; m < candidates[l].length; m++)
                System.out.println(candidates[l][m]);
        System.out.println("\n(THREE Dimension ARRAY), ALL ELEMENTS IN OUR ARRAY:");
        int[][][] arrayNumbers = {
            {{1, 2}, {3,4}}, 
            {{5,6}, {7,8}},
            {{9,10}, {11,12}}
        };
        for (int i = 0;i < arrayNumbers.length; i++) {
            for (int j = 0; j < arrayNumbers[i].length; j++) {
                for (int k = 0; k < arrayNumbers[i][j].length; k++) {
                    System.out.println(arrayNumbers[i][j][k]);
                }
            }
        }
        System.out.println(helloWorld());;
    }
    
}
