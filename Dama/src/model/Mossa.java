package model;

public class Mossa implements Comparable<Mossa> {
	
	Cella inizio, fine;
	int pedineMangiabili = 0;
	private Cella[] percorso;												// null in caso di movimento, contiene la cella iniziale e quella mangiata in caso di mangiata
	
	public Mossa (Cella inizio, Cella fine, Cella[] percorso, int pedineMangiabili) {
		this.inizio = inizio;
		this.fine = fine;
		this.pedineMangiabili = pedineMangiabili;
		this.percorso = percorso;
	}

	public int getPedineMangiabili() {
		return pedineMangiabili;
	}

	public Cella getFine() {
		return fine;
	}

	public Cella getInizio() {
		return inizio;
	}	

	public Cella[] getPercorso() {
		return percorso;
	}
	
	public void setPedineMangiabili (int other) {
		pedineMangiabili = other;
	}
	
	public boolean mangiateUguali (Mossa other) {							
		return ((pedineMangiabili - other.pedineMangiabili) == 0);			// se entrambe mangiano lo stesso numero di pedine
	}
	
	public int compareTo (Mossa other) {									// regola di ordinamento del sortedSet (lista mosse)
		if (pedineMangiabili - other.pedineMangiabili == 0)					// se entrambe mangiano lo stesso numero di pedine
			return 1;
		else
			return (pedineMangiabili - other.pedineMangiabili);
	}
	
	public boolean equals (Mossa other) {									// controlla se due mosse sono uguali
		if (percorso.length != other.getPercorso().length || pedineMangiabili-other.pedineMangiabili != 0)
			return false;
		for (int i=0; i<percorso.length; i++) {
			if (percorso[i].getX() != other.getPercorso()[i].getX() || percorso[i].getY() != other.getPercorso()[i].getY())
				return false;
		}
		return true;
	}
}
