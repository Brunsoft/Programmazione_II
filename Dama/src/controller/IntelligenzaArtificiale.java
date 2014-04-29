package controller;

import java.util.ArrayList;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import model.*;

public class IntelligenzaArtificiale {											// calcola la miglior mossa

	private SortedSet<Mossa> mosseNereMovimento;								// lista mosse movimento
	private SortedSet<Mossa> mosseNereMangiata;									// lista mosse mangiata
	private Damiera damiera;													// damiera
	private Esecutore esecutore = new Esecutore();								// esegue la mossa su una damiera
	private Cella[] percorso = new Cella[2];									// cella iniziale e cella mangiata
	private Cella[] fine = new Cella[1];										// cella finale
	public static boolean multipla = false;										// mangiata multipla
	public boolean stop = false;												// fine mangiate multiple (usato in PC)

	public IntelligenzaArtificiale (Damiera damiera) {
		this.damiera = damiera;
	}

	public int giocaPC() {														// calcola e fa eseguire la miglior mossa del PC e controlla se il gioco può proseguire
		int x=1;																// valore di ritorno (se resta a 1 l'utente ha perso, se diventa -1 il PC ha perso, se diventa 0 la partita può continuare)
		mosseNereMovimento = new TreeSet<Mossa>();
		mosseNereMangiata = new TreeSet<Mossa>();

		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++)
				if (!damiera.getCella(i,j).getColore() && !damiera.getCella(i,j).isEmpty() && !damiera.getCella(i,j).getColorePedina())			// se pedina nera
					mossaNera(i,j);																												// calcola la miglior mossa per ogni pedina e la aggiunge nella lista corretta

		if (!mosseNereMangiata.isEmpty()){										// se ci sono mangiate
			if (multipla){														// se la mangiata corrente è multipla
				int max = 2;
				for (Mossa m:mosseNereMangiata)																									// cerco la mangiata consecutiva alla precedente
					if (m.getPercorso() != null && m.getPercorso()[0].getX() == fine[0].getX() && m.getPercorso()[0].getY() == fine[0].getY())
						max = m.getPercorso().length;																							// salvo la lunghezza del percorso maggiore

				for (Mossa m:mosseNereMangiata){																								// cerco la mangiata con lunghezza max e mi salvo le celle iniziali della mossa multipla
					if (m.getPercorso() != null && m.getPercorso()[0].getX() == fine[0].getX() && m.getPercorso()[0].getY() == fine[0].getY() && m.getPercorso().length == max){
						
						percorso[0] = m.getPercorso()[0];						// cella iniziale
						percorso[1] = m.getPercorso()[1];						// cella mangiata
						if (m.getPercorso().length > 2){						// se la mossa può continuare
							fine[0] = m.getPercorso()[2];						// cella finale della mossa corrente (intermedia per la mossa multipla)
							multipla = true;									// turno resta al PC
						} else {												// se la mossa è l'ultima
							fine[0] = m.getFine();						 		// cella finale
							multipla = false;
							stop = true;										// turno torna all'utente
						}
					}
				}
			}else
				trovaMangiataMigliore();										// se è la prima mangiata trova la migliore

				esecutore.esegui(damiera, new Mossa(percorso[0], fine[0], percorso, 1));	// esegue la mangiata migliore

		}else if (!mosseNereMovimento.isEmpty()){								// se non ci sono mangiate ma ci sono movimenti
			esecutore.esegui(damiera, mosseNereMovimento.last());				// esegue il miglior movimento
			stop = true;
		}else{																	// non ci sono mosse possibili nella damiera
			stop = true;
			return x=-1;														// il PC ha perso
		}

		for (int i=0; i<8; i++)													// scorro tutta la damiera
			for (int j=0; j<8; j++){
				if (!damiera.getCella(i,j).getColore() && !damiera.getCella(i,j).isEmpty() && damiera.getCella(i,j).getColorePedina()) {	// se pedina bianca
					MosseValide tempBianche = new MosseValide(damiera,i,j,true);															// calcola mosse possibili
					if (!tempBianche.isEmpty())																								// se ci sono mosse
						x=0;																												// l'utente può continuare a giocare
				}
			}
		return x;
		
	}

	private void mossaNera (int x, int y) {										// calcola e sceglie la miglior mossa nera per una pedina
		SortedSet<Mossa> mosseBianche = new TreeSet<Mossa>();					// lista contromosse virtuali bianche per le possibili mosse virtuali nere
		Damiera copia = new Damiera(damiera);									// copia damiera
		MosseValide virtualiNere = new MosseValide(copia, x, y, true);			// lista mosse possibili per la pedina
		if (!virtualiNere.isEmpty())											// se ci sono mosse nere
			for (Mossa virtuale:virtualiNere) {									// scorro le mosse

				copia = new Damiera(damiera);									// creo una copia della damiera
				esecutore.esegui(copia,virtuale);								// esegue la mossa virtuale sulla damiera virtuale
				
				for (int i=0; i<8; i++)
					for (int j=0; j<8; j++) {
						if (!copia.getCella(i,j).getColore() && !copia.getCella(i,j).isEmpty() && copia.getCella(i,j).getColorePedina()) {		// se pedina bianca
							MosseValide tempBianche = new MosseValide(copia,i,j,true);															// calcola mosse bianche possibili
							if (!tempBianche.isEmpty())																							// se ci sono mosse bianche
								mosseBianche.add(tempBianche.migliorMossa());																	// aggiungi la migliore alla lista
						}
					}

				if (!mosseBianche.isEmpty())																// se ci sono mosse bianche
					if (virtuale.getPercorso() == null) {													// se è un movimento
						virtuale.setPedineMangiabili(virtuale.compareTo(mosseBianche.last()));				// calcola le pedine mangiabili
						mosseNereMovimento.add(virtuale);													// aggiungi la mossa nera alla lista dei movimenti
					}
					else {																					// se è una mangiata
						virtuale.setPedineMangiabili(virtuale.compareTo(mosseBianche.last()));				// calcola le pedine mangiabili
						mosseNereMangiata.add(virtuale);													// aggiungi la mossa nera alla lista delle mangiate
					}
				else																						// se non ci sono mosse bianche
					mosseNereMovimento.add(virtuale);														// aggiungi mossa alla lista dei movimenti
			}
	}

	private void trovaMangiataMigliore(){																	// sceglie la miglior mangiata tra quelle possibili (quella con percorso più lungo e quindi con più pedine mangiate)
		int lunghezzaMax = 2;
		for (Mossa m:mosseNereMangiata){																	// cerco la mangiata e mi salvo le celle iniziali della mossa multipla
			if (m.getPercorso() != null && m.getPercorso().length >= lunghezzaMax){
				percorso[0] = m.getPercorso()[0];
				percorso[1] = m.getPercorso()[1];
				if (m.getPercorso().length > 2){
					fine[0] = m.getPercorso()[2];
					multipla = true;
					stop = false;
				}else{
					fine[0] = m.getFine();
					stop = true;
				}
				lunghezzaMax = m.getPercorso().length;
			}
		}
}

	private Mossa movimentoCasuale() { 																		// ritorna una mossa casuale tra i movimenti (problema con ordinamento lista movimenti)
		Mossa migliore = mosseNereMovimento.last();	
		ArrayList <Mossa> movimenti = new ArrayList <Mossa>();
		for (Mossa m:mosseNereMovimento){
			if (m.getPedineMangiabili() == migliore.getPedineMangiabili())
				movimenti.add(m);
		}
		
		Random r = new Random();
		Mossa casuale = movimenti.get(r.nextInt(movimenti.size()));
		return casuale;

		}
}