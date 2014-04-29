package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.SortedSet;
import java.util.TreeSet;

import controller.*;
import model.*;

public class PannelloDamiera extends JPanel {

	protected static boolean turno = false;										// indica il turno
	private static Casella[][] bottoni;											// contiene la damiera grafica
	protected static Damiera damiera;
	private Casella[] mossaUtente;												// mossa utente
	private boolean multipla;													// indica la presenza di mangiata multipla dall'utente
	private MosseValide mosse;													// lista mosse valide
	private MangiateValide mangiate;											// lista mangiate valide
	private Esecutore esecutore;
	private PC pc;
	private Thread t;															// thread contenente l'intelligienza artificiale, utilizzato per aggiornare la grafica settando delle pause intermedie

	public PannelloDamiera() {

		bottoni = new Casella[8][8];											// griglia 8x8 della damiera grafica
		damiera = new Damiera();
		mossaUtente = new Casella[2];											// contiene inizio e mangiata
		multipla = false;
		esecutore = new Esecutore();
		pc = new PC();
		t = new Thread(pc);

		setLayout(new GridLayout(8,8));
		costruisciDamiera();
		t.start();

	}

	private void costruisciDamiera() {											// aggiunge caselle alla griglia e il corrispondente ascoltatore
		for( int i = 0; i <8; i++)
			for(int j = 0; j <8; j++){
				bottoni[i][j] = new Casella(i, j, damiera);
				add(bottoni[i][j]);
				addListener(bottoni[i][j], i, j);
			}
	}

	public static void azzeraDamiera() {										// azzera la damiera all'inizio di una nuova partita

		damiera.setPedineIniziali();
		damiera.resetNumeroPedine();
		aggiorna(0);
		FinestraDama.azzeraPedine();
	}

	public static void aggiorna(int termina) { 																					// aggiorna la grafica

		for(int i=0; i < 8; i++)
			for(int j=0; j < 8; j++)
				bottoni[i][j].aggiorna(damiera, i, j);

		FinestraDama.aggiungiPedine(12-damiera.getNumeroPedine()[0], 12-damiera.getNumeroPedine()[1]);							// passa i nuovi valori delle pedine mangiate
		controlla(termina);																										// controlla se la partita può continuare
	}
	
	public static void controlla(int termina) {																					// partita può continuare se il pc e l'Utente hanno ancora mosse disponibili
		if((damiera.getNumeroPedine()[0] == 0 || damiera.getNumeroPedine()[1] == 0 || termina == 1 || termina == -1 || termina == -2)){
			Component frame = null;
			Object[] opzioni = {"Si, Nuova partita", "No, Esci"};																// opzioni messaggio di uscita
			int scelta;
			if(damiera.getNumeroPedine()[0] == 0 || termina == -1)																// l'utente ha vinto
				scelta = JOptionPane.showOptionDialog(frame, "HAI VINTO! " + "Vuoi cominciare una nuova partita?", "DAMA",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, opzioni, opzioni[1]);
			else if(damiera.getNumeroPedine()[1] == 0 || termina == 1)															// il computer ha vinto
				scelta = JOptionPane.showOptionDialog(frame, "HAI PERSO! " + "Vuoi cominciare una nuova partita?", "DAMA",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, opzioni, opzioni[1]);
			else																												// partita terminata in pari
				scelta = JOptionPane.showOptionDialog(frame, "PAREGGIO! " + "Vuoi cominciare una nuova partita?", "DAMA",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, opzioni, opzioni[1]);
			if(scelta==0)
				azzeraDamiera();				// azzera damiera e ricomincia partita
			else
				System.exit(0); 				// esci dal gioco
		}
	}
	
	private void illuminaMosseValide (Cella cella, Damiera damiera, boolean comando) {						// illumina la cella cliccata e le corrispondenti celle finali
		mangiate = new MangiateValide();																	// trova mangiate valide
		if (mangiate.trovaMangiateBianche(damiera).isEmpty()) {												// se ci sono movimenti
			MosseValide mosse = new MosseValide(damiera, cella.getX(), cella.getY(), true);
			SortedSet<Mossa> listaMovimenti = new TreeSet<Mossa>();
			listaMovimenti = mosse.getMosseValide();
			for (Mossa m:listaMovimenti)
				if (cella.equals(m.getInizio()))
					if (comando) {
						bottoni[m.getInizio().getX()] [m.getInizio().getY()].setPressed(true);
						bottoni[m.getInizio().getX()] [m.getInizio().getY()].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 1));		// cella cliccata GIALLA
						bottoni[m.getFine().getX()] [m.getFine().getY()].setBorder(BorderFactory.createLineBorder(Color.RED, 1));				// celle destinazione ROSSE

					}
					else {
						bottoni[m.getInizio().getX()] [m.getInizio().getY()].setBorder(null);				// toglie i bordi alla fine della mossa o in caso di deselezione
						bottoni[m.getFine().getX()] [m.getFine().getY()].setBorder(null);
					}
		}
		else {																								// se ci sono mangiate
			TreeSet<Mossa> listaMangiate = mangiate.trovaMangiateBianche(damiera);
			boolean b = false;
			for (Mossa m:listaMangiate){
				if (m.getPercorso() != null && m.getPercorso().length > 2){									// se esiste un percorso ed è > 2
					if (cella.equals(m.getPercorso()[0]))
						if (comando) {
							bottoni[m.getPercorso()[0].getX()] [m.getPercorso()[0].getY()].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 1));	// cella cliccata GIALLA
							bottoni[m.getPercorso()[2].getX()] [m.getPercorso()[2].getY()].setBorder(BorderFactory.createLineBorder(Color.RED, 1));		// celle destinazione ROSSE
							b = true;
						}
						else {
							bottoni[m.getPercorso()[0].getX()] [m.getPercorso()[0].getY()].setBorder(null);	// toglie i bordi alla fine della mossa o in caso di deselezione
							bottoni[m.getPercorso()[2].getX()] [m.getPercorso()[2].getY()].setBorder(null);
						}
				}else
					if (cella.equals(m.getInizio()))
						if (comando) {
							bottoni[m.getInizio().getX()] [m.getInizio().getY()].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 1));			// cella cliccata GIALLA
							bottoni[m.getFine().getX()] [m.getFine().getY()].setBorder(BorderFactory.createLineBorder(Color.RED, 1));					// celle destinazione ROSSE
							b = true;
						}
						else {
							bottoni[m.getInizio().getX()] [m.getInizio().getY()].setBorder(null);
							bottoni[m.getFine().getX()] [m.getFine().getY()].setBorder(null);
						}
			}
			if (!b && !multipla)													// se ci sono mangiate (son obbligato a fare la mangiata migliore)
				for (Mossa m:listaMangiate)
					bottoni[m.getPercorso()[0].getX()] [m.getPercorso()[0].getY()].setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));			// celle iniziale obbligata BLU
		}
	}

	private boolean cellaValida(Cella cella){ 										// contolla se la cella cliccata è una mossa valida
		mangiate = new MangiateValide();
		if (mangiate.trovaMangiateBianche(damiera).isEmpty()) {						// cerca tra i movimenti
			MosseValide mosse = new MosseValide(damiera, mossaUtente[0].getx(), mossaUtente[0].gety(), true);
			SortedSet<Mossa> listaMovimenti = new TreeSet<Mossa>();
			listaMovimenti = mosse.getMosseValide();
			for (Mossa m:listaMovimenti){
				if (cella.equals(m.getFine()))
					return true;
			}
		}
		else {																		// cerca tra le mangiate
			TreeSet<Mossa> listaMangiate = mangiate.trovaMangiateBianche(damiera);
			for (Mossa m:listaMangiate)
				if(m.getPercorso() != null && m.getPercorso().length > 2){			// se la mangiata è multipla
					if (cella.equals(m.getPercorso()[2]))
						return true;
				}else
					if (cella.equals(m.getFine()))
						return true;
		}
		return false;
	}

	private void addListener(Casella casella, int i, int j){								// ascoltatore della casella
		final int x = i;
		final int y = j;
		bottoni[x][y].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!turno) {															// entra solo se il turno è tornato all'utente

					if (!damiera.getCella(x, y).isEmpty() && damiera.getCella(x, y).getColorePedina() && !bottoni[x][y].isPressed()) { 					// se la pedina è bianca e il bottone non è premuto

						if (mossaUtente[0] == null) { 										// primo bottone premuto
							mossaUtente[0] = bottoni [x][y];
							mossaUtente[1] = null;
							illuminaMosseValide(damiera.getCella(x, y), damiera, true);		// illumino le mosse valide
						}

						else if(!multipla){ 												// secondo bottone premuto
							bottoni[x][y].setPressed(true);
							mossaUtente[1] = bottoni [x][y];

							try {
								controllaCella();											// se è corretta eseguo la mossa scelta
							} catch (InterruptedException e1) {}
						}
					}

					else if (!damiera.getCella(x, y).isEmpty() && damiera.getCella(x, y).getColorePedina() && bottoni[x][y].isPressed() && !multipla) { // disabilita selezione precendente
						mossaUtente[0] = null;
						mossaUtente[1] = null;
						illuminaMosseValide(damiera.getCella(x, y), damiera, false);		// toglie i bordi alle celle precedentemente selezionate
						bottoni[x][y].setPressed(false);
					}

					else if (!damiera.getCella(x, y).isEmpty() && !damiera.getCella(x, y).getColorePedina()){}  // se premo una pedina non eseguo operazioni
					else if (damiera.getCella(x, y).isEmpty() && damiera.getCella(x, y).getColore() || damiera.getCella(x, y).isEmpty() && !damiera.getCella(x, y).getColore() && mossaUtente[0] == null){}
					else{																	// secondo bottone premuto è una cella vuota
						bottoni[x][y].setPressed(true);
						mossaUtente[1] = bottoni [x][y];
						try {
							controllaCella();												// se è corretta eseguo la mossa scelta
						} catch (InterruptedException e1) {}
					}
				}
			}

			private void controllaCella() throws InterruptedException {													// controlla cella
				if (cellaValida(damiera.getCella(mossaUtente[1].getx(), mossaUtente[1].gety())))						// se ho cliccao una cella corretta eseguo la mossa
					eseguiMossa();

				else if (damiera.getCella(mossaUtente[1].getx(), mossaUtente[1].gety()).getPedina() == null){			// se clicco una cella vuota non illuminata
					mossaUtente[1].setPressed(false);
					mossaUtente[1]=null;	
				}																										// se ho cliccato una cella contenente una pedina bianca
				else if (damiera.getCella(mossaUtente[1].getx(), mossaUtente[1].gety()).getColorePedina() == damiera.getCella(mossaUtente[0].getx(), mossaUtente[0].gety()).getColorePedina() ) {
					illuminaMosseValide(damiera.getCella(mossaUtente[0].getx(), mossaUtente[0].gety()), damiera, false);
					bottoni[ mossaUtente[0].getx() ][ mossaUtente[0].gety() ].setPressed(false);				
					mossaUtente[0] = mossaUtente[1];
					mossaUtente[1] = null;
					illuminaMosseValide(damiera.getCella(mossaUtente[0].getx(), mossaUtente[0].gety()), damiera, true);	// illumino le nuove mosse valide
				}															
			}

			private void eseguiMossa() {						// esegue la mossa
				mangiate = new MangiateValide();
				TreeSet<Mossa> listaMangiate = mangiate.trovaMangiateBianche(damiera);
				int mangia = 0;
				for(Mossa m:listaMangiate)													//controlla se ci sono mangiate multiple
					if (m.getPercorso() != null && m.getPercorso().length > 2)
						if (m.getPercorso()[2].getX()==mossaUtente[1].getx() && m.getPercorso()[2].getY()==mossaUtente[1].gety())
							mangia = m.getPedineMangiabili();


				Mossa temp = null;
				mosse = new MosseValide(damiera, mossaUtente[0].getx(), mossaUtente[0].gety());

				for(Mossa m:mosse)
					if (m.getPercorso() != null && m.getPercorso().length > 2){				// se la mangiata è multipla
						if (m.getPercorso()[2].getX()==mossaUtente[1].getx() && m.getPercorso()[2].getY()==mossaUtente[1].gety())
							temp = m;
					}else
						if (m.getFine().getX()==mossaUtente[1].getx() && m.getFine().getY()==mossaUtente[1].gety())
							temp = m;

				esecutore.esegui(damiera, temp);				// passa la damiera e la mossa all'esecutore che la esegue

				mossaUtente[0].setPressed(false);
				mossaUtente[1].setPressed(false);

				aggiorna(0);									// aggiorna la grafica							

				if(mangia > 1){									// se ci son ancora mosse
					mossaUtente[0] = mossaUtente[1];			// la nuova mangiata iniziale è la precedente finale
					mossaUtente[1] = null;						// la nuova mossa finale è da definire
					mossaUtente[0].setPressed(true);			// non devo poter spegere questo bottone
					multipla = true;							// ci son mosse multiple

					illuminaMosseValide(damiera.getCella(mossaUtente[0].getx(), mossaUtente[0].gety()), damiera, true);			// illumino le mosse valide rispetto all'attuale selezionata
				}
				else { 											// passo il turno al pc
					mossaUtente[0] = null;						// azzero mosse utente
					mossaUtente[1] = null;
					turno = true;								// turno del pc
					multipla = false;							// mangiata multipla utente finita
				}
			}
		});
	}
}
