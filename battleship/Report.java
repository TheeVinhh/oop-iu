package battleship;

public class Report {
	private Position p;
	private boolean colpita;
	private boolean sunk;
	
	public Report(){
	}
	
	public Report(Position p, boolean colpita, boolean sunk) {
		this.p = p;
		this.colpita = colpita;
		this.sunk = sunk;
	}
	public Position getP() {
		return p;
	}
	public void setP(Position p) {
		this.p = p;
	}
	public boolean isHit() {
		return colpita;
	}
	public void setColpita(boolean colpita) {
		this.colpita = colpita;
	}
	public boolean isSunk() {
		return sunk;
	}
	public void setsunk(boolean sunk) {
		this.sunk = sunk;
	}	
	public String toString(){
		return "coordinate:"+p+" hit:"+colpita+" sunk:"+sunk;
	}
}
