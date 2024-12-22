package battleship;

import java.util.LinkedList;
import java.util.Random;

public class Map {
	public static final int DIM_map = 10;
	private final char NULLA = '0', NAVE = 'X', ACQUA = 'A', hit = 'C';
	private char[][] map;
	private LinkedList<Direction> listaNavi;

	public Map() {
		listaNavi = new LinkedList<Direction>();
		map = new char[DIM_map][DIM_map];
		for (int i = 0; i < DIM_map; i++)
			for (int j = 0; j < DIM_map; j++)
				map[i][j] = NULLA;
	}

	public void riempimapRandom() {
		clear();
		Random r = new Random();
		insertNaveRandom(r, 4);
		insertNaveRandom(r, 3);
		insertNaveRandom(r, 3);
		insertNaveRandom(r, 2);
		insertNaveRandom(r, 2);
		insertNaveRandom(r, 2);
		insertNaveRandom(r, 1);
		insertNaveRandom(r, 1);
		insertNaveRandom(r, 1);
		insertNaveRandom(r, 1);
	}

	private void clear() {
		for (int i = 0; i < DIM_map; i++)
			for (int j = 0; j < DIM_map; j++)
				map[i][j] = NULLA;

	}

	public boolean inserisciNave(int x, int y, int dim, int dir) {
		if (dir == 1 && x + dim > DIM_map) {
			return false;
		} 
		if (dir == 0 && y + dim > DIM_map) {
			return false;
		} 
		boolean inserted;

		if (dir == 0)
			inserted = verificaOrizzontale(x, y, dim);
		else
			inserted = verificaVerticale(x, y, dim);

		if (!inserted)
			return false;
		if (dir == 0) {
			Direction n = new Direction(x, y, x, y + dim - 1);
			listaNavi.add(n);
		} else {
			Direction n = new Direction(x, y, x + dim - 1, y);
			listaNavi.add(n);
		}
		for (int i = 0; i < dim; i++) {
			if (dir == 0) {
				map[x][y + i] = NAVE;
			} else
				map[x + i][y] = NAVE;
		}
		return true;
	}

	public int[] insertNaveRandom(Random random, int dimensione) {
		boolean inserted;
		int[] dati = new int[4];
		int direction, riga, colonna;
		do {
			inserted = true;
			direction = random.nextInt(2);
			if (direction == 0) {
				colonna = random.nextInt(DIM_map - dimensione + 1);
				riga = random.nextInt(DIM_map);
			} else {
				colonna = random.nextInt(DIM_map);
				riga = random.nextInt(DIM_map - dimensione + 1);
			}
			if (direction == 0)
				inserted = verificaOrizzontale(riga, colonna, dimensione);
			else
				inserted = verificaVerticale(riga, colonna, dimensione);
		} while (!inserted);
		if (direction == 0) {
			Direction n = new Direction(riga, colonna, riga, colonna + dimensione - 1);
			listaNavi.add(n);
		} else {
			Direction n = new Direction(riga, colonna, riga + dimensione - 1, colonna);
			listaNavi.add(n);
		}
		for (int i = 0; i < dimensione; i++) {
			if (direction == 0) {
				map[riga][colonna + i] = NAVE;
			} else
				map[riga + i][colonna] = NAVE;
		}
		dati[0] = riga;
		dati[1] = colonna;
		dati[2] = dimensione;
		dati[3] = direction;
		return dati;
	}

	public boolean verificaVerticale(int riga, int colonna, int dimensione) {
		if (riga != 0)
			if (map[riga - 1][colonna] == NAVE)
				return false;
		if (riga != DIM_map - dimensione)
			if (map[riga + dimensione][colonna] == NAVE)
				return false;
		for (int i = 0; i < dimensione; i++) {
			if (colonna != 0)
				if (map[riga + i][colonna - 1] == NAVE)
					return false;
			if (colonna != DIM_map - 1)
				if (map[riga + i][colonna + 1] == NAVE)
					return false;
			if (map[riga + i][colonna] == NAVE)
				return false;
		}
		return true;
	}

	public boolean verificaOrizzontale(int riga, int colonna, int dimensione) {
		if (colonna != 0)
			if (map[riga][colonna - 1] == NAVE)
				return false;
		if (colonna != DIM_map - dimensione)
			if (map[riga][colonna + dimensione] == NAVE)
				return false;
		for (int i = 0; i < dimensione; i++) {
			if (riga != 0)
				if (map[riga - 1][colonna + i] == NAVE)
					return false;
			if (riga != DIM_map - 1)
				if (map[riga + 1][colonna + i] == NAVE)
					return false;
			if (map[riga][colonna + i] == NAVE)
				return false;
		}
		return true;
	}

	public boolean hit(Position p) {
		int riga = p.getCoordX();
		int colonna = p.getCoordY();
		if (map[riga][colonna] == NAVE) {
			map[riga][colonna] = hit;
			return true;
		}
		map[riga][colonna] = ACQUA;
		return false;
	}

	public Direction sunk(Position p) {
		int riga = p.getCoordX();
		int col = p.getCoordY();
		Direction nave = null;
		for (int i = 0; i < listaNavi.size(); i++) {
			if (listaNavi.get(i).uguale(riga, col)) {
				nave = listaNavi.get(i);
				break;
			}
		}
		for (int i = nave.getXin(); i <= nave.getXfin(); i++) {
			for (int j = nave.getYin(); j <= nave.getYfin(); j++) {
				if (map[i][j] != hit) {
					return null;
				}
			}
		}
		listaNavi.remove(nave);
		return nave;
	}

	public void setAcqua(Position p) {
		map[p.getCoordX()][p.getCoordY()] = ACQUA;
	}

	public boolean acqua(Position p) {
		return map[p.getCoordX()][p.getCoordY()] == ACQUA;
	}

	public boolean hit(Position p) {
		return map[p.getCoordX()][p.getCoordY()] == hit;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < DIM_map; i++) {
			for (int j = 0; j < DIM_map; j++) {
				sb.append(map[i][j] + " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public void setAdvShips(LinkedList<int[]> advShips) {
		listaNavi.clear();
		for (int[] a : advShips) {
			inserisciNave(a[0], a[1], a[2], a[3]);
			System.out.println("sto inserendo" + a[0] + a[1] + a[2] + a[3]);
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++)
				System.out.print(map[i][j]);
			System.out.println("");
		}
	}
}
