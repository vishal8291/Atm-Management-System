import java.sql.*;
import java.util.Scanner;

public class prac {
    public static void main(String args[]) {
        try {
            // Load MySQL Driver
            Class.forName("com.mysql.jdbc.Driver");
            
            // Establish Connection
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/students", "root", "root");

            Scanner sc = new Scanner(System.in);
            
            // Taking input from the user
            System.out.println("Enter the name:");
            String name = sc.nextLine();

            System.out.println("Enter the roll number:");
            int rollno = sc.nextInt();

            System.out.println("Enter the contact:");
            int contact = sc.nextInt();

            System.out.println("Enter marks for 3 subjects:");
            System.out.println("Enter marks for subject 1:");
            float sub1 = sc.nextFloat();

            System.out.println("Enter marks for subject 2:");
            float sub2 = sc.nextFloat();

            System.out.println("Enter marks for subject 3:");
            float sub3 = sc.nextFloat();

            // Calculate percentage
            float percentage = (sub1 + sub2 + sub3) / 3;

            // Prepare and execute SQL statement
            PreparedStatement ps = con.prepareStatement("INSERT INTO students(sname, rollno, contact, percentage) VALUES (?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setInt(2, rollno);
            ps.setInt(3, contact);
            ps.setFloat(4, percentage);
            ps.executeUpdate();

            // Close resources
            ps.close();
            con.close();
            sc.close();

            System.out.println("Data inserted successfully!");
        } 
        catch (Exception e) {
            e.printStackTrace(); // Print the full error details
        }
    }
}
