package battleship.ui;

import battleship.Computer;
import battleship.Direction;
import battleship.Map;
import battleship.Position;
import battleship.Report;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;	
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.StringTokenizer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class FrameBattle implements ActionListener, KeyListener {
	UImapnel playerPanel = new UImapnel("player");
	UImapnel cpuPanel = new UImapnel("cpu");
	JFrame frame = new JFrame("Warships");
	JPanel comandPanel = new JPanel();
	Cursor cursorDefault = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	UIJPanelBG panel = new UIJPanelBG(
			Toolkit.getDefaultToolkit().createImage(getClass().getResource("/res/images/battleImg.jpg")));
	Report rep;
	Computer cpu;
	Map cpuMap;
	Map playerMap;
	int numNaviPlayer = 10;
	int numNaviCPU = 10;
	StringBuilder sb = new StringBuilder();
	boolean b = true;
	UIStatPanel statPlayer;
	UIStatPanel statCPU;
	JPanel targetPanel = new JPanel(null);
	UIJPanelBG target = new UIJPanelBG(
			Toolkit.getDefaultToolkit().createImage(getClass().getResource("/res/images/target.png")));
	ImageIcon wreck = new ImageIcon(getClass().getResource("/res/images/wreck.gif"));
	Cursor cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	Timer timer;
	boolean turnoDelCPU;

	public FrameBattle(LinkedList<int[]> playerShips, Map map) {
		playerMap = map;
		cpu = new Computer(map);
		cpuMap = new Map();
		cpuMap.riempimapRandom();
		frame.setSize(1080, 700);
		frame.setTitle("Warships");
		frame.setFocusable(true);
		frame.requestFocusInWindow();
		frame.addKeyListener(this);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/images/icon.png")));
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		statPlayer = new UIStatPanel();
		statCPU = new UIStatPanel();
		statPlayer.setBounds(30, 595, 500, 80);
		statCPU.setBounds(570, 595, 500, 80);
		frame.add(statPlayer);
		frame.add(statCPU);
		targetPanel.setBounds(0, 0, 500, 500);
		targetPanel.setOpaque(false);
		playerPanel.sea.add(targetPanel);

		panel.add(playerPanel);
		playerPanel.setBounds(0, 0, UImapnel.X, UImapnel.Y);
		playerPanel.setOpaque(false);
		panel.add(cpuPanel);
		cpuPanel.setBounds(540, 0, UImapnel.X, UImapnel.Y);
		panel.add(comandPanel);
		frame.add(panel);
		frame.setResizable(false);
		timer = new Timer(2000, new GestoreTimer());
		turnoDelCPU = false;

		for (int i = 0; i < cpuPanel.buttons.length; i++) {
			for (int j = 0; j < cpuPanel.buttons[i].length; j++) {
				cpuPanel.buttons[i][j].addActionListener(this);
				cpuPanel.buttons[i][j].setActionCommand("" + i + " " + j);
			}
		}
		for (int[] v : playerShips) {
			playerPanel.drawShip(v);
		}

	}

	void setBox(Report rep, boolean player) {
		int x = rep.getP().getCoordX();
		int y = rep.getP().getCoordY();
		ImageIcon fire = new ImageIcon(getClass().getResource("/res/images/fireButton.gif"));
		ImageIcon water = new ImageIcon(getClass().getResource("/res/images/grayButton.gif"));
		String cosa;
		if (rep.isHit())
			cosa = "X";
		else
			cosa = "A";
		UImapnel mapnel;
		if (!player) {
			mapnel = playerPanel;
		} else {
			mapnel = cpuPanel;
		}
		if (cosa == "X") {
			mapnel.buttons[x][y].setIcon(fire);
			mapnel.buttons[x][y].setEnabled(false);
			mapnel.buttons[x][y].setDisabledIcon(fire);
			mapnel.buttons[x][y].setCursor(cursorDefault);
		} else {
			mapnel.buttons[x][y].setIcon(water);
			mapnel.buttons[x][y].setEnabled(false);
			mapnel.buttons[x][y].setDisabledIcon(water);
			mapnel.buttons[x][y].setCursor(cursorDefault);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (turnoDelCPU)
			return;
		JButton source = (JButton) e.getSource();
		StringTokenizer st = new StringTokenizer(source.getActionCommand(), " ");
		int x = Integer.parseInt(st.nextToken());
		int y = Integer.parseInt(st.nextToken());
		Position newP = new Position(x, y);
		boolean hit = cpuMap.hit(newP);
		Report rep = new Report(newP, hit, false);
		this.setBox(rep, true);
		if (hit) { 
			Direction shipSunk = cpuMap.sunk(newP);
			if (shipSunk != null) {
				numNaviCPU--;
				setSunk(shipSunk);
				if (numNaviCPU == 0) {
					Object[] options = { "New game", "Exit" };
					int n = JOptionPane.showOptionDialog(frame, (new JLabel("You win!", JLabel.CENTER)),
							"Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
							options[1]);
					if (n == 0) {
						FrameManageship restart = new FrameManageship();
						restart.setVisible(true);
						this.frame.setVisible(false);
					} else {
						System.exit(0);
					}
				}
			}
		} else { 
			if (b) {
				timer.start();
				turnoDelCPU = true;
			}
		}
		frame.requestFocusInWindow();
	}

	private void setSunk(Position p) {
		LinkedList<String> possibilitize = new LinkedList<String>();
		if (p.getCoordX() != 0) {
			possibilitize.add("N");
		}
		if (p.getCoordX() != Map.DIM_map - 1) {
			possibilitize.add("S");
		}
		if (p.getCoordY() != 0) {
			possibilitize.add("O");
		}
		if (p.getCoordY() != Map.DIM_map - 1) {
			possibilitize.add("E");
		}
		String direction;
		boolean found = false;
		Position posCurrent;
		do {
			posCurrent = new Position(p);
			if (possibilitize.isEmpty()) {
				deleteShip(1, statPlayer);
				playerPanel.buttons[posCurrent.getCoordX()][posCurrent.getCoordY()].setIcon(wreck);
				playerPanel.buttons[posCurrent.getCoordX()][posCurrent.getCoordY()].setEnabled(false);
				playerPanel.buttons[posCurrent.getCoordX()][posCurrent.getCoordY()].setDisabledIcon(wreck);
				playerPanel.buttons[posCurrent.getCoordX()][posCurrent.getCoordY()].setCursor(cursorDefault);
				return;
			}
			direction = possibilitize.removeFirst();
			posCurrent.sposta(direction.charAt(0));
			if (playerMap.hit(posCurrent)) {
				found = true;
			}
		} while (!found);
		int dim = 0;
		posCurrent = new Position(p);
		do {

			playerPanel.buttons[posCurrent.getCoordX()][posCurrent.getCoordY()].setIcon(wreck);
			playerPanel.buttons[posCurrent.getCoordX()][posCurrent.getCoordY()].setEnabled(false);
			playerPanel.buttons[posCurrent.getCoordX()][posCurrent.getCoordY()].setDisabledIcon(wreck);
			playerPanel.buttons[posCurrent.getCoordX()][posCurrent.getCoordY()].setCursor(cursorDefault);
			posCurrent.sposta(direction.charAt(0));

			dim++;
		} while (posCurrent.getCoordX() >= 0 && posCurrent.getCoordX() <= 9 && posCurrent.getCoordY() >= 0
				&& posCurrent.getCoordY() <= 9 && !playerMap.acqua(posCurrent));

		deleteShip(dim, statPlayer);
	}

	private void setSunk(Direction shipSunk) {
		int dim = 0;
		for (int i = shipSunk.getXin(); i <= shipSunk.getXfin(); i++) {
			for (int j = shipSunk.getYin(); j <= shipSunk.getYfin(); j++) {
				cpuPanel.buttons[i][j].setIcon(wreck);
				cpuPanel.buttons[i][j].setEnabled(false);
				cpuPanel.buttons[i][j].setDisabledIcon(wreck);
				cpuPanel.buttons[i][j].setCursor(cursorDefault);
				dim++;
			}
		}
		deleteShip(dim, statCPU);
	}

	private void deleteShip(int dim, UIStatPanel panel) {
		switch (dim) {
		case 4:
			panel.ships[0].setEnabled(false);
			break;
		case 3:
			if (!panel.ships[1].isEnabled())
				panel.ships[2].setEnabled(false);
			else
				panel.ships[1].setEnabled(false);
			break;
		case 2:
			if (!panel.ships[3].isEnabled())
				if (!panel.ships[4].isEnabled())
					panel.ships[5].setEnabled(false);
				else
					panel.ships[4].setEnabled(false);
			else
				panel.ships[3].setEnabled(false);
			break;
		case 1:
			if (!panel.ships[6].isEnabled())
				if (!panel.ships[7].isEnabled())
					if (!panel.ships[8].isEnabled())
						panel.ships[9].setEnabled(false);
					else
						panel.ships[8].setEnabled(false);
				else
					panel.ships[7].setEnabled(false);
			else
				panel.ships[6].setEnabled(false);
			break;
		default:
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		int key = arg0.getKeyCode();
		if (key == KeyEvent.VK_ESCAPE) {
			FrameManageship manage = new FrameManageship();
			manage.setVisible(true);
			frame.setVisible(false);
		}

		sb.append(arg0.getKeyChar());
		if (sb.length() == 4) {
			int z = sb.toString().hashCode();
			if (z == 3194657) {
				sb = new StringBuilder();
				b = !b;
			} else {
				String s = sb.substring(1, 4);
				sb = new StringBuilder(s);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	public class GestoreTimer implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			timer.stop();
			boolean flag;

			Report report = cpu.myTurn();
			drawTarget(report.getP().getCoordX() * 50, report.getP().getCoordY() * 50);
			flag = report.isHit();
			setBox(report, false);
			if (report.isSunk()) {
				numNaviPlayer--;
				setSunk(report.getP());
				if (numNaviPlayer == 0) {
					Object[] options = { "New game", "Exit" };
					int n = JOptionPane.showOptionDialog(frame, (new JLabel("You are defeated!", JLabel.CENTER)),
							"Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
							options[1]);
					if (n == 0) {
						FrameManageship restart = new FrameManageship();
						restart.setVisible(true);
						frame.setVisible(false);
					} else {
						System.exit(0);
					}
				}
			}

			turnoDelCPU = false;
			if (flag) {
				timer.start();
				turnoDelCPU = true;
			}
			frame.requestFocusInWindow();
		}

	}

	public void drawTarget(int i, int j) {
		target.setBounds(j, i, 50, 50);
		target.setVisible(true);
		targetPanel.add(target);
		targetPanel.repaint();
	}
}
