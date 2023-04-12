package view.tablutboard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;
import model.gameLogic.GameLogic;
import model.logicPiece.King;
import model.logicPiece.Pawn;
import model.logicPiece.TablutPiece;

// A logic board on top of the visual Board
// This code was reorganized due that we can t use images as background
// This code does validation in there then lets the gameBoard do the rest of the work

public class Board extends JPanel /* implements MouseListener */{
	
	private BufferedImage img_king;
	private BufferedImage img_blackPawn;
	private BufferedImage img_whitePawn;
	
	private HashMap<Integer, TablutPiece> logicBoard;
	private HashSet<TablutPiece> list_attacker;
	private HashSet<TablutPiece> list_defender;
	
	private int lockPostion = -1;
	private int positionSelected = -1;
	
	private GameLogic gameLogic; // SingleTon
	
	// The board will also get  the column and row and transforme
	// Those values into a key (from 0 to 80 )
	
	boolean is_positionOccupied = false;
	
	
	int selectedrow;
	int selectedcolumn;
	int selectedKey;
	
	JLabel lbl_selected;
	
	public Board() {
		this.setDefaultOptionForJPanel();
		this.createBufferImageForBoard();
		this.linkGameLogicToBord();
		this.redifineAddMouseListener();
		this.setBoardReference();
		
	}
	

	// Pour redimensioner l'image....
		// De stackOverFlow
		// https://stackoverflow.com/questions/14548808/scale-the-imageicon-automatically-to-label-size
	 private static BufferedImage resize(BufferedImage image, int width, int height) {
		   BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
		    Graphics2D g2d = (Graphics2D) bi.createGraphics();
		    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		    g2d.drawImage(image, 0, 0, width, height, null);
		    g2d.dispose();
		    return bi;
		}
	
	public void resetLockedPosition() {
		lockPostion = -1;
		positionSelected = -1;
	}
	 
	public void  setBoardReference() {
		Controller<Board> controller = new Controller<Board>(this);
	}
	 
	 
	//
	// Nous passons la collection dans le paint Componment 
	//
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);	
		
		// Here we should pass the collection than
		// FIll the label witht the imageIcom
		
		// For testing 
		
		
		
		// Avoiding errors
		if (!this.logicBoard.isEmpty()) {
			
			// We display the visual effet on a component
			//JLabel lbl_temp;
			
			if (this.lockPostion >-1) {
				
				Integer keyCol = gameLogic.convertKeyToColumn(this.lockPostion);
				Integer keyRow = gameLogic.convertKeyToRow(this.lockPostion);
				
				Integer  xPos = gameLogic.convertRowToCoord(keyCol);
				Integer yPos = gameLogic.convertToColCoord(keyRow);
				
				g.setColor(Color.GREEN);
				
				g.fillRect(xPos, yPos, 50, 50);
			}
			
			
			// Iterate throught the keys  the HashMap 
			Iterator<Integer> iter_tempKeyList = this.logicBoard.keySet().iterator();
			
			while (iter_tempKeyList.hasNext()) {
				Integer tempkey = iter_tempKeyList.next();
				Integer keyCol = gameLogic.convertKeyToColumn(tempkey);
				Integer keyRow = gameLogic.convertKeyToRow(tempkey);
				
				Integer  xPos = gameLogic.convertRowToCoord(keyCol);
				Integer yPos = gameLogic.convertToColCoord(keyRow);
				
				
				TablutPiece tempTablutPiece = this.logicBoard.get(tempkey);
				
				
				// We need to check who the tempPiece belongs to
				
				// Attacker
				if (this.list_attacker.contains(tempTablutPiece)) {
					
					// We get the key and Transform it into a position
					// Remember we start at 0 to 80
					//lbl_temp.setIcon(new ImageIcon(img_blackPawn));
					g.drawImage(img_blackPawn, xPos, yPos, 50, 50, null);
					
				
				}
				
				// Defender
				if (this.list_defender.contains(tempTablutPiece)) {
					if (tempTablutPiece.getClass()== Pawn.class) {
						//lbl_temp.setIcon(new ImageIcon(img_whitePawn));
						g.drawImage(this.img_whitePawn, xPos, yPos, 50, 50, null);
					}
					if (tempTablutPiece.getClass().getName() == King.class.getName()) {
						//lbl_temp.setIcon(new ImageIcon(img_king));
						g.drawImage(this.img_king, xPos, yPos, 50, 50, null);
					}
					
				}
				
			}
 		}
		
		if (this.gameLogic.getWinner() > 0) {
			if (this.gameLogic.getWinner() == 1) {
				this.drawAssaliantAsWinner(g);
			} 
			if (this.gameLogic.getWinner() == 2) {
				this.drawDefenderAsWinner(g);
			}
		}
		
		this.drawStrokedLine(g);
	}
	
	
	
	private void setDefaultOptionForJPanel () {
		this.setPreferredSize(new Dimension(450,450));
		this.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		this.setOpaque(false); // pour la transparence
		
	}

	
	private void createBufferImageForBoard() {
		try {
			img_king = ImageIO.read(new File("src/image/king.png"));
			img_blackPawn = ImageIO.read(new File("src/image/blackPawn.png"));
			img_whitePawn = ImageIO.read(new File("src/image/whitePawn.png"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// The first resize of the image is when the game starts
		img_whitePawn = Board.resize(this.img_whitePawn, 50, 50);
		img_blackPawn = Board.resize(this.img_blackPawn, 50, 50);
		img_king = Board.resize(this.img_king, 50, 50);
	}
	
	private void linkGameLogicToBord () {
		gameLogic = GameLogic.getGameLogicInstance();
		this.list_attacker = gameLogic.getList_attacker();
		this.list_defender = gameLogic.getList_defender();
		this.logicBoard	= gameLogic.getLogicBoard();
		
	}
	
	
	// Stack OverFlow
	// How to pass parameters to anonymous class ?
	private void redifineAddMouseListener() {
		this.addMouseListener(new MouseAdapter () {

			TablutPiece lockedPiece;
			
			boolean is_positionOccupied = false;
			
			int selectedrow;
			int selectedcolumn;
			int selectedKey;
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				
				// The user can only left Click (Left Press)
				if (MouseEvent.BUTTON1 != arg0.getButton())
					return;
				
				
				int xCoord = arg0.getX();
				int yCoord = arg0.getY();
			
				int xPos = xCoord / 50;
				int yPos = yCoord / 50;
								
				int tempkey = gameLogic.convertPositionToKey(xPos, yPos);

				// Locked a label if none is selected
				if  (positionSelected == -1  ) {
					
					positionSelected = tempkey;
					lockPostion = positionSelected;
					lockedPiece = logicBoard.get(lockPostion);
					
					this.selectedcolumn= xPos;
					this.selectedrow = yPos;
					this.selectedKey = tempkey;
					
					this.is_positionOccupied = gameLogic.is_positionOccupied(tempkey);
					
				}
				else if ( tempkey == positionSelected ) {			
					positionSelected = -1;
					lockPostion = -1;
					lockedPiece = null;
				}
				
				// If the position the user click on has an object 
				// and it the current Player turn
				if (this.is_positionOccupied && gameLogic.getCurrentPlayer().contains(lockedPiece)) {
					
					// XOR Operator 
					// IF the selectedPosition has the same row or column as the newPosition
					// .. (Attention but not both ..because that would be the same position
					if  ( this.selectedcolumn == xPos ^  this.selectedrow ==  yPos) {
						int newKey = gameLogic.convertPositionToKey(xPos, yPos);
						boolean is_moveConfirmed = gameLogic.moveTablutToDestination(this.selectedKey, newKey);

						if (is_moveConfirmed) { 
							gameLogic.tryRemoveTablutPieceFromBoard(newKey);		
							lockPostion = -1;
							lockedPiece = null;
							positionSelected = -1;
							gameLogic.switchPlayer();
					    }
						else {
								
						}
					}
						
				}
				
				repaint(); // We need to repaint after each mouse Pressed
				// And we need to check certain condition 
				gameLogic.checkIfKingHasEscape(tempkey);
				gameLogic.checkIFGameIsOver();
			}
			
		});
	}
	
	public void drawAssaliantAsWinner(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 150, 450, 150);

		Font font = new Font("Monospaced", Font.BOLD, 30);
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("The Attackers have won", 25, 220);
		
	}
	
	public void drawDefenderAsWinner(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 150, 450, 150);
		
		Font font = new Font("Monospaced", Font.BOLD, 30);
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("The Defenders have won", 25, 220);
	}
	
	// the stroke code  was taken directly from here 
	// https://docs.oracle.com/javase/tutorial/2d/geometry/strokeandfill.html
	public void drawStrokedLine( Graphics g) {
		float dash1[] = {10.0f};
		BasicStroke dashed = new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,
											1.0f, dash1, 0.0f);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		g2.setStroke(dashed);
		// Draw dashed lines vertically
		for (int i = 1 ; i <=8; i++) {
		   g2.drawLine(50 * i, 0, 50 * i, 450);
		}
		
		// Draw dashed lines horizobtally
		for (int j = 1 ; j <=8; j++) {
			g2.drawLine(0, 50 * j, 450, 50 * j);
		}
		g2.dispose();
	}
	
}
