import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.*;

public class ManageInventory {
    public static boolean runManageInventory() throws SQLException {


        String choice;
        boolean shouldContinue = true;
        do {
            out.println("What would you like to do? ");
            out.println((" [A]dd to inventory, [V]iew figures, [U]pdate figure, [D]elete figure, or go [B]ack:  "));
            choice = CollectionAssistant.scanner.nextLine().trim().toUpperCase();
            switch(choice) {
                case "A" -> shouldContinue = addInventoryFigure();
                case "V" -> shouldContinue = viewInventoryFigures();
                case "U" -> shouldContinue = updateInventoryFigure();
                case "D" -> shouldContinue = deleteInventoryFigure();
                case "B" -> { return true; }
                default -> out.println(("Invalid Choice"));
            }
        } while (shouldContinue);
        return true;
    }
/////// / / /  /   /    /   CREATE  //
    private static boolean addInventoryFigure() throws SQLException {
        CollectionAssistant.scanner = new Scanner(in);

        String name;
        do {
            out.println("Name of figure to add: ");
            name = CollectionAssistant.scanner.nextLine().trim();
            if (name.isEmpty()) {
                out.println("Name cannot be empty.");
            }
        } while (name.isEmpty());

        List<String> scaleChoices = new ArrayList<>(Arrays.asList("L", "M", "MP", "N"));
        String input;
        String scale = "";
        do {
            out.println("Please choose a scale:");
            out.println("---[L]egends---[M]ainline---[MP](Masterpiece)---[N]on-Transforming");
            input = CollectionAssistant.scanner.nextLine().trim().toUpperCase();
            if (!scaleChoices.contains(input)) {
                out.println("invalid choice");
            } else {
                switch (input) {
                    case "L" -> scale = "Legends";
                    case "M" -> scale = "Mainline";
                    case "MP" -> scale = "Masterpiece";
                    case "N" -> scale = "Non-Transforming";
                }
            }
        } while (!scaleChoices.contains(input));

        List<String> factionChoices = new ArrayList<>(Arrays.asList("A", "D", "O"));
        String faction = "";
        do {
            out.println("Please choose a faction:");
            out.println("--- [A]utobot --- [D]ecepticon --- [O]ther ---");
            input = CollectionAssistant.scanner.nextLine().trim().toUpperCase();
            if (!factionChoices.contains(input)) {
                out.println("invalid choice");
            } else {
                switch (input) {
                    case "A" -> faction = "Autobot";
                    case "D" -> faction = "Decepticon";
                    case "O" -> faction = "Other";
                }
            }
        } while (!factionChoices.contains(input));

        String description = "";
        do {
            out.println("Please give a brief description of the figure you are adding (Limit 300 characters): ");
            description = CollectionAssistant.scanner.nextLine().trim();

            if (description.length() > 300) {
                out.println("That is too many characters!");
            } else if (description.equals("")) {
                out.println("You cannot leave this blank.");
            }
        } while (description.length() > 300 || description.isEmpty());

        int addedBy = CollectionAssistant.currentUser.getUserId();

        Transformer transformer = new Transformer(addedBy, name, scale, faction, description);
        transformer.setAddedBy(addedBy);
        transformer.setName(name);
        transformer.setScale(scale);
        transformer.setFaction(faction);
        transformer.setDescription(description);

        DBConnector dbConnector = new DBConnector();

        try {
            Connection connection = dbConnector.connect();
            String query = "INSERT INTO inventory (item_name, scale, faction, description, added_by) VALUES (?,?,?,?,?)";
            PreparedStatement addStatement = connection.prepareStatement(query);
            addStatement.setString(1,transformer.getName());
            addStatement.setString(2, transformer.getScale());
            addStatement.setString(3, transformer.getFaction());
            addStatement.setString(4, transformer.getDescription());
            addStatement.setInt(5, transformer.getAddedBy());
            addStatement.executeUpdate();
            out.println(name + " added to the inventory");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnector.disconnect();
        }
        return true;
    }

/////// / / /  /   /    /    READ   //

    private static boolean viewInventoryFigures() throws SQLException {
        List<String> factionChoices = new ArrayList<>(Arrays.asList("A", "D"));
        String faction = "";
        String choice;
        boolean shouldContinue = true;
        do {
            out.println("Which figures would you like to view? ");
            out.println("--- [A]utobots --- [D]ecepticons --- [ALL] --- or Go [B]ack ---");
            choice = CollectionAssistant.scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "A" -> faction = "Autobot";
                case "D" -> faction = "Decepticon";
                case "ALL" -> faction = "";
                case "B" -> {
                    return true;
                }
                default -> out.println("invalid choice");

            }
            DBConnector dbConnector = new DBConnector();
            String query;

            try {
                Connection connection = dbConnector.connect();
                if (factionChoices.contains(choice)) {
                    query = "SELECT * FROM inventory WHERE faction = '" + faction + "'";
                } else {
                    query = "SELECT * FROM inventory";
                }
                Statement viewStatement = connection.createStatement();
                ResultSet resultSet = viewStatement.executeQuery(query);

                // check if resultSet is empty -----------
                if (!resultSet.isBeforeFirst()) {
                    out.println("There are no figures here, yet.");
                } else {
                    // iterate over the records in the inventory
                    while (resultSet.next()) {
                        int figureId = resultSet.getInt("item_id");
                        String figureName = resultSet.getString("item_name");
                        String figureScale = resultSet.getString("scale");
                        String figureFaction = resultSet.getString("faction");
                        String figureDescription = resultSet.getString("description");

                        out.println("--------------------------");
                        out.println("Item ID: " + figureId);
                        out.println("Name: " + figureName);
                        out.println("Scale: " + figureScale);
                        out.println("Affiliation: " + figureFaction);
                        out.println("--- Description ---");
                        out.println(figureDescription);
                        out.println("--------------------------");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                dbConnector.disconnect();
            }
        } while (shouldContinue);
        return true;
    }

/////// / / /  /   /    /   UPDATE  //

    private static boolean updateInventoryFigure() {
        out.println("Upgrade to Premium Version to unlock this feature. ");
        return true;
    }


/////// / / /  /   /    /   DELETE  //

    private static boolean deleteInventoryFigure() {
        out.println("Upgrade to Premium Version to unlock this feature. ");
        return true;
    }



}