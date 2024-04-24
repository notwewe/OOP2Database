package com.example.midterm.Free;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CRUDTodo {

    public static int getUserIdByUsername(String username) {
        int userId = -1;
        try (Connection connection = MySQLConnection.getConnection()) {
            String query = "SELECT id FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    userId = resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    public boolean insertTask(int userId, String title, String description, String date, String time) {
        boolean inserted = false;
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "INSERT INTO todolist (userid, title, description, date, time) VALUES (?, ?, ?, ?, ?)"
             )) {
            statement.setInt(1, userId);
            statement.setString(2, title);
            statement.setString(3, description);
            statement.setString(4, date);
            statement.setString(5, time);
            int num = statement.executeUpdate();
            if (num != 0) {
                inserted = true;
                System.out.println("Task inserted successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Exception in insertTask");
            e.printStackTrace();
        }
        return inserted;
    }

    public List<Task> readTasks(int userId) {
        List<Task> tasks = new ArrayList<>();
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "SELECT * FROM todolist WHERE userid = ?"
             )) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // No need to retrieve taskId from the database as it's not used in this method
                // You can use the autogenerated id column
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String date = resultSet.getString("date");
                String time = resultSet.getString("time");

                // Create a new Task object with retrieved values
                Task task = new Task(title, description, date, time);

                tasks.add(task);
            }
        } catch (SQLException e) {
            System.out.println("Exception in readTasks");
            e.printStackTrace();
        }
        return tasks;
    }

    // Other methods remain unchanged
}