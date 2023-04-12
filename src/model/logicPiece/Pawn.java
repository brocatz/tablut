package model.logicPiece;

import java.util.HashSet;

public class Pawn extends TablutPiece{

	private static HashSet<Integer> list_restrictedMove;
	
	public Pawn() {
		super();	
	}
	
	
	public static boolean is_moveRestricted(int newKey) {
		return list_restrictedMove.contains(newKey);
	}
	
	// The places where the pawn can't take position on the board
	static {
		list_restrictedMove = new HashSet<Integer>();	
		list_restrictedMove.add(0); // TOP LEFT
		list_restrictedMove.add(8); // TOP RIGHT
		list_restrictedMove.add(40); // MIDDLE
		list_restrictedMove.add(72); // BOTTOM LEFT
		list_restrictedMove.add(80); // BOTTOM RIGHT
	}
}
