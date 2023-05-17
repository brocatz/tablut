package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import controller.Controller;
import model.gameLogic.GameLogic;
import view.tablutboard.Board;

public class MenuBar extends JMenuBar implements ActionListener {
	
	GameLogic gameLogic;
	
	JMenu jMenu_file;
	JMenuItem jMenuItem_restartGame;
	JMenuItem jMenuItem_quitGame;

	JSeparator jSeperator_restartGame_and_quitGame;

	
	
	public MenuBar() {
		jMenu_file = new JMenu("File");
		jMenuItem_restartGame = new JMenuItem("Restart Game");
		jMenuItem_restartGame.addActionListener(this);

		jMenuItem_quitGame = new JMenuItem("Quit Game" );
		jMenuItem_quitGame.addActionListener(this);

		jSeperator_restartGame_and_quitGame = new JSeparator();
		
		jMenu_file.add(jMenuItem_restartGame);
		jMenu_file.add(jSeperator_restartGame_and_quitGame);
		jMenu_file.add(jMenuItem_quitGame);

		this.add(jMenu_file);
		

		this.gameLogic = GameLogic.getGameLogicInstance();
		
	}



	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (jMenuItem_restartGame == arg0.getSource()) {
			this.gameLogic.restartGame();
			// Get the actually reference of the board;
			Board board = (Board) Controller.object;
			board.resetLockedPosition();
			board.repaint();
			
		}

		if (jMenuItem_quitGame == arg0.getSource()) {
			System.exit(-1);
		}
		
	}

}
