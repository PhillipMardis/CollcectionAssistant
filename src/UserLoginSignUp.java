import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static java.lang.System.*;

public class UserLoginSignUp {

    private Connection connection;
    private Scanner scanner;
    public boolean IsLoggedIn;

    public UserLoginSignUp(Connection connection, Scanner scanner) {
        this.connection = DBConnector.connection;
        this.scanner = CollectionAssistant.scanner;
    }


    public void loginUser() {
        IsLoggedIn = false;
        String choice = "";
        do {
            out.println("Enter your username or press [B] to go back:  ");
            String username = CollectionAssistant.scanner.nextLine();

            if (username.equalsIgnoreCase("B")) {
                return; // Go back to the initial login/sign-up choice
            }

            try {
                // Check if the username exists in the users table
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // create an instance of User as set as current user
                    int userId = resultSet.getInt("user_id");
                    boolean isDealer = resultSet.getBoolean("is_dealer");
                    User loggedInUser = new User(userId, username, isDealer);
                    CollectionAssistant.currentUser = loggedInUser;

                    IsLoggedIn = true;
                    out.println("Logged in as: " + username);
                } else {
                    out.println("Username does not exist.");
                    out.println("Press [B] to go back or any other key to retry.");
                    choice = CollectionAssistant.scanner.nextLine().toUpperCase();
                }
            } catch (SQLException e) {
                out.println("Error: " + e.getMessage());
            }
        } while (!IsLoggedIn && !choice.equalsIgnoreCase("B"));
    }

    public void signUpUser() {
        IsLoggedIn = false;
        do {
            out.println("Create your username: ");
            String username = CollectionAssistant.scanner.nextLine().trim();
            //check for blank username
            if (username.isEmpty()) {
                out.println("Invalid username. Please enter a valid username.");
                return;
            }
            List<String> dealerOptions = new ArrayList<>();
            dealerOptions.add("Y");
            dealerOptions.add("N");
            String dealerChoice;
            do {
                out.println("Are you a dealer? [Y]es/[N]o");
                dealerChoice = CollectionAssistant.scanner.nextLine().toUpperCase();
                if (!dealerOptions.contains(dealerChoice)) {
                    out.println("Invalid choice");
                }
            } while (!dealerOptions.contains(dealerChoice));
            boolean isDealer = dealerChoice.equals("Y");

            try {
                // Check if the username already exists in the users table
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();


                if (resultSet.next()) {
                    System.out.println("Username already exists. Please choose a different username.");
                } else {
                    // Insert the new user into the users table
                    PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO users (username, is_dealer) VALUES (?, ?)");
                    insertStatement.setString(1, username);
                    insertStatement.setBoolean(2, isDealer);
                    insertStatement.executeUpdate();

                    // retrieve newly created user_id
                    try {
                        statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
                        statement.setString(1, username);
                        resultSet = statement.executeQuery();

                        if (resultSet.next()) {
                            int userId = resultSet.getInt("user_id");
                            // create an instance of User as set as current user
                            User newUser = new User(userId, username, isDealer);
                            CollectionAssistant.currentUser = newUser;

                            String dealer = isDealer ? "Yes" : "No";
                            System.out.println("Signed up successfully. Username: " + username + ", Dealer: " + dealer);
                            IsLoggedIn = true;
                        }
                    }  catch (SQLException e) {
                        System.out.println("Error: " + e.getMessage());
                    }

                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (!IsLoggedIn);
    }

    public User getCurrentUser() {
        return CollectionAssistant.currentUser;
    }

    public void runUserLoginSignUp() {
        List<String> options = new ArrayList<>();
        options.add("L");
        options.add("S");
        options.add("Q");
        String choice;
        do {
            System.out.println("------- User Login/Sign-Up -------");
            System.out.println("--- [L]ogin --- [S]ign-Up --- [Q]uit ---");

            choice = CollectionAssistant.scanner.nextLine().toUpperCase();

            switch (choice) {
                case "L" -> loginUser();
                case "S" -> signUpUser();
                case "Q" -> System.out.println("Goodbye");
                default -> System.out.println("Invalid choice");
            }
        }
            while (!options.contains(choice) || !IsLoggedIn);
        }
    }

