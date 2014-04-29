package model;

public class Pedina {
	
	boolean colore; 								// Bianco = true
	
	public Pedina (boolean colore) {
		this.colore = colore;
	}
	
	public Pedina (Pedina other) {
		this.colore = other.colore;
	}
	
	public boolean getColore() {
		return colore;
	}
}
