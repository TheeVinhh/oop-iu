package battleship;

import java.util.LinkedList;
import java.util.Random;

public class Computer {
	private LinkedList<Position> listHits;
	private Random r;
	private int hit;
	private LinkedList<String> possibilitize;
	private Position lastShot;
	private String direction;
	private Map plMap;
	private Position primohit;


	public Computer(Map mapAdversary) {
		listHits = new LinkedList<Position>();
		this.plMap = mapAdversary;
		for (int i = 0; i < Map.DIM_map; i++) {
			for (int j = 0; j < Map.DIM_map; j++) {
				Position p = new Position(i, j);
				listHits.add(p);
			}
		}
		r = new Random();
		hit = 0;
	}

	public Report myTurn() {

		Report rep = new Report();
		if (hit == 0) {
			boolean hit = shootRandom();
			rep.setP(lastShot);
			rep.setColpita(hit);
			Direction sunk;
			if (hit) {
				hit++;
				sunk = plMap.sunk(lastShot);
				if (sunk != null) {
					rep.setsunk(true);
					removeContours(sunk);
					hit = 0;
					direction = null;
				} else {
					primohit = lastShot;
					possibilitize = new LinkedList<String>();
					inizializzaLista();
				}
			}
			return rep;
		} 
		if (hit == 1) {
			boolean hit = sparaMirato1();
			Direction sunk;
			rep.setP(lastShot);
			rep.setColpita(hit);
			rep.setsunk(false);
			if (hit) {
				hit++;
				possibilitize = null;
				sunk = plMap.sunk(lastShot);
				if (sunk != null) {
					rep.setsunk(true);
					removeContours(sunk);
					hit = 0;
					direction = null;
				}
			}
			return rep;
		}
		if (hit >= 2) {
			boolean hit = sparaMirato2();
			Direction sunk;
			rep.setP(lastShot);
			rep.setColpita(hit);
			rep.setsunk(false);
			if (hit) {
				hit++;
				sunk = plMap.sunk(lastShot);
				if (sunk != null) {
					rep.setsunk(true);
					removeContours(sunk);
					hit = 0;
					direction = null;
				}
			} else {
				invertidirection();
			}
			return rep;
		}
		return null;
	}

	private boolean shootRandom() {
		int tiro = r.nextInt(listHits.size());
		Position p = listHits.remove(tiro);
		lastShot = p;
		boolean hit = plMap.hit(p);
		return hit;
	}

	private boolean sparaMirato1() {
		boolean errore = true;
		Position p = null;
		do {
			int tiro = r.nextInt(possibilitize.size());
			String dove = possibilitize.remove(tiro);
			p = new Position(primohit);
			p.sposta(dove.charAt(0));
			direction = dove;
			if (!plMap.acqua(p)) {
				listHits.remove(p);
				errore = false;
			}
		} while (errore);
							
		lastShot = p;
		return plMap.hit(p);
	}

	private boolean sparaMirato2() {
		boolean colpibile = false;
		Position p = new Position(lastShot);
		do {
			p.sposta(direction.charAt(0));

			if (p.fuorimap() || plMap.acqua(p)) {
				invertidirection();
			} else {
				if (!plMap.hit(p)) {
					colpibile = true;
				}

			}
		} while (!colpibile);
		listHits.remove(p);
		lastShot = p;
		return plMap.hit(p);
	}

	//

	private void removeContours(Direction sunk) {
		int Xin = sunk.getXin();
		int Xfin = sunk.getXfin();
		int Yin = sunk.getYin();
		int Yfin = sunk.getYfin();
		if (Xin == Xfin) {
			if (Yin != 0) {
				Position p = new Position(Xin, Yin - 1);
				if (!plMap.acqua(p)) {
					listHits.remove(p);
					plMap.setAcqua(p);

				}
			}
			if (Yfin != Map.DIM_map - 1) {
				Position p = new Position(Xin, Yfin + 1);
				if (!plMap.acqua(p)) {
					listHits.remove(p);
					plMap.setAcqua(p);
				}
			}
			if (Xin != 0) {
				for (int i = 0; i <= Yfin - Yin; i++) {
					Position p = new Position(Xin - 1, Yin + i);
					if (!plMap.acqua(p)) {
						listHits.remove(p);
						plMap.setAcqua(p);
					}
				}

			}
			if (Xin != Map.DIM_map - 1) {
				for (int i = 0; i <= Yfin - Yin; i++) {
					Position p = new Position(Xin + 1, Yin + i);
					if (!plMap.acqua(p)) {
						listHits.remove(p);
						plMap.setAcqua(p);
					}
				}
			}
		} else {
			if (Xin != 0) {
				Position p = new Position(Xin - 1, Yin);
				if (!plMap.acqua(p)) {
					listHits.remove(p);
					plMap.setAcqua(p);
				}
			}
			if (Xfin != Map.DIM_map - 1) {
				Position p = new Position(Xfin + 1, Yin);
				if (!plMap.acqua(p)) {
					listHits.remove(p);
					plMap.setAcqua(p);
				}
			}
			if (Yin != 0) {
				for (int i = 0; i <= Xfin - Xin; i++) {
					Position p = new Position(Xin + i, Yin - 1);
					if (!plMap.acqua(p)) {
						listHits.remove(p);
						plMap.setAcqua(p);
					}
				}

			}
			if (Yfin != Map.DIM_map - 1) {
				for (int i = 0; i <= Xfin - Xin; i++) {
					Position p = new Position(Xin + i, Yin + 1);
					if (!plMap.acqua(p)) {
						listHits.remove(p);
						plMap.setAcqua(p);
					}
				}
			}
		}
	}

	private void inizializzaLista() {
		if (lastShot.getCoordX() != 0) {
			possibilitize.add("N");
		}
		if (lastShot.getCoordX() != Map.DIM_map - 1) {
			possibilitize.add("S");
		}
		if (lastShot.getCoordY() != 0) {
			possibilitize.add("O");
		}
		if (lastShot.getCoordY() != Map.DIM_map - 1) {
			possibilitize.add("E");
		}
	}

	private void invertidirection() {
		if (direction.equals("N")) {
			direction = "S";
		} else if (direction.equals("S")) {
			direction = "N";
		} else if (direction.equals("E")) {
			direction = "O";
		} else if (direction.equals("O")) {
			direction = "E";
		}
	}

}
