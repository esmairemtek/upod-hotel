package services;

import dao.contract.UserDao;
import dao.ex.DaoException;
import dao.ex.NoSuchUserException;
import dao.impl.UserDaoJdbc;
import model.Customer;
import model.Manager;
import model.User;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserService {
    static UserDao userDao = new UserDaoJdbc();
    public static void start() {
        Scanner scanner = new Scanner(System.in);
        boolean menuLoop = true;
        while (menuLoop) {
            System.out.println("""
                    ---- UPOD HOTEL ----
                    Welcome! Please choose an option.
                    (1) Register
                    (2) Login
                    (3) Exit""");
            String[] options = {"1", "2", "3"};
            String option = getValidOption(scanner, "", options);

            switch (option) {
             case "1" -> handleRegistration(scanner);
             case "2" -> handleLogin(scanner);
             case "3" -> {
                    System.out.println("Have a nice day!..");
                    menuLoop=false;}
            }
        }
        scanner.close();
    }

    private static void handleLogin(Scanner scanner) {
        System.out.println("--- LOGIN ---\nPlease enter your credentials.");
        String email = getValidEmail(scanner);
        System.out.print("Password: " );
        String password = scanner.nextLine();

        try {
            User user = userDao.findByAuth(email,password);

            if (user instanceof Customer) {
                CustomerService.handleCustomerMenu(scanner, user);
            } else if (user instanceof Manager) {
                ManagerService.handleManagerMenu(scanner, user);
            }

        } catch (NoSuchUserException e) {
            System.out.print("You entered invalid credentials. Enter 'y' to try again and 'n' to go back to welcome menu: ");
            String[] options = {"y", "Y", "n", "N"};
            String option = getValidOption(scanner,"", options);
            if (option.equalsIgnoreCase("y")) {
                handleLogin(scanner);
            }
        } catch ( DaoException | NoSuchAlgorithmException e) {
            System.out.println("Error during login: " + e.getMessage());
        }
    }

    private static void handleRegistration(Scanner scanner) {
        System.out.println("--- REGISTER ---\nPlease enter your credentials.");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        String email = getValidEmail(scanner);
        System.out.print("Password:" );
        String password = scanner.nextLine();

        try {
            userDao.save(new User(name,email,password));
            System.out.println("Registration successful!");
        } catch (DaoException | NoSuchAlgorithmException e) {
            System.out.println("Error during registration: " + e.getMessage());
        }
    }

    private static String getValidEmail(Scanner scanner) {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

         if (!emailPattern.matcher(email).matches()) {
             System.out.println("Please enter a valid email. It should be in the example@example.com format.");
             getValidEmail(scanner);
        }
        return email;
    }

    static String getValidOption(Scanner scanner, String printStatement, String[] options) {
        System.out.print(printStatement);
        String option = scanner.nextLine();

        if (!Arrays.asList(options).contains(option)) {
            System.out.println("You entered an invalid option. Try again.");
            getValidOption(scanner,printStatement,options);
        }
        return option;
    }
    public static void main(String[] args) {
        start();
    }
}

