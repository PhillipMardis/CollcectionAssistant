import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.*;

public class CollectionAssistant {
    String username;
    ConcurrentHashMap<String, Transformer> myCollection;
    public static User currentUser;

    public static Scanner scanner = new Scanner(in);
    private UserLoginSignUp userLoginSignUp;
    public static Connection connection;

    public CollectionAssistant() {
    }



    public void runCollectionAssistant() throws SQLException {
        out.println("-------- Welcome To The Transformers Collection Assistant! --------");
        out.println("----------------------------------------------------------------");



        String choice;
        boolean shouldContinue = true;
        do {
            out.println("------------------------- MAIN MENU ----------------------------");
            out.println("--- Manage [I]nventory --- Manage [C]ollection --- [Q]uit --- >");
            choice = scanner.nextLine().trim().toUpperCase();
            switch (choice) {
                case "I" -> shouldContinue = ManageInventory.runManageInventory();
                case "C" -> shouldContinue = ManageCollection.runManageCollection();
                case "Q" -> {
                    out.println("Goodbye");
                    shouldContinue = false;
                }
                default -> out.println(("Invalid Choice"));
            }
        } while (shouldContinue);
    }

    public static void main(String[] args) throws SQLException {
        DBConnector dbConnector = new DBConnector();

        try {
            dbConnector.connect();

            // program workflow
            UserLoginSignUp loginSignUp = new UserLoginSignUp(connection, scanner);
            loginSignUp.runUserLoginSignUp();
            CollectionAssistant collectionAssistant = new CollectionAssistant();
            CollectionAssistant.scanner = new Scanner(in);
            collectionAssistant.runCollectionAssistant();

        } catch (SQLException e){
            out.println("Error connecting to the database: " + e.getMessage());
        } finally {
            dbConnector.disconnect();
        }
    }
}
