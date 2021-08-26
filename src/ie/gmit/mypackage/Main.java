package ie.gmit.mypackage;

import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The Main class holds the main method.
 */
public class Main extends Application {

PlayerManager sm = new PlayerManager(); // Used for managing players
	
	@Override
	public void start(Stage primaryStage) {
		
		int noOfCmdLineArgs = 0;	// Used to set stage title
		String cmdLineArgs = null;	// Used to set stage title
		
		/* Preparing the Scenes */
		// Create gridpane node to use as root node of scene and to arrnage child nodes logically
		GridPane gridPane1 = new GridPane();
		// Create child nodes
		// Create Text node for top of scene 1
		Text txtHeader = new Text("Please select an option below:");
		// Create button and TextField for Loading DB
		Button btnLoadPlayerList = new Button("Load Players from File");
		TextField tfLoadPlayerFilePath = new TextField();
		tfLoadPlayerFilePath.setPromptText("Path to Player File");
		// Add Player Button and text fields
		Button btnAddPlayer = new Button("Add Player");
		TextField tfPlayerName = new TextField();
		tfPlayerName.setPromptText("Player Name");
		TextField tfPlayerClub = new TextField();
		tfPlayerClub.setPromptText("Club");
		TextField tfPlayerNationality = new TextField();
		tfPlayerNationality.setPromptText("Nationality");
		TextField tfPlayerAge = new TextField();
		tfPlayerAge.setPromptText("Age");
		// Delete Player
		Button btnDelPlayer = new Button("Delete Player");
		TextField tfPlayerDel = new TextField();
		tfPlayerDel.setPromptText("Player No.");
		// Show total number of Player
		Button btnShowTotal = new Button("Show Total Players");
		// Show total number of Players
		Button btnShowPlayerList = new Button("Show Players List");
		// Save Players to file
		Button btnSavePlayerList = new Button("Save Player List");
		TextField tfSavePlayerFilePath = new TextField();
		tfSavePlayerFilePath.setPromptText("Path to Player File");
		// Add Quit button
		Button btnQuit = new Button("Quit");	
		// Create TextArea node for bottom of scene 1 to display output
		TextArea taMyOutput = new TextArea();
		
		// Adding and arranging all the nodes in the grid - add(node, column, row)
		gridPane1.add(txtHeader, 0, 0);
		gridPane1.add(btnLoadPlayerList, 0, 1);
		gridPane1.add(tfLoadPlayerFilePath, 1, 1);
		gridPane1.add(btnAddPlayer, 0, 2);
		gridPane1.add(tfPlayerName, 1, 2);
		gridPane1.add(tfPlayerClub, 2, 2);
		gridPane1.add(tfPlayerNationality, 3, 2);
		gridPane1.add(tfPlayerAge, 4, 2);
		gridPane1.add(btnDelPlayer, 0, 3);
		gridPane1.add(tfPlayerDel, 1, 3);
		gridPane1.add(btnShowTotal, 0, 4);
		gridPane1.add(btnShowPlayerList, 0, 5);
		gridPane1.add(btnSavePlayerList, 0, 6);
		gridPane1.add(tfSavePlayerFilePath, 1, 6);
		gridPane1.add(btnQuit, 0, 7);
		gridPane1.add(taMyOutput, 0, 8, 5, 1);
		
		// Adding events to buttons
		// Load Players DB button
		btnLoadPlayerList.setOnAction(e -> {

			if (tfLoadPlayerFilePath.getText().trim().equals("")) { // If text field is empty
				taMyOutput.setText("Please enter path to Player file.\n");
			} else {
				
				File playerObjectsFile = new File(tfLoadPlayerFilePath.getText());
				sm = sm.loadPlayerManagerObjectFromFile(playerObjectsFile);
				if (sm == null) {
					sm = new PlayerManager();
					taMyOutput.setText("ERROR: DB path " + tfLoadPlayerFilePath.getText() + " does not exist\n");
					taMyOutput.appendText("Please check DB path and try again");
					tfLoadPlayerFilePath.clear();
				} else {
					taMyOutput.setText("DB loaded successfully from " + tfLoadPlayerFilePath.getText());
					tfLoadPlayerFilePath.clear();
				}
			}

		});
		
		// Add Player button action
		btnAddPlayer.setOnAction(e -> {

			// If any of the player fields are empty print prompt message
			if (tfPlayerName.getText().trim().equals("") || 
					tfPlayerClub.getText().trim().equals("") ||
					tfPlayerNationality.getText().trim().equals("") ||
					tfPlayerAge.getText().trim().equals("")) { 
				taMyOutput.setText("Please enter ALL Player details\n");
			} else {
				// Create new Player with information in text fields
				try {
					Player newPlayer = new Player(tfPlayerName.getText(), tfPlayerClub.getText(), tfPlayerNationality.getText(), Integer.parseInt(tfPlayerAge.getText()));
					this.sm.addPlayer(newPlayer); // Add Player to Player list
					// Print success message
					taMyOutput.setText(newPlayer.getPlayerName() + " " + newPlayer.getclub() + " has been added to the Player list");
				
					// Clear input fields
					tfPlayerName.clear();
					tfPlayerClub.clear();
					tfPlayerNationality.clear();
					tfPlayerAge.clear();
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
					taMyOutput.setText("Please enter a number for Player Age");
				}

			}

		});
		
		// Delete Player button action
		btnDelPlayer.setOnAction(e -> {
			
			if (tfPlayerDel.getText().trim().equals("")) { // If text field is empty
				taMyOutput.setText("Please enter the Player Number you want to delete");
			} else {
				Player removedPlayer;
				removedPlayer = sm.deletePlayerByAge(Integer.parseInt(tfPlayerDel.getText()));
				if (removedPlayer != null) {
					taMyOutput.setText(removedPlayer.getPlayerName() + " " + removedPlayer.getclub() + " has been removed from the Player list!");
					tfPlayerDel.clear();
				} else {
					taMyOutput.setText("Player " + tfPlayerDel.getText() + " not found\n");
					taMyOutput.appendText("No Player deleted!");
					tfPlayerDel.clear();
				}
			}

		});

		// Show total number of Players
		btnShowTotal.setOnAction(e -> {

			int totalPlayers = 0;
			// Find total Players
			totalPlayers = sm.findTotalPlayers();
			taMyOutput.setText("Current Total Players: " + Integer.toString(totalPlayers));
		
		});
		
		
		btnSavePlayerList.setOnAction(e -> {

			if (tfSavePlayerFilePath.getText().trim().equals("")) { // If text field is empty
				taMyOutput.setText("Please enter path to Player List.\n");
			} else {
				File playerListFile = new File(tfSavePlayerFilePath.getText());
				try {
					sm.savePlayerManagerObjectToFile(playerListFile);
					taMyOutput.setText("Player list saved!");
					tfSavePlayerFilePath.clear();
				} catch (Exception exception) {
					System.out.print("[Error] Cannont save DB. Cause: ");
					exception.printStackTrace();
					taMyOutput.setText("ERROR: Failed to save Players DB!");
				}
			}

		});
		
		// Quit button action
		btnQuit.setOnAction(e -> Platform.exit());
		
	
		// Create scene and add the root node i.e. the gridpane
		Scene scene1 = new Scene(gridPane1, 600, 450);
		// Preparing the Stage (i.e. the container of any JavaFX application)
        
		// Set Stage Title
		
		// Find number of command line arguments supplied 
		noOfCmdLineArgs = getParameters().getRaw().size();
		// If command line arguments have been provided then set the title to 
		// them. If none were provided then set title to default value.
		if (noOfCmdLineArgs > 0) {
			// Get command line arguments as String
			cmdLineArgs = getParameters().getRaw().toString();
			// Remove unwanted characters ([ and ] and ,)from string
			cmdLineArgs = cmdLineArgs.replaceAll("\\[|\\]|\\,", "");
			primaryStage.setTitle(cmdLineArgs);
		} else {
			// Default value
			primaryStage.setTitle("Player Manager Application");
		}
		
		// Setting the scene to Stage
		primaryStage.setScene(scene1);
		// Displaying the stage
		primaryStage.show();
		
		
	}// End Start Method
	

	public static void main(String[] args) {
		launch(args);
	}
	
}//End Main method

