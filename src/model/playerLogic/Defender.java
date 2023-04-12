package model.playerLogic;

import java.util.Collection;
import java.util.HashSet;

import model.logicPiece.TablutPiece;

public class Defender extends Player {
	
	private HashSet<TablutPiece> list_defender;
	
	public Defender() {
		super();
		this.initilise();
	}
	
	public Collection getListDefender() {
		return this.list_defender;
	}
	
	private void initilise () {
		this.list_defender = new HashSet<TablutPiece>();
	}
}
