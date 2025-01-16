package bankManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {
    private static final String URL = "jdbc:mysql://localhost:3306/stud";
    private static final String user = "root";
    private static final String pswrd = "@merasql123";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Drivers Loaded Successfully");

        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            Connection connection = DriverManager.getConnection(URL, user, pswrd);
            Scanner sc = new Scanner(System.in);

            User user = new User(connection, sc);

            Accounts acc = new Accounts(connection, sc);

            AccountManager acc_Mgr = new AccountManager(connection, sc);

            String email;
            long acc_no;

            while (true) {
                System.out.println(" *** WELCOME TO BANKING SYSTEM ***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Enter your choice: ");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        user.register();
                        System.out.flush();
                        break;
                    case 2:
                        email = user.login();
                        if (email != null) {
                            System.out.println();
                            System.out.println("User logged in");
                            if (!acc.account_exist(email)) {
                                System.out.println();
                                System.out.println("1. Open a new Account");
                                System.out.println("2. Exit");
                                if (sc.nextInt() == 1) {
                                    acc_no = acc.openAccount(email);
                                    System.out.println("Account created successfully.");
                                    System.out.println("Your Account No is-> " + acc_no);
                                } else {
                                    break;
                                }
                            }

                            acc_no = acc.getAccountNo(email);
                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Get balance");
                                System.out.println("5. Log out");
                                System.out.print("Enter your choice: ");
                                choice2 = sc.nextInt();
                                switch (choice2) {
                                    case 1:
                                        acc_Mgr.debit_money(acc_no);
                                        break;
                                    case 2:
                                        acc_Mgr.credit_money(acc_no);
                                        break;
                                    case 3:
                                        acc_Mgr.transfer_money(acc_no);
                                        break;
                                    case 4:
                                        acc_Mgr.getBalance(acc_no);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Invalid choice hai bhai mere");
                                        break;

                                }

                            }
                        } else {
                            System.out.println("Incorrect email");
                        }
                    case 3:
                        System.out.println("THANK YOU FOR USING OUR BANKING SYSTEM");
                        System.out.println("Exiting System!!!");
                        return;
                    default:
                        System.out.println("Invalid choice");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}

