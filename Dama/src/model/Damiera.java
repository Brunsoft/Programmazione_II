package model;

public class Damiera {

	Cella damiera [][] = new Cella [8][8];																// Damiera (matrice di celle)
	int numeroPedine [] = {12,12};																		// numero pedine iniziali del PC e dell'Utente

	public Damiera () {
		for (int i=0; i<8; i++)																			// scorre tutta la damiera
			for (int j=0; j<8; j++)
				if ((i+j)%2 == 0)
					damiera[i][j] = new Cella (i, j, false);											// imposta cella nera								
				else
					damiera[i][j] = new Cella (i, j, true);												// imposta cella bianca
		setPedineIniziali(); 																			// inserisce pedine iniziale 
	}

	public Damiera (Damiera other) {																	// crea copia da una damiera
		this.numeroPedine[0] = other.numeroPedine[0];
		this.numeroPedine[1] = other.numeroPedine[1];

		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++)
				this.damiera[i][j] = new Cella (other.damiera[i][j]);
	}

	public Cella getCella (int x, int y) throws IllegalArgumentException {								// ritorna cella se valida
		if (x<0 || x>7 || y<0 || y>7)
			throw new IllegalArgumentException();
		return damiera[x][y];
	}

	public int[] getNumeroPedine() {																	// ritorna il numero delle pedine restanti
		return numeroPedine;
	}

	public void resetNumeroPedine(){																	// reset numero pedine
		numeroPedine[0]=12;
		numeroPedine[1]=12;
	}

	public void muovi (Cella inizio, Cella fine) {														// richiama muoviSuDamiera controllando se le celle sono valide
		muoviSuDamiera(getCella(inizio.getX(), inizio.getY()) , getCella(fine.getX(), fine.getY()));		
	}

	private void muoviSuDamiera (Cella inizio, Cella fine) {
		fine.addPedina (inizio.getPedina());															// aggiungi pedina alla cella finale
		inizio.rimuoviPedina();																			// rimuove pedina alla cella iniziale
		trasformazione(fine);																			// controlla se può diventare una dama
	}

	public void mangia (Cella inizio, Cella mangiata, Cella fine) {										// richiama mangiaSuDamiera controllando se le celle sono valide
		mangiaSuDamiera(getCella(inizio.getX(), inizio.getY()), getCella(mangiata.getX(), mangiata.getY()), getCella(fine.getX(), fine.getY()));
	}

	private void mangiaSuDamiera (Cella inizio, Cella mangiata, Cella fine) {
		muoviSuDamiera(inizio, fine);																	// muove la pedina dalla cella iniziale alla cella finale
		if (mangiata.getColorePedina())
			numeroPedine[1]--;																			// rimuove la pedina dalla lista delle pedine Bianche
		else
			numeroPedine[0]--;																			// rimuove la pedina dalla lista delle pedine Nere
		mangiata.rimuoviPedina();																		// rimuove pedina mangiata
		trasformazione(fine);																			// controlla se può diventare una dama
	}
	
	private void trasformazione (Cella cella) {															// passa a Dama se la pedina si trova in fondo alla damiera
		if (!(cella.getPedina() instanceof Dama) && (cella.getX() == 0 || cella.getX() == 7))
			cella.addPedina (new Dama(cella.getColorePedina()));
	}
	
	public void copiaDamiera (Damiera other) {															// copia damiera da una damiera 
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++){
				if (this.damiera[i][j].getPedina() == null)
					other.getCella(i,j).addPedina (null);
				else if (this.damiera[i][j].getColorePedina() == true)
					other.getCella(i,j).addPedina (new Pedina(true));
				else if (this.damiera[i][j].getColorePedina() == false)
					other.getCella(i,j).addPedina (new Pedina(false));
			}
	}

	public void setPedineIniziali() {																	// imposta le pedine all'inizio della partita
		for (int i=0; i<3; i++)																			// imposta pedine nere
			for (int j=0; j<8; j++)
				if (!damiera[i][j].getColore())
					damiera[i][j].addPedina(new Pedina(false));

		for (int i=3; i<5; i++)																			// imposta celle vuote al centro della damiera
			for (int j=0; j<8; j++)
				if (!damiera[i][j].getColore())
					damiera[i][j].addPedina(null);

		for (int i=5; i<8; i++)																			// imposta pedine bianche
			for (int j=0; j<8; j++)			
				if (!damiera[i][j].getColore())
					damiera[i][j].addPedina(new Pedina(true));
	}

	private void setPedineDebug() {																		// set pedine debug

//		damiera[4][4].addPedina(new Pedina(true));
//		damiera[2][4].addPedina(new Pedina(true));
//		damiera[2][4].addPedina(new Dama(false));
//		damiera[2][2].addPedina(new Pedina(true));
//		damiera[7][1].addPedina(new Pedina(true));
//		damiera[6][4].addPedina(new Pedina(true));
//		damiera[2][6].addPedina(new Pedina(true));
		damiera[6][2].addPedina(new Pedina(false));
		damiera[7][3].addPedina(new Dama(true));
		damiera[4][2].addPedina(new Pedina(false));
	}
	
}
