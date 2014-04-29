package model;

public class Cella {
	
	private boolean colore; 														// Bianco = true
	private Pedina pedina;
	private int x, y;																// coordinate cella damiera
	
	public Cella (int x, int y, boolean colore) {
		this.colore = colore;
		this.x = x;
		this.y = y;
		pedina = null;																//  cella vuota
	}
	
	public Cella (Cella other) {			
		this.colore = other.colore;
		if (other.pedina != null)
			if (other.pedina instanceof Dama)										// se è Dama
			this.pedina = new Dama (other.pedina);
			else
				this.pedina = new Pedina (other.pedina);
			else this.pedina = null;
		
		this.x = other.x;
		this.y = other.y;
	}
	
	public Cella (Pedina pedina) {													// aggiunge pedina alla cella
		addPedina(pedina);
		colore = false;
	}
	
	public void copiaCella (Cella cella) {
		cella.colore = this.getColore();
		cella.pedina = new Pedina(this.getColorePedina());
		cella.x = this.x;
		cella.y = this.y;
	}
	
	public boolean isEmpty(){														// se cella è vuota
		return (pedina == null);
	}
	
	public void addPedina (Pedina pedina) {
		this.pedina = pedina;
	}
	
	public void rimuoviPedina() {
		pedina = null;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public boolean getColorePedina() {
		return pedina.getColore();
	}
	
	public Pedina getPedina() {
		return pedina;
	}
	
	public boolean getColore() {
		return colore;
	}
	
	public boolean equals (Cella other) {
		return ( x == other.x && y == other.y );
	}
}


