package bankManagement;

import java.sql.*;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner sc;

    User(Connection connection, Scanner sc) {
        this.connection = connection;
        this.sc = sc;
    }

    public void register() {
        sc.nextLine();
        System.out.print("Enter Full Name: ");
        String fullname = sc.nextLine();
        System.out.print("Enter Email Address: ");
        String email = sc.nextLine();
        System.out.print("Enter Password: ");
        String pswrd = sc.nextLine();
        if (user_exist(email)) {
            System.out.println("User already exists for this email address");
            return;
        }
        String register_query = "Insert into user (Full_Name, email, password) values(?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(register_query);
            stmt.setString(1, fullname);
            stmt.setString(2, email);
            stmt.setString(3, pswrd);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Registration Successful");
            } else {
                System.out.println("Registration Failed");
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public String login() {
        sc.nextLine();
        System.out.print("Enter Email Address: ");
        String email = sc.nextLine();
        System.out.print("Enter Password: ");
        String pswrd = sc.nextLine();
        String login_query = "select * from user where email = ? and password = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, pswrd);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return email;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean user_exist(String email) {
        String query = "select * from user where email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}