package edu.arizona.FXTemp;

import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/*
 * This file provides the code for the GUI written for the game Boggle
 */
public class BoggleGUI extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private Boggle game;

	// Graphical Nodes
	private TextArea diceTrayArea;
	private TextArea resultArea;
	private TextArea userInputArea;
	private GridPane everything = new GridPane();
	private Button newGame;
	private Button endGame;

	public void start(@SuppressWarnings("exports") Stage stage) {

		layoutWindow(); // Set up window with buttons and labels in row one, textareas in row 2

		Scene scene = new Scene(everything, 900, 330);
		Parent root = scene.getRoot();
		root.setStyle("-fx-font: 13 \"Arial\"; ");
		stage.setScene(scene);
		stage.show();
		setUpHandlers();
		startNewGame();
	}

	private void startNewGame() {
		game = new Boggle();
		userInputArea.setText("");
		userInputArea.setEditable(true);
		game = new Boggle();
		diceTrayArea.setText(game.getBoggleTrayAsString());
		resultArea.setText("Click End Game to see results.");
	}

	private void layoutWindow() {

		// Create a new GridPane, add two Buttons, and add to
		// everything.add(buttonPane, 1, 1);

		GridPane gameButtons = new GridPane();
		newGame = new Button("New Game");
		endGame = new Button("End Game");
		gameButtons.setHgap(5);
		gameButtons.setVgap(5);
		gameButtons.add(newGame, 1, 1);
		gameButtons.add(endGame, 2, 1);
		everything.add(gameButtons, 1, 1);

		everything.setHgap(10);
		everything.setVgap(5);

		diceTrayArea = new TextArea();
		diceTrayArea.setFont(new Font("Courier New", 24));
		diceTrayArea.setMaxHeight(250);
		diceTrayArea.setMaxWidth(250);
		everything.add(diceTrayArea, 1, 2);

		userInputArea = new TextArea();
		userInputArea.setMaxHeight(250);
		userInputArea.setMaxWidth(150);
		userInputArea.setWrapText(true);
		everything.add(userInputArea, 2, 2);

		resultArea = new TextArea();
		resultArea.setMaxHeight(250);
		resultArea.setMaxWidth(425);
		everything.add(resultArea, 3, 2);

		Label inputLabel = new Label("Enter attempts below:");
		inputLabel.setFont(new Font("Arial", 12));
		everything.add(inputLabel, 2, 1);

		Label resultLabel = new Label("Results:");
		resultLabel.setFont(new Font("Arial", 12));
		everything.add(resultLabel, 3, 1);

	}

	private void setUpHandlers() {
		newGame.setOnAction(new NewGameHandler());
		endGame.setOnAction(new EndGameHandler());

	}

	private class NewGameHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			startNewGame();
			diceTrayArea.setText(game.getBoggleTrayAsString());
		}

	}

	private class EndGameHandler implements EventHandler<ActionEvent> {

		private Scanner guesses;
		private Object[] wordsFound;
		private Object[] wordsNotGuessed;
		private Object[] incorrectWords;

		@Override
		public void handle(ActionEvent event) {

			guesses = new Scanner(userInputArea.getText());

			while (guesses.hasNext()) {
				game.addGuess(guesses.next());
			}

			String result = getResult();
			resultArea.setWrapText(true);
			resultArea.setText(result);
			userInputArea.setEditable(false); 
		}

		public String getResult() {
			int index;
			String result = "Your score: " + game.getScore() + "\n\n" + "Words you found:" + "\n";

			wordsFound = game.getWordsFound().toArray();
			for ( index = 0; index < wordsFound.length; index++) {
				result += (wordsFound[index] + " ");
				
			}

			result += "\n\nIncorrect words" + "\n"; 
			incorrectWords= game.getWordsIncorrect().toArray(); 
			for (index = 0; index < incorrectWords.length; index++) {
				result += (incorrectWords[index] + " ");
				
			}
			
			wordsNotGuessed = game.getWordsNotGuessed().toArray();
			result += "\n\nYou could have found " + (wordsNotGuessed.length) + " more words:\n";
			for(index = 0; index< wordsNotGuessed.length; index++) { 
				result += (wordsNotGuessed[index] + " "); 
			}
			
			return result; 
		}

	}

}
