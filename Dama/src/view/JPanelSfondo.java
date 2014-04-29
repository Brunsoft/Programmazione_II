package view;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
 
class JPanelSfondo extends JPanel {
    private Image img;																// immagine utilizzata per lo sfondo
 
    public JPanelSfondo (String immagine) {
        this(new ImageIcon(immagine).getImage());
    }
 
    public JPanelSfondo (Image img) {
        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));	// le dimensioni del pannello corrispondono a quelle dell'immagine
        setSize(size);
        setLayout(null);
    }
    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
}