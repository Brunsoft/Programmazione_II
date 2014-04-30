package view;

import controller.IntelligenzaArtificiale;

public class PC implements Runnable {										// esegue la mossa del PC
	
	private IntelligenzaArtificiale ia;
	
	public PC() {
		ia = new IntelligenzaArtificiale(PannelloDamiera.damiera);
	}
	
	public void run() {
		while (true) {														// ciclo infinito che termina all'uscita del thread padre
			
			while (!PannelloDamiera.turno)									// esce quando è il turno del PC
				aspetta(500);
			
			FinestraDama.illumina(false);									// illumina il pallino del PC
			int a;
			
			while(!ia.stop){												// finchè ci son eventuali mangiate doppie
				aspetta(500);												// mette in pausa la thread per 500 msec
				a = ia.giocaPC();											// richiama gioca PC
				PannelloDamiera.aggiorna(a);								// aggiorna la damiera
			}
			aspetta(100);													// mette in pausa la thread per 100 msec
			ia.stop = false;
			//PannelloDamiera.turno = false;								// passo il turno
			FinestraDama.illumina(true);									// illumina il pallino dell'Utente
		}
	}
	
	private void aspetta(int msec){											// mette in pausa la thread per n msec
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {}
	}
}
