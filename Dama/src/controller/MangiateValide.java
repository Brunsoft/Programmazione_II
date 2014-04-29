package controller;

import java.util.SortedSet;
import java.util.TreeSet;

import model.Damiera;
import model.Mossa;

public class MangiateValide {
	
	public TreeSet<Mossa> trovaMangiateBianche(Damiera damiera) {
		TreeSet<Mossa> mangiate = new TreeSet<Mossa>();
		SortedSet<Mossa> mangiateBianche = new TreeSet<Mossa>();
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++)
				if (!damiera.getCella(i,j).getColore() && !damiera.getCella(i,j).isEmpty() && damiera.getCella(i,j).getColorePedina()) {		// se pedina bianca
					MosseValide mosseBianche = new MosseValide(damiera,i,j,true);																// calcola le mosse bianche possibili
					
					for (Mossa m:mosseBianche)										// scorro le mosse bianche
						if (m.getPercorso() != null)								// se è un movimento
							mangiateBianche.add(m);									// aggiunge la mossa alla lista
				}
		
		if (!mangiateBianche.isEmpty()) {											// se ci sono mosse bianche
			Mossa migliore = mangiateBianche.last();								// prendo la mossa migliore
			for (Mossa m:mangiateBianche)											// scorro le mosse bianche
				if (m.mangiateUguali(migliore))										// se la mangiata è equivalente alla migliore
					mangiate.add(m);												// aggiunge la mossa alla lista
		}
		return mangiate;															// ritorna la lista
	}
}
