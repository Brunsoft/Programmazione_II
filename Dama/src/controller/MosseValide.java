package controller;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import model.*;

public class MosseValide implements Iterable<Mossa> {
	
	Damiera damiera;
	int x, y;
	private SortedSet<Mossa> listaMosseValide = new TreeSet<Mossa>();				// lista mosse valide
	boolean turno;
	
	public MosseValide (Damiera damiera, int x, int y, boolean turno) {
		this.x = x;
		this.y = y;
		this.damiera = damiera;
		this.turno = turno;
		mossaValida();
	}
	
	public MosseValide (Damiera damiera, int x, int y) {
		this.x = x;
		this.y = y;
		this.damiera = damiera;
		this.turno = false;
		mossaValida();
	}
	
	public boolean isEmpty() {
		return listaMosseValide.isEmpty();
	}
	
	public void mossaValida() {														// aggiunge le mosse in modo ordinato (in base al tipo e al numero di pedine mangiate)
		if (damiera.getCella(x,y).getPedina() instanceof Dama)						// se è una dama
			muoviDama(turno);														// calcola le mosse possibili
		else
			if (damiera.getCella(x,y).getColorePedina())							// se è una pedina bianca
				muoviBianca(turno);													// calcola le mosse possibli
			else																	// se è una pedina nera
				muoviNera(turno);													// calcola le mosse possibili
	}

	public SortedSet<Mossa> getMosseValide() {										// ritorna l'insieme delle mosse valide
		return listaMosseValide;
	}
	
	private void muoviDama (boolean turno) {										// cerca le mosse in entrambi i sensi
		muoviNera(turno);
		muoviBianca(turno);
	}

	private void muoviBianca (boolean turno) {										// cerca possibili mosse bianche (movimenti e mangiate)
																					// try catch usati per evitare i controlli sulle celle (superamento estremi damiera)
		boolean occupata = false;													// cella vicina è occupata da una pedina
		try{
			if(damiera.getCella(x-1,y+1).isEmpty())																// se cella diagonale in alto a destra è vuota
				listaMosseValide.add(new Mossa(damiera.getCella(x, y),damiera.getCella(x-1, y+1),null,0));		// aggiungo la mossa alla lista (con percorso vuoto e pedine mangiate 0)
			else 																								// possibile mangiata
				occupata=true;
		}
		catch(Exception e){}
		
		try{
			if(damiera.getCella(x-1,y-1).isEmpty())																// se cella diagonale in alto a sinistra è vuota
				listaMosseValide.add(new Mossa(damiera.getCella(x, y),damiera.getCella(x-1, y-1),null,0));		// aggiungo la mossa alla lista (con percorso vuoto e pedine mangiate 0)
			else 																								// possibile mangiata
				occupata=true;
		}
		catch(Exception e){}
		
		if(occupata)																// controllo possibili mangiate
			muoviBiancaSeOccupata(x, y, 0, null, turno);
	}

	private void muoviNera(boolean turn){											// cerca possibili mosse bianche (movimenti e mangiate)
																					// try catch usati per evitare i controlli sulle celle (superamento estremi damiera)
		boolean occupata = false;													// cella vicina è occupata da una pedina
		try{
			if(damiera.getCella(x+1,y+1).isEmpty())																// se cella diagonale in basso a destra è vuota
				listaMosseValide.add(new Mossa(damiera.getCella(x, y),damiera.getCella(x+1, y+1),null,0));		// aggiungo la mossa alla lista (con percorso vuoto e pedine mangiate 0)
			else 																								// possibile mangiata
				occupata=true;
		}
		catch(Exception e){}
		
		try{
			if(damiera.getCella(x+1,y-1).isEmpty())																// se cella diagonale in basso a sinistra è vuota
				listaMosseValide.add(new Mossa(damiera.getCella(x, y),damiera.getCella(x+1, y-1),null,0));		// aggiungo la mossa alla lista (con percorso vuoto e pedine mangiate 0)
			else 																								// possibile mangiata
				occupata=true;
		}
		catch(Exception e){}
		
		if(occupata)																// controllo possibili mangiate
			muoviNeraSeOccupata(x, y, 0, null, turno);
	}
	
	public void muoviBiancaSeOccupata(int x, int y, int mangiata, Cella[] percorso, boolean turno){				// calcola in modo ricorsivo tutte le possibili mangiate bianche
		int prossimaMangiata;
		Cella[] nuovoPercorso;
		try{
			if(percorso==null){																					// se è un movimento	
				nuovoPercorso=new Cella[2];																		// crea un nuovo percorso
				nuovoPercorso[0]=damiera.getCella(x,y);															// inizio percorso uguale a cella corrente
			}
			else{																								// se è una mangiata
				nuovoPercorso=new Cella[percorso.length+2];														// aggiungo 2 celle al percorso
				copiaArray(percorso, nuovoPercorso);															// copio percorso vecchio
				nuovoPercorso[nuovoPercorso.length-2]=damiera.getCella(x,y);									// cella corrente salvata in coda al percorso
			}
			if(!damiera.getCella(x-1,y+1).isEmpty())																								// se cella diagonale in alto a destra è vuota
				if(!(damiera.getCella(x-1,y+1).getColorePedina()==nuovoPercorso[0].getColorePedina() )){											// se la pedina indicata è di colore opposto alla mia pedina iniziale
					if(damiera.getCella(x-2,y+2).isEmpty()){																						// se cella diagonale successiva è vuota
						nuovoPercorso[nuovoPercorso.length-1]=damiera.getCella(x-1,y+1);															// aggiungo la pedina mangiata al percorso
						prossimaMangiata = damiera.getCella(x-1,y+1).getPedina() instanceof Dama ? mangiata+10 : mangiata + 1;						// calcolo il punteggio in base alla pedina mangiata
						listaMosseValide.add(new Mossa(damiera.getCella(x, y),damiera.getCella(x-2, y+2), nuovoPercorso, prossimaMangiata ));		// aggiungo la mossa alla lista delle mosse valide
						
						if(turno){																				// se è turno pc si richiama per calcolo ricorsivo	
							if(damiera.getCella(x,y).getPedina() instanceof Dama)								// se la pedina corrente è una dama
								muoviNeraSeOccupata(x-2, y+2, prossimaMangiata, nuovoPercorso, turno);			// controlla mangiate nel senso opposto
							muoviBiancaSeOccupata(x-2, y+2, prossimaMangiata,nuovoPercorso, turno);				// controlla mangiate nello stesso senso
						}
					}
				}
		}
		catch(Exception e){}
		
		try{
			if(percorso==null){																					// se è un movimento	
				nuovoPercorso=new Cella[2];																		// crea un nuovo percorso
				nuovoPercorso[0]=damiera.getCella(x,y);															// inizio percorso uguale a cella corrente
			}
			else{																								// se è una mangiata
				nuovoPercorso=new Cella[percorso.length+2];														// aggiungo 2 celle al percorso
				copiaArray(percorso, nuovoPercorso);															// copio percorso vecchio
				nuovoPercorso[nuovoPercorso.length-2]=damiera.getCella(x,y);									// cella corrente salvata in coda al percorso
			}
			if(!damiera.getCella(x-1,y-1).isEmpty())																								// se cella diagonale in alto a sinistra è vuota
				if(!(damiera.getCella(x-1,y-1).getColorePedina()==nuovoPercorso[0].getColorePedina() )){											// se la pedina indicata è di colore opposto alla mia pedina iniziale
					if(damiera.getCella(x-2,y-2).isEmpty()){																						// se cella diagonale successiva è vuota
						nuovoPercorso[nuovoPercorso.length-1]=damiera.getCella(x-1,y-1);															// aggiungo la pedina mangiata al percorso
						prossimaMangiata = damiera.getCella(x-1,y-1).getPedina() instanceof Dama ? mangiata+10 : mangiata + 1;						// calcolo il punteggio in base alla pedina mangiata
						listaMosseValide.add(new Mossa(damiera.getCella(x, y),damiera.getCella(x-2, y-2), nuovoPercorso, prossimaMangiata ));		// aggiungo la mossa alla lista delle mosse valide
						
						if(turno){																				// se è turno pc si richiama per calcolo ricorsivo	
							if(damiera.getCella(x,y).getPedina() instanceof Dama)								// se la pedina corrente è una dama
								muoviNeraSeOccupata(x-2, y-2, prossimaMangiata, nuovoPercorso, turno);			// controlla mangiate nel senso opposto
							muoviBiancaSeOccupata(x-2, y-2, prossimaMangiata,nuovoPercorso, turno);				// controlla mangiate nello stesso senso
						}
					}
				}
		}
		catch(Exception e){}
	}

	// Metodo ricorsivo che calcola tutte le mosse possibili in caso di mangiata se la pedina o dama che muove nera
	public void muoviNeraSeOccupata(int x, int y, int mangiata, Cella[] percorso, boolean turno){
		int prossimaMangiata;
		Cella[] nuovoPercorso;
		try{
			if(percorso==null){																					// se è un movimento	
				nuovoPercorso=new Cella[2];																		// crea un nuovo percorso
				nuovoPercorso[0]=damiera.getCella(x,y);															// inizio percorso uguale a cella corrente
			}
			else{																								// se è una mangiata
				nuovoPercorso=new Cella[percorso.length+2];														// aggiungo 2 celle al percorso
				copiaArray(percorso, nuovoPercorso);															// copio percorso vecchio
				nuovoPercorso[nuovoPercorso.length-2]=damiera.getCella(x,y);									// cella corrente salvata in coda al percorso
			}
			if(!damiera.getCella(x+1,y+1).isEmpty())																								// se cella diagonale in basso a destra è vuota
				if(!(damiera.getCella(x+1,y+1).getColorePedina()==nuovoPercorso[0].getColorePedina() )){											// se la pedina indicata è di colore opposto alla mia pedina iniziale
					if(damiera.getCella(x+2,y+2).isEmpty()){																						// se cella diagonale successiva è vuota
						nuovoPercorso[nuovoPercorso.length-1]=damiera.getCella(x+1,y+1);															// aggiungo la pedina mangiata al percorso
						prossimaMangiata = damiera.getCella(x+1,y+1).getPedina() instanceof Dama ? mangiata+10 : mangiata + 1;						// calcolo il punteggio in base alla pedina mangiata
						listaMosseValide.add(new Mossa(damiera.getCella(x, y),damiera.getCella(x+2, y+2), nuovoPercorso, prossimaMangiata ));		// aggiungo la mossa alla lista delle mosse valide
						
						if(turno){																				// se è turno pc si richiama per calcolo ricorsivo	
							if(damiera.getCella(x,y).getPedina() instanceof Dama)								// se la pedina corrente è una dama
								muoviBiancaSeOccupata(x+2, y+2, prossimaMangiata, nuovoPercorso, turno);			// controlla mangiate nel senso opposto
							muoviNeraSeOccupata(x+2, y+2, prossimaMangiata,nuovoPercorso, turno);				// controlla mangiate nello stesso senso
						}
					}
				}
		}
		catch(Exception e){}
		
		try{
			if(percorso==null){																					// se è un movimento	
				nuovoPercorso=new Cella[2];																		// crea un nuovo percorso
				nuovoPercorso[0]=damiera.getCella(x,y);															// inizio percorso uguale a cella corrente
			}
			else{																								// se è una mangiata
				nuovoPercorso=new Cella[percorso.length+2];														// aggiungo 2 celle al percorso
				copiaArray(percorso, nuovoPercorso);															// copio percorso vecchio
				nuovoPercorso[nuovoPercorso.length-2]=damiera.getCella(x,y);									// cella corrente salvata in coda al percorso
			}
			if(!damiera.getCella(x+1,y-1).isEmpty())																								// se cella diagonale in basso a sinistra è vuota
				if(!(damiera.getCella(x+1,y-1).getColorePedina()==nuovoPercorso[0].getColorePedina() )){											// se la pedina indicata è di colore opposto alla mia pedina iniziale
					if(damiera.getCella(x+2,y-2).isEmpty()){																						// se cella diagonale successiva è vuota
						nuovoPercorso[nuovoPercorso.length-1]=damiera.getCella(x+1,y-1);															// aggiungo la pedina mangiata al percorso
						prossimaMangiata = damiera.getCella(x+1,y-1).getPedina() instanceof Dama ? mangiata+10 : mangiata + 1;						// calcolo il punteggio in base alla pedina mangiata
						listaMosseValide.add(new Mossa(damiera.getCella(x, y),damiera.getCella(x+2, y-2), nuovoPercorso, prossimaMangiata ));		// aggiungo la mossa alla lista delle mosse valide
						
						if(turno){																				// se è turno pc si richiama per calcolo ricorsivo	
							if(damiera.getCella(x,y).getPedina() instanceof Dama)								// se la pedina corrente è una dama
								muoviBiancaSeOccupata(x+2, y-2, prossimaMangiata, nuovoPercorso, turno);			// controlla mangiate nel senso opposto
							muoviNeraSeOccupata(x+2, y-2, prossimaMangiata,nuovoPercorso, turno);				// controlla mangiate nello stesso senso
						}
					}
				}
		}
		catch(Exception e){}
	}
	
	public Mossa migliorMossa(){									// ritorna la miglior mossa tra quelle nella lista
		return listaMosseValide.last();
	}
	
	private void copiaArray(Cella[] originale, Cella[] copia) {		// copia array
		for(int i = 0; i < originale.length ; i++ ){
			copia[i] = new Cella(originale[i]); 
		}
	}
	
	public Iterator<Mossa> iterator() {
		return listaMosseValide.iterator();
	}
}
