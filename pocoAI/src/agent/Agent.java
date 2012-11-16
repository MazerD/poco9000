package agent;

import simulation.Board;

public interface Agent {

	public Solution findSolution(Board startState);
	
	public String algorithmName();
}
