package view;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import model.Damiera;
import model.Dama;

public class Casella extends JButton {
	
	private int x,y;																// coordinate
	boolean pressed;
	ImageIcon scura, chiara;														// sfondi caselle
	ImageIcon nera, bianca, damaNera, damaBianca;									// icone caselle (pedine)
	Damiera damiera;
	JLabel pedina = new JLabel();

	public Casella(int x, int y, Damiera damiera){
		
		pressed = false;
		scura = new ImageIcon("images/scura.png");
		chiara = new ImageIcon("images/chiara.png");
		nera = new ImageIcon("images/Pedine/nera.png");
		bianca = new ImageIcon("images/Pedine/bianca.png");
		damaNera = new ImageIcon("images/Pedine/dama_nera.png");
		damaBianca = new ImageIcon("images/Pedine/dama_bianca.png");
		this.x = x;
		this.y = y;
		this.damiera = damiera;
		

		if(!(damiera.getCella(x, y).getColore()))									// imposta sfondo casella
			setIcon(scura);															// casella Nera
		else
			setIcon(chiara);														// casella Bianca
		
		if(!damiera.getCella(x,y).isEmpty())										// imposta Pedina
			if(!(damiera.getCella(x,y).getColorePedina()))							// se Nera
				if(!(damiera.getCella(x,y).getPedina() instanceof Dama))			// se non è Dama
					pedina.setIcon(nera);											// imposta Pedina Nera
				else
					pedina.setIcon(damaNera);										// imposta Dama Nera
			else																	// se Bianca
				if(!(damiera.getCella(x,y).getPedina() instanceof Dama))			// se non è Dama
					pedina.setIcon(bianca);											// imposta Pedina Bianca
				else
					pedina.setIcon(damaBianca);										// imposta Dama Bianca
		add(pedina);
		setBorder(null);
	}
	
	public void aggiorna(Damiera damiera, int x, int y){							// aggiorna la damiera grafica confrontandola con la damiera testuale
		pedina.setIcon(null);
		
		if(!(damiera.getCella(x, y).getColore()))									// se la cella è nera
			if(!damiera.getCella(x,y).isEmpty()){
				if(!(damiera.getCella(x,y).getColorePedina()))						// se la pedina è nera
					if(!(damiera.getCella(x,y).getPedina() instanceof Dama))		// se non è Dama
						pedina.setIcon(nera);
					else
						pedina.setIcon(damaNera);
				else																// se la pedina è bianca
					if(!(damiera.getCella(x,y).getPedina() instanceof Dama))		// se non è Dama
						pedina.setIcon(bianca);
					else
						pedina.setIcon(damaBianca);
				}
				
		setBorder(null);
	}
	
	public void rimuoviIcone(int x, int y){
		setIcon(null);
	}
	
	public void setGriglia(int x, int y, boolean scelta){
		this.x = x;
		this.y = y;
		if (scelta)
			pedina.setIcon(bianca);
		else
			pedina.setIcon(nera);
	}

	public void setPressed(boolean pressed){
		this.pressed = pressed;
	}
	
	public int getx(){
		return x;
	}
	
	public int gety(){
		return y;
	}
	
	public boolean isPressed(){
		return pressed;
	}
}