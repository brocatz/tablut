package view;

// La vue principale qui utilisara un border Layout  de 4 element invisible 
// 
// Les coordonnees de l'image ont etes calcule approximatibement a partir de
// Ce site qui n est pas securitaire
// http://imagemap-generator.dariodomi.de

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

import model.gameLogic.GameLogic;
import view.tablutboard.Board;

public class GameFrame extends JPanel{
	
	Board board;	
	GameLogic gameLogic;
	
	int squareWidth = 50; // The width where a TablutPiece can be
	int squareHeight = 50;

	
	public GameFrame () {
		// Creates the game logic to 
		
		gameLogic = GameLogic.getGameLogicInstance();
		this.setDefaultOptionForJPanel();
		this.createGameBoard();
		this.add(board);
		
		
		// Creates the game logic to 
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.drawBackground(g);
		
		
	}
	
	private void setDefaultOptionForJPanel () {
		this.setPreferredSize(new Dimension(450,450));
		this.setLayout(new BorderLayout()); 
		// BorderLayout so that the child  Board.class
		// will fill the whole Parent
	}
	
	private void createGameBoard () {
		board = new Board();
	}
	
	
	public void drawBackground(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getWidth());
		
		
		// Red
		// All 4 Forteresse
		g.setColor(new Color(232,22,34));
		g.fillRect(0, 0, 50, 50);
		g.fillRect(400,0,50,50);
		g.fillRect(0,400,50,50);
		g.fillRect(400, 400, 50, 50);
		

		// Top Part 
		g.setColor(new Color(72,72,72));
		g.fillRect(150, 0, 150, 50);
		g.fillRect(200, 50, 50, 50);
		
		// Left part
		g.fillRect(0, 150, 50, 150);
		g.fillRect(50, 200, 50, 50);
		
		// Right Part
		g.fillRect(400, 150, 50, 150);
		g.fillRect(350,200, 50,50);
		
		// Bottom Part
		g.fillRect(150, 400, 150, 50);
		g.fillRect(200, 350, 50, 50);
		
		// Middle
		
		g.setColor(new Color(255, 125, 20));
		g.fillRect(200, 200, 50, 50);

	}
}
