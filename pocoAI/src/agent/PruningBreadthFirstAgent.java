package agent;

import java.util.Collection;
import java.util.LinkedList;

import simulation.Action;
import simulation.Board;
import simulation.Location;
import agent.BreadthFirstAgent.Node;

public class PruningBreadthFirstAgent extends AbstractAgent {

	private Collection<Board> visited = new LinkedList<Board>();
	private LinkedList<Node> fringe = new LinkedList<Node>();
	
	@Override
	public Solution doFindSolution(Board startState) {
		visited.clear();
		fringe.clear();
		
		fringe.add(new Node(startState, null, null));
		while (!fringe.isEmpty()) {
			Node cur = fringe.removeFirst();
			visited.add(cur.state);
			
			for (Action a : Action.values()) {
				if (cur.state.canMoveAgent(a)) {
					Board newState = cur.state.clone();
					newState.moveAgent(a);
					
					if (newState.getBoxLocations().isEmpty())
						return BreadthFirstAgent.buildSolution(new Node(newState, a, cur));
					
					if (!visited.contains(newState) && isSolvable(newState, a))
						fringe.addLast(new Node(newState, a, cur));
				}
			}
		}
		
		//No solution possible
		return new Solution();
	}
	
	@Override
	public String algorithmName() {
		return "Breadth First Graph Search (Pruning)";
	}
	
	/**
	 * Returns whether this state is still solvable, given that the previous state was solvable
	 * @param state
	 * @param justTaken
	 * @return
	 */
	private boolean isSolvable(Board state, Action justTaken) {
		Location agent = state.getAgentLocation();
		Location box = new Location(agent.getX() + justTaken.getDX(),
				agent.getY() + justTaken.getDY());
		
		if (state.hasBox(box.getX(), box.getY())) {
			//A box was moved: check if that box is still movable
			return isMovable(state, box, new LinkedList<Location>());
			
		} else {
			//No box was moved, so it is solvable
			return true;
		}
	}
	
	private boolean isMovable(Board state, Location l, Collection<Location> visited) {
		// If two squares on either side of the box are both empty, it is movable
		visited.add(l);
		Location left = new Location(l, Action.LEFT);
		Location right = new Location(l, Action.RIGHT);
		Location up = new Location(l, Action.UP);
		Location down = new Location(l, Action.DOWN);
		
		return (canBeEmpty(state, left, visited) &&
				canBeEmpty(state, right, visited))
				|| (canBeEmpty(state, up, visited) &&
				canBeEmpty(state, down, visited));
	}
	
	private boolean canBeEmpty(Board state, Location l, Collection<Location> visited) {
		return state.isEmpty(l) || state.getAgentLocation().equals(l)
				|| (state.hasBox(l.getX(), l.getY()) && !visited.contains(l) && isMovable(
						state, l, new LinkedList<Location>(visited)));
	}

}
