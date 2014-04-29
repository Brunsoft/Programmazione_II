package controller;

import model.Damiera;
import model.Mossa;

public class Esecutore {
	
	public void esegui (Damiera damiera, Mossa mossa) {									// esegue una mossa su una damiera (reale o virtuale)
	
		if (mossa.getPercorso() == null)												// se è un movimento
			damiera.muovi(mossa.getInizio(), mossa.getFine());							// muove la pedina dalla cella iniziale a quella finale
		else {																			// se è una mangiata
			for (int i = 2; i < mossa.getPercorso().length; i+=2)																						// ciclo utilizzato per mosse multiple consecutive
				damiera.mangia(mossa.getPercorso()[i-2], mossa.getPercorso()[i-1], mossa.getPercorso()[i]);												// mangia la pedina in posizione i-1 e sposta la pedina in posizione i
			damiera.mangia(mossa.getPercorso()[mossa.getPercorso().length-2], mossa.getPercorso()[mossa.getPercorso().length-1], mossa.getFine());		// mangia la pedina in posizione length-1 e sposta la pedina nella cella finale
		}
	}	
}
