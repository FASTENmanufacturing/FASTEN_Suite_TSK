package com.fasten.wp4.optimizator.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Ignore;
@Ignore
public class TestConnection {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://150.162.6.64:5432/thyssen", "fasten", "#fasten-0917!")) {

            System.out.println("Java JDBC PostgreSQL Example");
            Statement statement = connection.createStatement();
            System.out.println("Connected to PostgreSQL database!");
            System.out.println("Reading delivery records...");
            System.out.printf("%-30.30s  %-30.30s%n", "Destination", "Cost");
            ResultSet resultSet = statement.executeQuery("SELECT * FROM delivery LIMIT 10");
            while (resultSet.next()) {
                System.out.printf("%-30.30s  %-30.30s%n", resultSet.getString("destination"), resultSet.getString("cost"));
            }

        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }
}
