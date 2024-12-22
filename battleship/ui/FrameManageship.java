package battleship.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;

import battleship.Map;

public class FrameManageship extends JFrame implements ActionListener, KeyListener {
	private static final long serialVersionUID = 2923975805665801740L;
	private static final int NUM_NAVI = 10;
	LinkedList<int[]> playerShips;
								
	boolean finished = false;
	int shipsInserted = 0;
	int[] counterShip = { 1, 2, 3, 4 };
	Map map;
	UIManagePanel choosePan;
	UImapnel mapnel;

	public FrameManageship() {
		super("Warships");
		map = new Map();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(900, 672);
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.addKeyListener(this);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/images/icon.png")));
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		this.setLocation(x, y);
		UIJPanelBG container = new UIJPanelBG(
				Toolkit.getDefaultToolkit().createImage(getClass().getResource("/res/images/wood.jpg")));
		mapnel = new UImapnel("manage");
		container.add(mapnel);
		choosePan = new UIManagePanel();
		container.add(choosePan);
		mapnel.setBounds(25, 25, 600, 620);
		choosePan.setBounds(580, 25, 280, 800);

		this.add(container);
		for (int i = 0; i < mapnel.buttons.length; i++) {
			for (int j = 0; j < mapnel.buttons[i].length; j++) {
				mapnel.buttons[i][j].addActionListener(this);
				mapnel.buttons[i][j].setActionCommand("" + i + " " + j);
			}
		}
		choosePan.random.addActionListener(this);
		choosePan.reset.addActionListener(this);
		choosePan.play.addActionListener(this);
		playerShips = new LinkedList<int[]>();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		String text = source.getText();

		if (text.equals("reset")) {
			reset();
		}

		else if (text.equals("random")) {
			random();
		}

		else if (text.equals("play")) {
			play();

		} else {
			if (finished) {
				return;
			}
			StringTokenizer st = new StringTokenizer(source.getActionCommand(), " ");
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			int nave = -1;
			int dim = 0;
			int dir;
			for (int i = 0; i < choosePan.ship.length; i++) {
				if (choosePan.ship[i].isSelected())
					nave = i;
			}
			switch (nave) {
			case 0:
				dim = 4;
				break;
			case 1:
				dim = 3;
				break;
			case 2:
				dim = 2;
				break;
			case 3:
				dim = 1;
				break;
			}
			if (choosePan.direction[0].isSelected())
				dir = 0;
			else
				dir = 1;
			boolean inserted = map.inserisciNave(x, y, dim, dir);
			if (inserted) {
				shipsInserted++;

				counterShip[nave]--;
				choosePan.counterLabel[nave].setText("" + counterShip[nave]);

				if (choosePan.counterLabel[nave].getText().equals("0")) {
					choosePan.ship[nave].setEnabled(false);
					for (int i = 0; i < choosePan.ship.length; i++) {
						if (choosePan.ship[i].isEnabled() && !choosePan.ship[i].isSelected()) {
							choosePan.ship[i].setSelected(true);
							break;
						}
					}
				}

				if (shipsInserted == NUM_NAVI) {
					finished = true;
					choosePan.direction[0].setEnabled(false);
					choosePan.direction[1].setEnabled(false);
					choosePan.play.setEnabled(true);
				}
				int[] dati = { x, y, dim, dir };
				playerShips.add(dati);
				mapnel.drawShip(dati);
			}
		}
		this.requestFocusInWindow();
	}

	private void random() {
		if (shipsInserted == NUM_NAVI) {
			reset();
		}
		Random r = new Random();
		int[] dati = new int[4];
		for (int i = 0; i < counterShip.length; i++) {
			for (int j = 0; j < counterShip[i]; j++) {
				dati = map.insertNaveRandom(r, counterShip.length - i);
				playerShips.add(dati);
				mapnel.drawShip(dati);
			}
		}
		shipsInserted = NUM_NAVI;
		finished = true;
		choosePan.play.setEnabled(true);
		for (int i = 0; i < choosePan.ship.length; i++) {
			choosePan.ship[i].setEnabled(false);
		}
		choosePan.direction[0].setEnabled(false);
		choosePan.direction[1].setEnabled(false);
		for (int i = 0; i < counterShip.length; i++) {
			counterShip[i] = 0;
			choosePan.counterLabel[i].setText("0");
		}
		choosePan.ship[0].setSelected(true);

	}

	private void reset() {
		map = new Map();
		playerShips = new LinkedList<int[]>();
		for (int i = 0; i < Map.DIM_map; i++) {
			for (int j = 0; j < Map.DIM_map; j++) {
				mapnel.buttons[i][j].setEnabled(true);
			}
		}
		finished = false;
		choosePan.play.setEnabled(false);
		for (int i = 0; i < choosePan.ship.length; i++) {
			choosePan.ship[i].setEnabled(true);
		}
		choosePan.direction[0].setEnabled(true);
		choosePan.direction[1].setEnabled(true);
		for (int i = 0; i < counterShip.length; i++) {
			counterShip[i] = i + 1;
			choosePan.counterLabel[i].setText("" + (i + 1));
		}
		choosePan.ship[0].setSelected(true);
		shipsInserted = 0;
	}

	private void play() {
		FrameBattle battle = new FrameBattle(playerShips, map);
		battle.frame.setVisible(true);
		this.setVisible(false);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		char s = Character.toLowerCase(arg0.getKeyChar());
		int key = arg0.getKeyCode();
		if (s == 'g') {

			random();
			play();
		} else {
			if (s == 'r') {
				random();
			} else {
				if (key == KeyEvent.VK_DELETE || key == KeyEvent.VK_BACK_SPACE) {
					reset();
				} else {
					if (key == KeyEvent.VK_ESCAPE) {
						System.exit(0);
					}
				}
				if (key == KeyEvent.VK_ENTER) {
					if (finished) {
						play();
					}
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

}
