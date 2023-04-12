package model.logicPiece;

import java.util.ArrayList;

public class King extends TablutPiece {

	private static ArrayList<Integer> list_fortess;
	//The place where the king can't request a move 
	private static final int RESTRICTED_MOVE = 40;
	
	public static boolean is_moveRestricted(int newKey) {
		return King.RESTRICTED_MOVE == newKey ;
	}
	
	public static boolean is_kingEsacpe(int position) {
		return list_fortess.contains(position);
	}
	
	public King () {
		super();
	}
	
	static {
		list_fortess = new ArrayList<Integer> ();
		list_fortess.add(0); // TOP LEFT
		list_fortess.add(8); // TOP RIGHT
		list_fortess.add(72); // BOTTOM LEFT
		list_fortess.add(80); // BOTTOM RIGHT
	}
}
