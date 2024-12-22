package battleship.ui;

import java.awt.Cursor;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UImapnel extends JPanel {
	private static final long serialVersionUID = 1L;
	static int X = 570;
	static int Y = 630;
	int numC = 10;
	int dimC = 48;
	int oroff = 1;
	int veroff = 1;
        @SuppressWarnings("unused")
	int c1Off = 0;
	int c2Off = 0;
	JButton[][] buttons;
	JLabel[] COr;
	JLabel[] CVer;
	protected JLabel label;
	UIJPanelBG sea;
	Cursor cursorHand = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	Cursor cursorDefault = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

	public UImapnel(String etichetta) {

		this.setSize(X, Y);
		this.setLayout(null);
		this.setOpaque(false);
		label = new JLabel();
		label.setIcon(new ImageIcon(getClass().getResource(("/res/images/" + etichetta + ".png"))));
		this.add(label);
		label.setBounds(50, 0, 550, 60);
		sea = new UIJPanelBG(
				Toolkit.getDefaultToolkit().createImage(FrameManageship.class.getResource("/res/images/sea.png")));
		sea.setBounds(34, 45, 550, 550);
		buttons = new JButton[numC][numC];
		ImageIcon gray = new ImageIcon(getClass().getResource("/res/images/grayButtonOpaque.png"));
		for (int i = 0; i < numC; i++) {
			for (int j = 0; j < numC; j++) {
				buttons[i][j] = new JButton(gray);
				buttons[i][j].setSize(dimC, dimC);
				sea.add(buttons[i][j]);
				buttons[i][j].setCursor(cursorHand);
				buttons[i][j].setBorder(null);
				buttons[i][j].setOpaque(false);
				buttons[i][j].setBorderPainted(false);
				buttons[i][j].setContentAreaFilled(false);
				buttons[i][j].setBounds(oroff, veroff, dimC, dimC);
				if (etichetta.equals("player")) {
					buttons[i][j].setCursor(cursorDefault);
					buttons[i][j].setDisabledIcon(gray);
					buttons[i][j].setEnabled(false);
				} else {
					buttons[i][j].setCursor(cursorHand);
				}
				oroff += dimC + 2;
			}
			veroff += dimC + 2;
			oroff = 1;
		}
		oroff = 40;
		veroff = 0;
		JPanel grid = new JPanel(null);
		grid.setOpaque(false);
		grid.add(sea);
		COr = new JLabel[10];
		CVer = new JLabel[10];


		for (int i = 0; i < 10; i++) {
			COr[i] = new JLabel();
			CVer[i] = new JLabel();
			grid.add(COr[i]);
			grid.add(CVer[i]);
			CVer[i].setIcon(new ImageIcon(getClass().getResource((("/res/images/coord/" + (i + 1) + ".png")))));
			CVer[i].setBounds(veroff, oroff, dimC, dimC);
			COr[i].setIcon(new ImageIcon(getClass().getResource((("/res/images/coord/" + (i + 11) + ".png")))));
			COr[i].setBounds(oroff, veroff, dimC, dimC);
			oroff += 50;
		}

		this.add(grid);
		grid.setBounds(0, 45, 550, 660);

	}

	void drawShip(int[] dati) {
		int x = dati[0];
		int y = dati[1];
		int dim = dati[2];
		int dir = dati[3];
		ImageIcon shipDim1orizz = new ImageIcon(
				getClass().getResource("/res/images/shipDim1orizz.png"));
		ImageIcon shipDim1vert = new ImageIcon(getClass().getResource("/res/images/shipDim1vert.png"));
		if (dim == 1) {
			buttons[x][y].setEnabled(false);
			if (dir == 0)
				buttons[x][y].setDisabledIcon(shipDim1orizz);
			else if (dir == 1)
				buttons[x][y].setDisabledIcon(shipDim1vert);
		} else {
			ImageIcon shipHeadLeft = new ImageIcon(
					getClass().getResource("/res/images/shipHeadLeft.png"));
			ImageIcon shipHeadTop = new ImageIcon(
					getClass().getResource("/res/images/shipHeadTop.png"));
			ImageIcon shipBodyLeft = new ImageIcon(
					getClass().getResource("/res/images/shipBodyLeft.png"));
			ImageIcon shipBodyTop = new ImageIcon(
					getClass().getResource("/res/images/shipBodyTop.png"));
			ImageIcon shipFootLeft = new ImageIcon(
					getClass().getResource("/res/images/shipFootLeft.png"));
			ImageIcon shipFootTop = new ImageIcon(
					getClass().getResource("/res/images/shipFootTop.png"));
			if (dir == 0) {
				buttons[x][y].setDisabledIcon(shipHeadLeft);
				buttons[x][y].setEnabled(false);
				for (int i = 1; i < dim - 1; i++) {
					buttons[x][y + i].setDisabledIcon(shipBodyLeft);
					buttons[x][y + i].setEnabled(false);
				}

				buttons[x][y + dim - 1].setDisabledIcon(shipFootLeft);
				buttons[x][y + dim - 1].setEnabled(false);
			} else { 
				
				buttons[x][y].setDisabledIcon(shipHeadTop);
				buttons[x][y].setEnabled(false);
			
				for (int i = 1; i < dim - 1; i++) {
					buttons[x + i][y].setDisabledIcon(shipBodyTop);
					buttons[x + i][y].setEnabled(false);
				}
			
				buttons[x + dim - 1][y].setDisabledIcon(shipFootTop);
				buttons[x + dim - 1][y].setEnabled(false);
			}
		}
	}

}
