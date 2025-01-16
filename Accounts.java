package bankManagement;

import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner sc;

    Accounts(Connection connection, Scanner sc) {
        this.connection = connection;
        this.sc = sc;
    }

    public long openAccount(String email) {
        if (!account_exist(email)) {
            String open_acc_query = "insert into accounts(Acc_No, full_Name, Email, balance ,Security_pin) values(?, ?, ?, ?, ?)";
            sc.nextLine();
            System.out.print("Enter Full Name: ");
            String fullname = sc.nextLine();
            System.out.print("Enter Initial Amount: ");
            double balance = sc.nextDouble();
            sc.nextLine();
            System.out.print("Enter Security pin: ");
            String pswrd = sc.nextLine();
            try {
                long Acc_no = generate_acc_no();
                PreparedStatement preparedStatement = connection.prepareStatement(open_acc_query);
                preparedStatement.setLong(1, Acc_no);
                preparedStatement.setString(2, fullname);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, pswrd);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    return Acc_no;
                } else {
                    throw new RuntimeException("Account creation failed");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        throw new RuntimeException("Account already exists");
    }

    public long getAccountNo(String email) {
        String get_acc_query = "select Acc_No from accounts where email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(get_acc_query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("Acc_No");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Account No does not exist");
    }

    private long generate_acc_no() {
        try {
            String genAccQuery = "select Acc_No from accounts order by Acc_No desc limit 1";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(genAccQuery);
            if (resultSet.next()) {
                long last_acc_no = resultSet.getLong("Acc_No");
                return last_acc_no + 1;
            } else {
                return 1000100;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean account_exist(String email) {
        String query = "select Acc_No from accounts where email = ?";
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
