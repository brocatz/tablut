package model.playerLogic;

import java.util.Collection;
import java.util.HashSet;

import model.logicPiece.TablutPiece;

public class Attacker extends Player {
	
	private HashSet<TablutPiece> list_attacker;
	
	
	public Attacker() {
		super();
		this.initiliase();
		
	}
	
	public Collection getListAttacker() {
		return this.list_attacker;
	}
	
	private void initiliase() {
		list_attacker = new HashSet<TablutPiece>();
	}
	
}
