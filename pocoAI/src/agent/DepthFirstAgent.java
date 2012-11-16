package agent;

import java.util.Collection;
import java.util.LinkedList;

import simulation.Action;
import simulation.Board;

public class DepthFirstAgent implements Agent {

	private Solution s;
	private Collection<Board> visited = new LinkedList<Board>();
	
	@Override
	public Solution findSolution(Board startState) {
		s = new Solution();
		visited.clear();
		solve(startState);		
		return s;
	}

	@Override
	public String algorithmName() {
		return "Breadth First Graph Search";
	}
	
	private boolean solve(Board state) {
		if (state.getBoxLocations().size() == 0) {
			return true;
		}
		
		visited.add(state);
		
		for (Action a : Action.values()) {
			if (state.canMoveAgent(a)) {
				Board moved = state.clone();
				moved.moveAgent(a);
				
				if (!visited.contains(moved) && solve(moved)) {
					s.prependAction(a);
					return true;
				}
			}
		}
		
		return false;
	}

}
