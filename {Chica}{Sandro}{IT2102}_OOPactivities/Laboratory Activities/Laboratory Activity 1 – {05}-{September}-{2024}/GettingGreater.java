import java.util.Scanner;

public class GettingGreater {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Getting the Greater Value");
        
        System.out.print("Enter first character: ");
        char ch1 = scanner.next().charAt(0);
        
        System.out.print("Enter second character: ");
        char ch2 = scanner.next().charAt(0);
        
        char greaterChar = (char) Math.max(ch1, ch2);
        
        System.out.println("-----------------------------------------");
        System.out.println("The character with greater value is: " + greaterChar);
        System.out.println("-----------------------------------------");
        System.out.println("Showing the ASCII Codes");
        System.out.println(ch1 + ": " + (int) ch1);
        System.out.println(ch2 + ": " + (int) ch2);
        
        scanner.close();
    }
}
