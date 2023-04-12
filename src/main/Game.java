package main;

import model.gameLogic.GameLogic;
import view.MainWindow;

public class Game {

	public static void main (String [] args) {
		Game.startGame();
	}
	
	
	public static void startGame () {
		MainWindow window = new MainWindow("Tablut");
	}
}
