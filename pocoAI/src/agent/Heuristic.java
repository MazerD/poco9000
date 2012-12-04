package agent;

import simulation.Board;

public interface Heuristic {
	
	public int value(Board state);
	
	public String name();
}
