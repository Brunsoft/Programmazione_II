package model;

public class Dama extends Pedina {

	public Dama (boolean colore) {
		super (colore);
	}
	
	public Dama (Pedina other) {
		super (other.colore);
	}
}
