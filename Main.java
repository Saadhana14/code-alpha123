/******************************************************************************

                            Online Java Compiler.
                Code, Compile, Run and Debug java program online.
Write your code in this editor and press "Run" button to execute it.

*******************************************************************************/

import java.util.ArrayList;
import java.util.Scanner;

public class StudentGradeManager {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> studentNames = new ArrayList<>();
        ArrayList<Double> studentGrades = new ArrayList<>();

        System.out.println("=== Student Grade Manager ===");

        while (true) {
            System.out.print("Enter student name (or type 'done' to finish): ");
            String name = scanner.nextLine();
            if (name.equalsIgnoreCase("done")) {
                break;
            }

            double grade = -1;
            while (true) {
                System.out.print("Enter grade for " + name + ": ");
                String input = scanner.nextLine();
                try {
                    grade = Double.parseDouble(input);
                    if (grade < 0 || grade > 100) {
                        System.out.println("Grade must be between 0 and 100.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            studentNames.add(name);
            studentGrades.add(grade);
        }

        if (studentNames.isEmpty()) {
            System.out.println("No student data entered.");
        } else {
            printSummary(studentNames, studentGrades);
        }

        scanner.close();
    }

    private static void printSummary(ArrayList<String> names, ArrayList<Double> grades) {
        double total = 0;
        double highest = grades.get(0);
        double lowest = grades.get(0);

        for (double grade : grades) {
            total += grade;
            if (grade > highest) highest = grade;
            if (grade < lowest) lowest = grade;
        }

        double average = total / grades.size();

        System.out.println("\n=== Summary Report ===");
        for (int i = 0; i < names.size(); i++) {
            System.out.printf("Student: %-15s Grade: %.2f\n", names.get(i), grades.get(i));
        }

        System.out.printf("\nAverage Grade: %.2f\n", average);
        System.out.printf("Highest Grade: %.2f\n", highest);
        System.out.printf("Lowest Grade : %.2f\n", lowest);
    }
}