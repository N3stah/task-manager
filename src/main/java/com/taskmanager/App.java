package com.taskmanager;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        DatabaseManager.initialize();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            clearScreen();
            System.out.println("============================================");
            System.out.println("      NESTAH'S TASK MANAGER             ");
            System.out.println("============================================");
            System.out.println("1. Add | 2. View | 3. Done | 4. Delete | 5. Exit");
            System.out.print("Choose: ");

            if (!scanner.hasNextInt()) {
                scanner.next(); 
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            if (choice == 1) {
                System.out.print("Task description: ");
                DatabaseManager.saveTask(scanner.nextLine());
            } else if (choice == 2) {
                DatabaseManager.showTasks();
            } else if (choice == 3) {
                System.out.print("Task ID to complete: ");
                String idStr = scanner.nextLine().trim();
                try {
                    int id = Integer.parseInt(idStr);
                    DatabaseManager.markDone(id);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid ID. Please enter a number.");
                }
            } else if (choice == 4) {
                System.out.print("Task ID to delete: ");
                String idStr = scanner.nextLine().trim();
                try {
                    int id = Integer.parseInt(idStr);
                    System.out.print("Confirm (y/n)? ");
                    String confirm = scanner.nextLine();
                    if (confirm.equalsIgnoreCase("y")) DatabaseManager.delete(id);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid ID. Please enter a number.");
                }
            } else if (choice == 5) {
                break;
            }
            pause(scanner);
        }
        scanner.close();
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void pause(Scanner scanner) {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}