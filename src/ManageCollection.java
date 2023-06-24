import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static java.lang.System.*;

public class ManageCollection {
    public static boolean runManageCollection()  {
        String choice;
        boolean shouldContinue = true;
        do {
            out.println("What would you like to do? ");
            out.println((" [A]dd to collection, [V]iew figures, [U]pdate figure, [D]elete figure, or go [B]ack:  "));
            choice = CollectionAssistant.scanner.nextLine().trim().toUpperCase();
            switch (choice) {

                case "A" -> addCollectionFigure();
                case "V" -> viewCollectionFigures();
                case "U" -> updateCollectionFigure();
                case "D" -> deleteCollectionFigure();
                case "B" -> {
                    return true;
                }
                default -> out.println(("Invalid Choice"));
            }
        } while (shouldContinue);
        return true;
    }

    private static void addCollectionFigure() {
        try {
            // Check if the connection is closed, and re-establish if necessary
            if (DBConnector.connection.isClosed()) {
                DBConnector.connect();
            }

            } catch (SQLException e) {
            out.println(e);
        }

        try {
            PreparedStatement inventoryStatement = DBConnector.connection.prepareStatement("SELECT item_id, item_name FROM inventory");
            ResultSet inventoryResultSet = inventoryStatement.executeQuery();

            // Display inventory items to the user
            out.println("Here are the available items in the inventory:");
            out.println("");
            while (inventoryResultSet.next()) {
                int itemId = inventoryResultSet.getInt("item_id");
                String itemName = inventoryResultSet.getString("item_name");
                out.println("Item ID: " + itemId + " | Item Name: " + itemName);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        // Prompt the user to enter an item ID
        out.println("Enter the item ID of the item you want to add to your collection:");

        String input = CollectionAssistant.scanner.nextLine();

        if (input.equalsIgnoreCase("B")) {
            return;
        }

        int itemId;
        try {
            itemId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            out.println("Invalid input. Please enter a valid item ID.");
            return;
        }

        // Retrieve the item details based on the item ID
        String query = "SELECT item_id, item_name FROM inventory WHERE item_id = ?";
        try (PreparedStatement statement = DBConnector.connection.prepareStatement(query)) {
            statement.setInt(1, itemId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Retrieve the item name
                String itemName = resultSet.getString("item_name");
                // Retrieve the item ID
                itemId = resultSet.getInt("item_id");
                // Retrieve user ID
                int userId = CollectionAssistant.currentUser.getUserId();
                // Set is_favorite flag to false (default value)
                boolean isFavorite = false;

                // Check if the item already exists in the user's collection
                String checkQuery = "SELECT * FROM user_collections WHERE item_id = ? AND user_id = ?";
                try (PreparedStatement checkStatement = DBConnector.connection.prepareStatement(checkQuery)) {
                    checkStatement.setInt(1, itemId);
                    checkStatement.setInt(2, userId);
                    ResultSet checkResultSet = checkStatement.executeQuery();

                    if (checkResultSet.next()) {
                        out.println("You have already added " + itemName + " to your collection.");
                        return; // Exit the method, no further action needed
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }


                // Add the item to the user_collections table
                try {
                    PreparedStatement insertStatement = DBConnector.connection.prepareStatement("INSERT INTO user_collections (item_id, user_id, is_favorite) VALUES (?, ?, ?)");
                    insertStatement.setInt(1, itemId);
                    insertStatement.setInt(2, userId);
                    insertStatement.setBoolean(3, isFavorite);
                    insertStatement.executeUpdate();
                }  catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }


                out.println(itemName + " has been added to your collection!");
            } else {
                // Invalid item ID, prompt the user to try again or go back
                out.println("Invalid item ID. Please try again or enter 'B' to go back.");
                String choice = CollectionAssistant.scanner.nextLine().trim().toUpperCase();

                if (choice.equals("B")) {
                    // User wants to go back, return to the previous menu
                    return;
                } else {
                    // User wants to try again, recursively call the method
                    addCollectionFigure();
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    private static void deleteCollectionFigure() {
        out.println("Upgrade to Premium Version to unlock this feature. ");
    }

    private static void updateCollectionFigure() {
        out.println("Upgrade to Premium Version to unlock this feature. ");
    }

    private static void viewCollectionFigures() {
        try {
            PreparedStatement statement = DBConnector.connection.prepareStatement("SELECT u.username, u.is_dealer, uc.*, i.item_name from user_collections uc join inventory i on i.item_id = uc.item_id join users u on uc.user_id = u.user_id where uc.user_id = ?");
            statement.setInt(1, CollectionAssistant.currentUser.getUserId());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                out.println("You don't have any figures in your collection yet. :( ");
            } else {
                do {
                    String userName = resultSet.getString("username");
                    String itemName = resultSet.getString(("item_name"));

                    out.println("item name: " + itemName + " | Username: " + userName);
                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }
}
