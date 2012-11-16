package agent;

import java.util.Collection;
import java.util.LinkedList;

import simulation.Action;
import simulation.Board;

/**
 * This works but wow it takes super long
 * @author Marjie
 *
 */
public class BreadthFirstAgent extends AbstractAgent {
	
	private Collection<Board> visited = new LinkedList<Board>();
	private LinkedList<Node> fringe = new LinkedList<Node>();
	
	@Override
	public Solution doFindSolution(Board startState) {
		visited.clear();
		fringe.clear();
		
		fringe.add(new Node(startState, null, null));
		while (!fringe.isEmpty()) {
			Node cur = fringe.removeFirst();
			if (cur.state.getBoxLocations().isEmpty()) {
				return buildSolution(cur);
			}
			visited.add(cur.state);
			
			for (Action a : Action.values()) {
				if (cur.state.canMoveAgent(a)) {
					Board newState = cur.state.clone();
					newState.moveAgent(a);
					if (!visited.contains(newState))
						fringe.addLast(new Node(newState, a, cur));
				}
			}
		}
		
		//No solution possible
		return new Solution();
	}

	@Override
	public String algorithmName() {
		return "Breadth First Graph Search";
	}
	
	private Solution buildSolution(Node goalState) {
		Solution s = new Solution();
		Node cur = goalState;
		while (cur.prev != null) {
			s.prependAction(cur.action);
			cur = cur.prev;
		}
		return s;
	}
	
	private class Node {
		public final Board state;
		public final Action action;
		public final Node prev;
		
		public Node(Board state, Action action, Node prev) {
			this.state = state;
			this.action = action;
			this.prev = prev;
		}
	}
}
