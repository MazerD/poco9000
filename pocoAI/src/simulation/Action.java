package simulation;

public enum Action {
	LEFT(-1, 0), RIGHT(1, 0), UP(0, -1), DOWN(0, 1);
	
	private int x;
	private int y;
	
	private Action(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getDX() {
		return x;
	}
	
	public int getDY() {
		return y;
	}
}

