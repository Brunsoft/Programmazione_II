package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class FinestraDama extends JFrame {

	private static PannelloDamiera pannello;										// pannello contenente la damiera
	JPanelSfondo sfondo = new JPanelSfondo("images/sfondo.png");					// pannello grafico per settare l'immagine di background e contenitore generale
	GridBagConstraints c = new GridBagConstraints();								// componente per modifiche grafiche temporanee sui pannelli

	JMenuBar menuBar;
	JMenu menu, submenu;
	JMenuItem menuItem1, menuItem3;
	static JMenuItem menuItem2;
	
	static ImageIcon PallinoVerde, PallinoRosso;									// icone indicanti il turno
	static JLabel TurnoPC, TurnoUtente;
	JLabel PC, Utente, footer;														// scritte giocatori e footer
	static JLabel[][] PedinePC, PedineUtente;										// griglia di Label (pedine mangiate durante il gioco)
	JPanel spazio, GrigliaUtente, GrigliaPC;

	public FinestraDama() {

		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);							// setta operazione di chiusura finestra
		setSize (new Dimension(890,650));											// dimensione minima finestra
		setMinimumSize (new Dimension(890,650));
		setTitle("La Dama");
		setLocationRelativeTo(null);												// centra la finestra al centro dallo schermo
		pannello = new PannelloDamiera();
		Finestra();
		Utente.setText(JOptionPane.showInputDialog("Inserisci nome utente"));		// finestra di inserimento nome giocatore
		if(Utente.getText().compareTo("") == 0)
			Utente.setText("Utente");
		setVisible(true);
		Image icon = Toolkit.getDefaultToolkit().getImage("images/icon.png");
		setIconImage(icon);
	}

	public void Finestra(){															// setta componenti grafici

		menuBar = new JMenuBar();													// nuova barra dei menu
		menu = new JMenu("Partita");												// aggiunge menu "Partita"
		menuBar.add(menu);

		menuItem1 = new JMenuItem("Nuova partita");									// aggiunge sottomenu "Nuova Partita"
		menuItem2 = new JMenuItem("Pari");											// aggiunge sottomenu "Pari" abilitato dopo almeno una mossa
		menuItem2.setEnabled(false);
		menuItem3 = new JMenuItem("Esci");											// aggiunge sottomenu "Esci"
		menu.add(menuItem1);
		menu.add(menuItem2);
		menu.addSeparator();
		menu.add(menuItem3);
		setJMenuBar(menuBar);

		PC = new JLabel("Computer", JLabel.CENTER);									// nome del Computer
		Utente = new JLabel("User", JLabel.CENTER);									// nome dell'Utente inserito
		spazio = new JPanel();														// usato solo per spaziare i pannelli
		footer = new JLabel("Programmazione II  |  'LA DAMA'  |  Luca Vicentini & Matteo Dal Monte", JLabel.CENTER);
		GrigliaPC = new JPanel(new GridLayout(4,3));								// Griglia delle pedine mangiate dal PC
		GrigliaUtente = new JPanel(new GridLayout(4,3));							// Griglia delle pedine mangiate dall'Utente
		PedinePC = new JLabel[4][3];
		PedineUtente = new JLabel[4][3];
		TurnoPC = new JLabel();														// Pallino indicante il turno del PC
		TurnoUtente = new JLabel();													// Pallino indicante il turno dell?Utente
		PallinoRosso = new ImageIcon("images/rosso.png");
		PallinoVerde = new ImageIcon("images/verde.png");
		TurnoPC.setIcon(PallinoRosso);
		TurnoUtente.setIcon(PallinoVerde);
		
		menuItem1.addActionListener(new ActionListener() {  						// Ascoltatore della voce di menu "Nuova Partita"
			public void actionPerformed(ActionEvent event) {
				pannello.azzeraDamiera();											// azzera la Damiera e le pedine Mangiate
			}  
		});

		menuItem2.addActionListener(new ActionListener() {  						// Ascoltatore della voce di menu "Pari"
			public void actionPerformed(ActionEvent event) {
				pannello.controlla(-2);												// richiama controlla passandogli -2
			}  
		});

		menuItem3.addActionListener(new ActionListener() {  						// Ascoltatore della voce di menu "Esci"
			public void actionPerformed(ActionEvent event) {
				System.exit(0); 													// termina il programma
			}  
		});

		aggiungiGrafica();
	}

	private void creaGrigliaMangiate(){												// riempie la griglia di pedine graficamente vuote.
		for (int i=0; i<4; i++)
			for (int j=0; j<3; j++){
				PedinePC[i][j]=new JLabel();
				PedinePC[i][j].setIcon(new ImageIcon("images/Pedine/vuota.png"));
				PedineUtente[i][j]=new JLabel();
				PedineUtente[i][j].setIcon(new ImageIcon("images/Pedine/vuota.png"));
				GrigliaPC.add(PedinePC[i][j]);
				GrigliaUtente.add(PedineUtente[i][j]);
			}
		GrigliaPC.setOpaque(false);
		GrigliaUtente.setOpaque(false);
	}

	public static void azzeraPedine(){												// azzera la griglia dopo la fina di una partita (Nuova Partita, Pari)
		menuItem2.setEnabled(false);
		for (int i=0; i<4; i++)
			for (int j=0; j<3; j++){
				PedinePC[i][j].setIcon(new ImageIcon("images/Pedine/vuota.png"));
				PedineUtente[i][j].setIcon(new ImageIcon("images/Pedine/vuota.png"));
			}
	}

	public static void aggiungiPedine(int n, int b) {								// aggiunge le pedine appena mangiate alla griglia
		menuItem2.setEnabled(true);
		for (int i=0, x=0; i<4 && x<b; i++)											// aggiunge pedine Bianche
			for (int j=0; j<3 && x<b; j++, x++)
				PedinePC[i][j].setIcon(new ImageIcon("images/Pedine/bianca_icon.png"));


		for (int i=3, x=0; i>=0 && x<n; i--)										// aggiunge pedine Nere
			for (int j=0; j<3 && x<n; j++, x++)
				PedineUtente[i][j].setIcon(new ImageIcon("images/Pedine/nera_icon.png"));
	}

	public static  void illumina(boolean turno) {									// riceve il turno e illumina il Pallino corrispondente
		if (!turno){																// turno del PC
			TurnoPC.setIcon(PallinoVerde);
			TurnoUtente.setIcon(PallinoRosso);
		}else{																		// turno dell'Utente
			TurnoPC.setIcon(PallinoRosso);
			TurnoUtente.setIcon(PallinoVerde);
		}
	}
	
	private void aggiungiGrafica(){													// aggiunge i pannelli nella posizione desiderata grazie al GridBagLayout

		sfondo.setLayout(new GridBagLayout());
		creaGrigliaMangiate();

		c.anchor = GridBagConstraints.NORTH;										// imposta componente in Alto, al Centro, occupa 1 pos in altezza e 1 in larghezza
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;																// in posizione griglia 1,1
		c.gridy = 1;
		c.ipady = 10;
		PC.setFont(new Font("sansserif",Font.ROMAN_BASELINE,18));
		PC.setForeground(Color.WHITE);
		sfondo.add(PC,c);															// aggiungi al pannello con gli attributi del componente grafico c.
		
		c.anchor = GridBagConstraints.CENTER;										// imposta componente in Centro, occupa 1 pos in altezza e 1 in larghezza
		c.gridy = 2;																// in posizione griglia 1,2
		c.ipady = 0;
		sfondo.add(GrigliaPC, c);													// aggiungi al pannello con gli attributi del componente grafico c.

		c.gridx = 2;																// in posizione griglia 2,1
		c.gridy = 1;
		TurnoPC.setOpaque(false);
		sfondo.add(TurnoPC, c);														// aggiungi al pannello con gli attributi del componente grafico c.
		
		
		c.gridheight = 5;															// occupa 5 pos in altezza
		c.gridx = 3;																// in posizione griglia 3,1
		c.gridy = 1;
		pannello.setBorder(BorderFactory.createLineBorder(new Color(0,0,0), 13));
		sfondo.add(pannello, c);													// aggiungi al pannello con gli attributi del componente grafico c.

		c.anchor = GridBagConstraints.NORTH;										// imposta componente in Alto, al Centro, occupa 1 pos in altezza e 1 in larghezza
		c.gridheight = 1;
		c.gridx = 5;																// in posizione griglia 5,3
		c.gridy = 3;
		c.ipady = 120;																// sposta in basso di 100 px
		spazio.setOpaque(false);
		sfondo.add(spazio, c);														// aggiungi al pannello con gli attributi del componente grafico c.
		
		c.ipady = 0;																// resetta spaziatura in verticale
		c.gridx = 5;																// in posizione griglia 5,4
		c.gridy = 4;
		sfondo.add(GrigliaUtente, c);												// aggiungi al pannello con gli attributi del componente grafico c.

		c.gridx = 4;																// in posizione griglia 4,5
		c.gridy = 5;
		c.ipady = 10;
		TurnoUtente.setOpaque(false);
		sfondo.add(TurnoUtente, c);													// aggiungi al pannello con gli attributi del componente grafico c.
		
		c.gridx = 5;																// in posizione griglia 5,5
		c.gridy = 5;
		Utente.setFont(new Font("sansserif",Font.ROMAN_BASELINE,18));
		Utente.setForeground(Color.WHITE);
		sfondo.add(Utente,c);														// aggiungi al pannello con gli attributi del componente grafico c.

		c.anchor = GridBagConstraints.CENTER;										// imposta componente in Alto, al Centro, occupa 1 pos in altezza e 5 in larghezza
		c.gridwidth = 5;
		c.gridx = 1;																// in posizione griglia 1,6
		c.gridy = 6;
		footer.setFont(new Font("sansserif",Font.ITALIC,12));
		footer.setForeground(Color.WHITE);
		sfondo.add(footer, c);														// aggiungi al pannello con gli attributi del componente grafico c.
		
		add(sfondo); 																// aggiungi pannello sfondo al JFrame
	}

}
