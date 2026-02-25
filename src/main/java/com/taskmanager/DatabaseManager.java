package com.taskmanager;

import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:tasks.db";

    public static void initialize() {
        String sql = "CREATE TABLE IF NOT EXISTS tasks (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "description TEXT NOT NULL," +
                     "is_done INTEGER DEFAULT 0)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Init Error: " + e.getMessage());
        }
    }

    public static void saveTask(String description) {
        // Use '?' as a placeholder to prevent SQL Injection
        String sql = "INSERT INTO tasks(description) VALUES(?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, description);
            pstmt.executeUpdate();
            System.out.println("Success: Task saved!");
        } catch (SQLException e) {
            System.out.println("Save Error: " + e.getMessage());
        }
    }

    public static void showTasks() {
        System.out.println("\n--------------------------------------------");
        System.out.printf("%-4s | %-8s | %-25s%n", "ID", "STATUS", "DESCRIPTION");
        System.out.println("--------------------------------------------");

        String sql = "SELECT * FROM tasks";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            boolean hasTasks = false;
            while (rs.next()) {
                hasTasks = true;
                String status = rs.getInt("is_done") == 1 ? "DONE [X]" : "TODO [ ]";
                System.out.printf("%-4d | %-8s | %-25s%n", 
                                  rs.getInt("id"), status, rs.getString("description"));
            }
            if (!hasTasks) {
                System.out.println("|      No tasks yet! Your list is empty.       |");
            }
        } catch (SQLException e) {
            System.out.println("Read Error: " + e.getMessage());
        }
        System.out.println("--------------------------------------------");
    }

    public static void markDone(int id) {
        if (id <= 0) {
            System.out.println("Invalid ID.");
            return;
        }
        if (!exists(id)) {
            System.out.println("Task ID not found.");
            return;
        }
        String sql = "UPDATE tasks SET is_done = 1 WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            if (pstmt.executeUpdate() > 0) System.out.println("Updated Task #" + id);
            else System.out.println("Task ID not found.");
        } catch (SQLException e) {
            System.out.println("Update Error: " + e.getMessage());
        }
    }

    public static void delete(int id) {
        if (id <= 0) {
            System.out.println("Invalid ID.");
            return;
        }
        if (!exists(id)) {
            System.out.println("ID not found.");
            return;
        }
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            if (pstmt.executeUpdate() > 0) System.out.println("Deleted Task #" + id);
            else System.out.println("ID not found.");
        } catch (SQLException e) {
            System.out.println("Delete Error: " + e.getMessage());
        }
    }

    public static boolean exists(int id) {
        if (id <= 0) return false;
        String sql = "SELECT COUNT(*) FROM tasks WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Exists Check Error: " + e.getMessage());
        }
        return false;
    }
}