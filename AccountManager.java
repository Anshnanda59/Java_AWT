package bankManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner sc;

    AccountManager(Connection connection, Scanner sc) {
        this.connection = connection;
        this.sc = sc;
    }

    public void debit_money(long acc_no) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security pin: ");
        String security = sc.nextLine();
        try {
            connection.setAutoCommit(false);
            String chck_Acc_query = "select * from accounts where Acc_No = ? and Security_pin = ?";
            if (acc_no != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement(chck_Acc_query);
                preparedStatement.setLong(1, acc_no);
                preparedStatement.setString(2, security);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double Curr_balance = resultSet.getDouble("balance");
                    if (amount <= Curr_balance) {
                        String debit_money_query = "update accounts set balance = balance - ? where Acc_No = ? ";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debit_money_query);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, acc_no);
                        int rowsAffect = preparedStatement1.executeUpdate();
                        if (rowsAffect > 0) {
                            System.out.println(amount + " of Money has been debited successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }

                    } else {
                        System.out.println("Insufficient Balance!!!");
                    }
                }
            } else {
                System.out.println("Invalid pin");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        connection.setAutoCommit(true);
    }

    public void credit_money(long acc_no) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security pin: ");
        String security = sc.nextLine();
        try {
            connection.setAutoCommit(false);
            String chck_Acc_query = "select * from accounts where Acc_No = ? and Security_pin = ?";
            if (acc_no != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement(chck_Acc_query);
                preparedStatement.setLong(1, acc_no);
                preparedStatement.setString(2, security);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {


                    String credit_money_query = "update accounts set balance = balance + ? where Acc_No = ? ";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_money_query);
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, acc_no);
                    int rowsAffect = preparedStatement1.executeUpdate();
                    if (rowsAffect > 0) {
                        System.out.println(amount + " of Money has been credited successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction failed");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }
            } else {
                System.out.println("Invalid pin");
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        connection.setAutoCommit(true);
    }

    public void getBalance(long acc_no) {

        sc.nextLine();
        System.out.print("Enter your security pin: ");
        String pswrd = sc.nextLine();
        try {
            String get_bal_query = "select balance from accounts where Acc_No = ? and Security_pin = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(get_bal_query);
            preparedStatement.setLong(1, acc_no);
            preparedStatement.setString(2, pswrd);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                System.out.print("Your current balance: " + balance);
            } else {
                System.out.println("Invalid pin");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    public void transfer_money(long sender_acc_no) {
        sc.nextLine();
        System.out.print("Enter your security pin: ");
        String pswrd = sc.nextLine();
        try {
            String chck_pin = "select * from accounts where Acc_No = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(chck_pin);
            preparedStatement.setLong(1, sender_acc_no);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.print("Enter the amount you want to send: ");
                double transfer_amount = sc.nextDouble();
                System.out.println("Enter the receiver's account no");
                long receiver_acc_no = sc.nextLong();
                try {
                    connection.setAutoCommit(false);
                    if (receiver_acc_no != 0) {
                        double curr_bal = resultSet.getDouble("balance");
                        if (curr_bal >= transfer_amount) {
                            String debit_money = "update accounts set balance = balance - ? where Acc_No = ? ";
                            String credit_money = "update accounts set balance = balance + ? where Acc_No = ?";
                            PreparedStatement deb_Statement = connection.prepareStatement(debit_money);
                            PreparedStatement cre_Statement = connection.prepareStatement(credit_money);
                            deb_Statement.setDouble(1, transfer_amount);
                            deb_Statement.setLong(2, sender_acc_no);
                            cre_Statement.setDouble(1, transfer_amount);
                            cre_Statement.setLong(2, receiver_acc_no);
                            int rowsAffectedDeb = deb_Statement.executeUpdate();
                            int rowsAffectedCre = cre_Statement.executeUpdate();
                            if (rowsAffectedCre > 0 && rowsAffectedDeb > 0) {
                                System.out.println(transfer_amount + " of Money has been transferred successfully!!!");
                                connection.commit();
                                connection.setAutoCommit(true);
                                return;
                            } else {
                                System.out.println("Transfer Failed");
                                connection.rollback();
                                connection.setAutoCommit(true);
                            }
                        } else {
                            System.out.println("Insufficient balance!!!");
                        }
                    } else {
                        System.out.println("Invalid Account No. ");
                    }
                } catch (RuntimeException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
