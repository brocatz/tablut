package model.gameLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import model.logicPiece.King;
import model.logicPiece.Pawn;
import model.logicPiece.TablutPiece;
import model.playerLogic.Attacker;
import model.playerLogic.Defender;

// Represent la logic interne du jeu 
// Ou les mouvement des piece sont valide
//
// Singleton Design Pattern pour passer GameLogic a la vue 

// This class will be create in view.GameFrame
// Will we create the gameFrame 
// The gameLogic should know the dimension of the board 
// each square. in this case it does not (
public class GameLogic {

	private static GameLogic gameLogic;

	boolean is_AssaliantTurn;
	boolean is_DefenderTurn;

	private HashMap<Integer, TablutPiece> logicBoard;
	private HashSet<TablutPiece> list_defender;
	private HashSet<TablutPiece> list_attacker;

	int boardSize = 81;
	int nb_Attacker = 16;
	int nb_Defender = 8; // Excluant le roi 8 pion et 1 roi)

	private Attacker attacker;
	private Defender defender;
	
	private int winner = -1; // If 1 the attacker win , if the the defemder wins
	
	private boolean is_KingDead = false; 

	// This variable is Global due some some complexite
	int kingPositionIfFound = -1; // The king has found been found yet // This will be reset many times
	int kingPosition = -1; // represent the kings position  at all time it the game (when it has been found)
	// DUE TO SOME REFERENCE VALUES COMPLEXITY I DECIDE TO ADD THOSE VARIABLE
	// HERE SO THAT THEY CAN BE GLOBAL
	
	// These are for removing the TablutPiece
	
	HashSet<TablutPiece> setToRemove; // The complete set that we wamt to remove 
	HashSet<TablutPiece> verificationSet; // Is use before adding to the setToRenove
	HashSet<TablutPiece> dominatedPlayer; // Attacker or defender
	HashSet<TablutPiece> nonDominatedPlayer;
	
	public static GameLogic getGameLogicInstance() {
		if (gameLogic == null) {
			gameLogic = new GameLogic();
		}
		return gameLogic;
	}

	private GameLogic() {
		logicBoard = new HashMap<Integer, TablutPiece>();
		this.initiliasePlayerAndLink();
		this.initiliaseGame();
		
	}

	public HashMap<Integer, TablutPiece> getLogicBoard() {
		return logicBoard;
	}

	public HashSet<TablutPiece> getList_defender() {
		return list_defender;
	}

	public HashSet<TablutPiece> getList_attacker() {
		return list_attacker;
	}
	


	public void initiliaseGame() {
		// Reset the player turn when restarting
		this.is_AssaliantTurn = true;
		this.is_DefenderTurn = false;
		
		this.is_KingDead = false;

		// Create a new instance of the when reintialising
		logicBoard.clear();

		// We cannont say
		if (!this.list_attacker.isEmpty()) {
			this.list_attacker.clear();
		}

		if (!this.list_defender.isEmpty()) {
			this.list_defender.clear();
		}

		// Now create all of the Object into the player list
		// And pass them to the them by reference into our HashMap

		//
		// Defender
		for (int i = 1; i <= nb_Defender; i++) {
			if (i == 1) {
				King king = new King();
				this.list_defender.add(king);
				this.logicBoard.put(40, king);
			}

			this.list_defender.add(new Pawn());
		}

		//
		// Attacker
		for (int counter = 1; counter <= nb_Attacker; counter++) {
			this.list_attacker.add(new Pawn());
		}

		// Get the iteratoir for the two HashSet
		Iterator<TablutPiece> iter_attacker = this.list_attacker.iterator();
		Iterator<TablutPiece> iter_defender = this.list_defender.iterator();

		// Adding to the logicBoard all the Attackers in the attacke list
		for (int counter = 0; counter < boardSize; counter++) {

			if ((counter >= 3 && counter <= 5) || (counter >= 35 && counter <= 37) || (counter >= 43 && counter <= 45)
					|| (counter >= 75 && counter <= 77)) {
				if (iter_attacker.hasNext())
					this.logicBoard.put(counter, iter_attacker.next());

			} else if (counter == 13 || counter == 27 || counter == 53 || counter == 67) {
				if (iter_attacker.hasNext())
					this.logicBoard.put(counter, iter_attacker.next());
			}

		}

		// Adding to logicBoard to all the defender
		// Need to watch out of the king !!!
		// And we arleady did early .. we set it with a value of 40 in the logicboard

		// But there will be a more complicated problem
		// When will iterate over the hashmap we will eventually see the king
		// And we need to skip it
	
		for (int counter = 0; counter < boardSize; counter++) {

			if ((counter == 22 || counter == 31) || (counter >= 38 && counter <= 39) || (counter >= 41 && counter <= 42)
					|| (counter == 49 || counter == 58)) {

				if (iter_defender.hasNext()) {
					TablutPiece tempPiece = iter_defender.next();

					if (tempPiece.getClass() == Pawn.class) {
						// Cast the TablutPiece back to Pawn
						this.logicBoard.put(counter, (Pawn) tempPiece);
					} 
					else { // To skip the king 
						if (iter_defender.hasNext()) 
							this.logicBoard.put(counter, (Pawn) iter_defender.next());
					}
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	public void initiliasePlayerAndLink() {
		attacker = new Attacker();
		defender = new Defender();

		this.list_defender = (HashSet<TablutPiece>) defender.getListDefender();
		this.list_attacker = (HashSet<TablutPiece>) attacker.getListAttacker();
	}
	
	
	public void restartGame() {
		this.initiliaseGame();
		winner = -1; // we reset the winner
		kingPosition = -1;// we reset the king position
		kingPositionIfFound = -1;// we haven t found the king yet
	}
	
	public int getWinner() {
		return this.winner;
	}
	

	public  HashSet<TablutPiece> getCurrentPlayer() {
		if (this.is_AssaliantTurn)
			return this.list_attacker;
		if(this.is_DefenderTurn)
			return this.list_defender;
		return null; // will never be null 
	}
	
	public void switchPlayer() {
		if (this.is_AssaliantTurn) {
			this.is_AssaliantTurn = false;
			this.is_DefenderTurn = true;
			
			return; //After we switch the turns we don t need to perform any operation on switch player 
		}
			
		if(this.is_DefenderTurn) {
			this.is_DefenderTurn = false;
			this.is_AssaliantTurn = true;
		}
			
	}
	
	
	///////////////////////////////////
	// POSITION AND KEY CONVERSION ///
	/////////////////////////////////
	public int convertPositionToKey(int xPos, int yPos) {
		return xPos + (yPos * 9);

	}

	// XPos
	public int convertKeyToColumn(int key) {
		int transformKey = key;
		// La premiere ligne va de 0 a 8
		// Alors je cherche un valeur entre 0 et 8
		while (transformKey > 8) {
			transformKey -= 9;
		}

		return transformKey;
	}
	
	

	// YPos
	public int convertKeyToRow(int key) {
		int transformKey = key;
		int counter = 0;

		// Je recherche combien de fois je peux soutraire en hauteur
		// Mais on utilise une cle on doit etre entre 0 et 8
		while (transformKey > 8) {
			transformKey -= 9;
			counter++;
		}

		return counter;
	}
	
	public int convertRowToCoord(int row) {
		return row * 50;
	}
	
	public int convertToColCoord(int col) {
		return col * 50;
	}
	

	/////////////////////////////
	// The rule of GameLogic ///
	///////////////////////////

	public boolean is_positionOccupied(int key) {
		return this.logicBoard.containsKey(key);
	}

	// Move a TablutPiece to specify location if there are no other TablutPiece
	// In path of the move (provoque by the player)
	// Stop certain mouvement ..
	public boolean moveTablutToDestination(int selectedKey, int newKey) {
		// We need to know what Piece.. . because there are restricted mouvement for the King and pawn
		TablutPiece tempPiece = this.logicBoard.get(selectedKey);
		boolean is_mouvementRestricted = false; 

		if ( tempPiece instanceof Pawn) {
			is_mouvementRestricted = Pawn.is_moveRestricted(newKey);
		}

		if (tempPiece instanceof King ) {
			is_mouvementRestricted = King.is_moveRestricted(newKey);
		}
		
		
		if (is_mouvementRestricted)
			return false;

		// We are going to do operation on the selectedKey
		// To know if a next position has a value in the hashMap
		// If the next position doesn t have a value in  the HashMap
		// The mouvement is valid to that position 
		int selectedKeyCopy = selectedKey;

		boolean is_nextHopNotEmpty = false;
		// is_nextHopNotEmpty is false when the next position is empty
		// either in x or y

		int newKeyCol = this.gameLogic.convertKeyToColumn(newKey);
		int newKeyRow = this.gameLogic.convertKeyToRow(newKey);

		int selectedKeyCol = this.gameLogic.convertKeyToColumn(selectedKey);
		int selectedKeyRow = this.gameLogic.convertKeyToRow(selectedKey);

		// This code was simplify
		// The direction is the way we are moving 
		int direction = 0;


		// If the user if user is moving upwards or left
		// Our direction is negative (same as a cartesian Map)
		if (newKey < selectedKey) {
			direction = -1;
		} // If the user is moving downward or right
		else if (newKey > selectedKey) {
			direction = 1;
		}

		// If the new position is on the same column
		// The we move up or down (base on the direction)
		if (newKeyCol == selectedKeyCol) {
			while (selectedKeyCopy != newKey) {
				selectedKeyCopy += 9 * direction ;
				is_nextHopNotEmpty = this.logicBoard.containsKey(selectedKeyCopy);

				if (is_nextHopNotEmpty == true)
					return false; // Invalid mouvement dected
			}
		}

		// If the new position is on the same Row
		// Then we either move left or right (base on the direction) 
		if (newKeyRow == selectedKeyRow) {
			while (selectedKeyCopy != newKey) {
				selectedKeyCopy += 1 * direction;
				is_nextHopNotEmpty = this.logicBoard.containsKey(selectedKeyCopy);

				if (is_nextHopNotEmpty == true)
					return false; // Invalid mouvement detected
			}
		}	

		this.logicBoard.put(newKey, tempPiece);
		this.logicBoard.remove(selectedKey);
		
		return true;
	}
	
	// Will try to remove a TablutPiece from the board if all of the criteria are respected
	// We will need the newKey because we are going to check from that position 
	// If there are any other piece next to the piece that just move
	// At all time we are going to check in 3 different directions
	public void tryRemoveTablutPieceFromBoard( int newKey) {
		
		boolean is_kingFound = false;
		
		// We can eliminate more than one piece at a time 
		setToRemove = new HashSet<TablutPiece>();
		verificationSet = new HashSet<TablutPiece> (); // This set will be use for verification
		int nextHopPosition; // Correspond to the index we are currently checking for an enemny
		Integer sequence = 1; // Has a relationship to varPiece.. 
		// This variable is use to determine whether we should check for a dominate player or non-dominate
		
		dominatedPlayer = null; // The player who will elimate TablutPiece
		nonDominatedPlayer = null; // The player who TablutPiece will be elimnated
		
		// I need to know what the piece it  is (King or Pawn) and which player the piece belongs to
		TablutPiece tempPiece = this.logicBoard.get(newKey);
		/* TablutPiece varPiece; */ // A Piece that can be anything on the logicBoard (assaliant or defender
		
		if (this.list_attacker.contains(tempPiece)) {
			dominatedPlayer = this.gameLogic.list_attacker;
			nonDominatedPlayer = this.gameLogic.list_defender;
		}
			
		
		if (this.list_defender.contains(tempPiece)) {
			dominatedPlayer = this.gameLogic.list_defender;
			nonDominatedPlayer = this.gameLogic.list_attacker;
		}
		
		
		/////////////////////////////////////////////////////
		// For removing a TablutPiece in a simple manner  //
		///////////////////////////////////////////////////
		
		
		for (int iterater = 1 ; iterater<=4; iterater ++) {
			
			PositionIterater.setPosition(newKey);
			sequence = 1; // For the loop
			
			if (iterater == 1) {
				while (PositionIterater.hasLeft()) {
					nextHopPosition = PositionIterater.performLeft();
					boolean result = this.IteratorHelperMethod(sequence, nextHopPosition);
					if (result == false) break;
					sequence ++; 
				}
			}
			
			if (iterater == 2) {
				while (PositionIterater.hasTop()) {
					nextHopPosition = PositionIterater.performTop();
					boolean result = this.IteratorHelperMethod(sequence, nextHopPosition);
					if (result == false) break;
					sequence ++; 
				}
				
			}
			
			
			if (iterater == 3 ) {
				while (PositionIterater.hasRight()) {
					nextHopPosition = PositionIterater.performRight();
					boolean result = this.IteratorHelperMethod(sequence, nextHopPosition);
					if (result == false) break;
					sequence ++;
				}
			}
			
			
			if (iterater == 4) {
			   while (PositionIterater.hasBottom()) {
					nextHopPosition = PositionIterater.performBottom();
					boolean result = this.IteratorHelperMethod( sequence, nextHopPosition);
					if (result == false) break;
					sequence ++; 
				}
			
			}
			
			verificationSet.clear(); // We if the piece is next to a wall , it is going to 
			// add the to the setToRemove ... by clearing the verification set
		}
		
		checkIfDKingSurroudedWithPaw();
		
		///////////////////////////////////////////////////////////////////////////////
		// Checking for the King ... if he is surround by 4 different position only //
		/////////////////////////////////////////////////////////////////////////////
		
		if ( this.kingPositionIfFound > 0) {
		
			int i;
			for ( i  = 1 ; i<= 4; i ++ ) {
				
				int kingPositionFoundCopy = this.kingPositionIfFound;
				PositionIterater.setPosition(kingPositionFoundCopy);
				
				if ( i == 1  && PositionIterater.hasLeft()) {
					kingPositionFoundCopy = PositionIterater.performLeft();
				} 
				if ( i == 2  && PositionIterater.hasRight()) {
 					kingPositionFoundCopy =  PositionIterater.performRight();
				} 
				
				if ( i == 3  && PositionIterater.hasTop()) {
					kingPositionFoundCopy =  PositionIterater.performTop();
				} 
				
                if ( i == 4  && PositionIterater.hasBottom()) {
                	kingPositionFoundCopy =  PositionIterater.performBottom();
                } 
                
                TablutPiece varPiece = this.logicBoard.get(kingPositionFoundCopy);
                if (this.list_attacker.contains(varPiece) == false)
                	i = 6;
                
			}
			
			
			// After the 4 incrementation .. i will stop at 5 
			// so when all 4 corner of the king is check than we know that he is surrounded
			if( i == 5 ) {
				this.is_KingDead = true;
				is_kingFound = true;
				TablutPiece kingPiece = this.logicBoard.get(this.kingPositionIfFound);
				this.setToRemove.add(kingPiece);
			}
			
			//////////////////////////////////////////////////////////
			// Checking to see if the king against one or two wall //
			////////////////////////////////////////////////////////
			
			if (is_kingFound == false) {
				int numberOFWalls = 0;
				int numberOFPawnsFound = 0;
				int j = 1;
				for ( j  = 1 ; j<= 4; j ++ ) {
					
					int kingPositionFoundCopy = this.kingPositionIfFound;
					PositionIterater.setPosition(kingPositionFoundCopy);
					
					if ( j == 1  && PositionIterater.hasLeft() && PositionIterater.is_positionBlockByPawn(PositionIterater.performLeft()) == false) {
						kingPositionFoundCopy = PositionIterater.performLeft() +1; // we are areadly performing the iteration when condition check is true 
						
					} else if (j == 1 ) { numberOFWalls++;}
					if ( j == 2  && PositionIterater.hasRight() && PositionIterater.is_positionBlockByPawn(PositionIterater.performRight()) == false) {
	 					kingPositionFoundCopy =  PositionIterater.performRight() - 1;
					}  else if (j == 2 ) { numberOFWalls++;}
					
					if ( j == 3  && PositionIterater.hasTop() && PositionIterater.is_positionBlockByPawn(PositionIterater.performTop()) == false) {
						kingPositionFoundCopy =  PositionIterater.performTop() + 9;
					} else if (j == 3 ) { numberOFWalls++;}
					
	                if ( j == 4  && PositionIterater.hasBottom() && PositionIterater.is_positionBlockByPawn(PositionIterater.performBottom()) == false) {
	                	kingPositionFoundCopy =  PositionIterater.performBottom() - 9;
	                } else if (j == 4 ) { numberOFWalls++;}
	                
	                TablutPiece varPiece = this.logicBoard.get(kingPositionFoundCopy);
	                if (this.list_attacker.contains(varPiece) == true) {
	                	numberOFPawnsFound ++;
	                }
	                	
				}
				
				if ( numberOFWalls == 1 && numberOFPawnsFound == 3) {
					is_kingFound = true;
					this.is_KingDead = true;
				}
				else if (numberOFWalls == 2 && numberOFPawnsFound == 2) {
					is_kingFound = true;
					this.is_KingDead = true;
				}
				
				if (this.is_KingDead == true) {
    				TablutPiece kingPiece = this.logicBoard.get(this.kingPositionIfFound);
    				this.setToRemove.add(kingPiece);
    				
    			}
    				
			}	
			
		}
		
		
		// Will pieces from the logicboard if there is any Piece to remove
		this.logicBoard.values().removeAll(setToRemove);
		nonDominatedPlayer.removeAll(setToRemove);
		
		
	}
	
	// Simplies the reading of the iteration
	// We can t pass the HashMap into the method
	// It won t work ....
	public boolean IteratorHelperMethod(Integer sequence , Integer nextHopPosition) {
		
		TablutPiece varPiece;
		
		// Need to check if he position 
		// contains an enemy object on odd sequences
		varPiece = this.logicBoard.get(nextHopPosition);
		if ( (varPiece == null || dominatedPlayer.contains(varPiece)) && 
			  sequence % 2 != 0  ) {
			return false; 
		} 
		if ( nonDominatedPlayer.contains(varPiece) && sequence % 2 !=0 
			&& varPiece instanceof King == false ) {
			verificationSet.add(varPiece);
		}
		
		     // DominatedPlayer or wall 
			// I add the elemement one by one
		if ( dominatedPlayer.contains(varPiece) && sequence % 2 == 0 ) {
			setToRemove.addAll(verificationSet);	
		} else  if ( sequence % 2 == 0 ){ 
			return false;  // That mean there there are no dominate player next to 
		}
		
		
		if (varPiece instanceof King ) {
			this.kingPositionIfFound = nextHopPosition;
			this.kingPosition = this.kingPositionIfFound;
		}
			
		// In this case it mean that the varPiece is a nonDimatedPlayer
		// We need to check 
		return true;
	}
	
	
	////////////////////////////////////////////////////
	// Check od king is capture im a complicated way //
	//////////////////////////////////////////////////
	
	public void checkIfDKingSurroudedWithPaw() {
		// We nned the king postion if we found it  a check 
		int defenderPawnPosiiton = -1 ;
		int numberOfAttackers = 0; // must be there to continue the checking
		if (this.logicBoard.get(this.kingPositionIfFound) instanceof King) {
			
		
			int i = 1;
			for ( i = 1 ; i<=4 ; i++) {
				int kingPositionFoundCopy = this.kingPositionIfFound;
				PositionIterater.setPosition(kingPositionFoundCopy);
				if ( i == 1  && PositionIterater.hasLeft()) {
					kingPositionFoundCopy = PositionIterater.performLeft();
				} 
				if ( i == 2  && PositionIterater.hasRight()) {
					kingPositionFoundCopy =  PositionIterater.performRight();
				} 
			
				if ( i == 3  && PositionIterater.hasTop()) {
					kingPositionFoundCopy =  PositionIterater.performTop();
				} 
				
				if ( i == 4  && PositionIterater.hasBottom()) {
					kingPositionFoundCopy =  PositionIterater.performBottom();
				} 
            
				TablutPiece varPiece = this.logicBoard.get(kingPositionFoundCopy);
				if (this.list_defender.contains(varPiece))  {
					defenderPawnPosiiton  = kingPositionFoundCopy;
				} else if (this.list_attacker.contains(varPiece)) {
					numberOfAttackers++;
				}
            
			}
			
			if (numberOfAttackers != 3 || defenderPawnPosiiton < 0 ) {
				return;
			}
			
			// We check the defender pawn to see if is is surround by 3 attacker
		    numberOfAttackers = 0; // must be there to continue the checking
		    boolean is_kingNextToPawn = false;
		    
			for (int  j = 1 ; j<=4 ; j++) {
				int PawnPositionFoundCopy = defenderPawnPosiiton;
				PositionIterater.setPosition( PawnPositionFoundCopy);
				if ( j == 1  && PositionIterater.hasLeft()) {
					 PawnPositionFoundCopy = PositionIterater.performLeft();
				} 
				if ( j == 2  && PositionIterater.hasRight()) {
					 PawnPositionFoundCopy =  PositionIterater.performRight();
				} 
			
				if ( j == 3  && PositionIterater.hasTop()) {
					 PawnPositionFoundCopy =  PositionIterater.performTop();
				} 
				
				if ( j == 4  && PositionIterater.hasBottom()) {
					 PawnPositionFoundCopy =  PositionIterater.performBottom();
				} 
            
				TablutPiece varPiece = this.logicBoard.get( PawnPositionFoundCopy);
				if (varPiece instanceof King )  {
					is_kingNextToPawn = true;
				} else if (this.list_attacker.contains(varPiece)) {
					numberOfAttackers++;
				}
				
				if (numberOfAttackers == 3 && is_kingNextToPawn == true ) {
					this.setToRemove.add(this.logicBoard.get(this.kingPositionIfFound));
					this.winner = 1;
				}
			
			
			}
		}
		
		
	}
	
	
	//////////////////////////////////////////
	// Checking to see if the King has won //
	////////////////////////////////////////
	
	public void checkIfKingHasEscape(int position) {
		if (this.logicBoard.get(position) instanceof King) {
			if (King.is_kingEsacpe(position))
				winner = 2;
		}
	}
	
	public void checkIFGameIsOver() {
		// Attacker win they kill king
		if (this.is_KingDead == true ) {
			this.winner = 1;
		}
		// Defender Win win the king escape or there isn t any more attacker to remove
		// The king will be check in a seperate method
		if ( (this.is_KingDead == false ) &&  this.list_attacker.isEmpty() )  {
			this.winner = 2;
		}
		
	}
	
}


// This class works in parallale with the GameLogic
// Will will need to check if a boundary position Exist
// Before retreiving performing operations 

 
class PositionIterater {
	
	private static final int LOWEST_ROW_COL = 0;
	private static final int HIGHEST_ROW_COL = 8;
	
	private static GameLogic gameLogic;
	
	private static int newKeyPosition_copy;
	private static int newKeyCol_ofCopy;
	private static int newKeyRow_ofCopy;
	
	private static ArrayList<Integer> list_restrictedArea;
	
	
	public static boolean is_positionBlockByPawn  ( int positionToCheck) {
		return list_restrictedArea.contains(positionToCheck);
	}  
	
	public static void setPosition(int newKey) {
		newKeyPosition_copy = newKey;
		newKeyCol_ofCopy = gameLogic.convertKeyToColumn(newKeyPosition_copy);
		newKeyRow_ofCopy = gameLogic.convertKeyToRow(newKeyPosition_copy);
	}
	
	public static boolean hasLeft() {
		return  newKeyCol_ofCopy > PositionIterater.LOWEST_ROW_COL;
	}
	
	public static boolean hasRight () {
		return newKeyCol_ofCopy < PositionIterater.HIGHEST_ROW_COL;
	}
	
	public static boolean hasTop () {
		return newKeyRow_ofCopy > PositionIterater.LOWEST_ROW_COL;
	}
	
	public static boolean hasBottom () {
		return newKeyRow_ofCopy < PositionIterater.HIGHEST_ROW_COL;
	}
	
	public static int performLeft () {
		newKeyPosition_copy -=1;
		newKeyCol_ofCopy = gameLogic.convertKeyToColumn(newKeyPosition_copy);
		return newKeyPosition_copy;
	}
	
	public static int performRight() {
		newKeyPosition_copy +=1;
		newKeyCol_ofCopy = gameLogic.convertKeyToColumn(newKeyPosition_copy);	
		return newKeyPosition_copy;
	}
	
	public static int performTop () {
		newKeyPosition_copy -=9;
		newKeyRow_ofCopy = gameLogic.convertKeyToRow(newKeyPosition_copy);	
		return newKeyPosition_copy;
	}
	
	public static int performBottom() {
		newKeyPosition_copy +=9;
		newKeyRow_ofCopy = gameLogic.convertKeyToRow(newKeyPosition_copy);
		return newKeyPosition_copy;
	}
	
	static {
		gameLogic = GameLogic.getGameLogicInstance();
		list_restrictedArea = new ArrayList<Integer>();
		list_restrictedArea.add(0); // TOP LEFT
		list_restrictedArea.add(8); // TOP RIGHT
		list_restrictedArea.add(40); // MIDDLE
		list_restrictedArea.add(72); // BOTTOM LEFT
		list_restrictedArea.add(80); // BOTTOM RIGHT
	}
  	
}