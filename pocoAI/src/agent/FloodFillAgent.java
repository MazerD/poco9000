package agent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import simulation.Action;
import simulation.Board;
import simulation.Location;

public class FloodFillAgent extends AbstractAgent {
	
	private Solution s;
	private int[][] floodedBoard;
	private Board state;
	private boolean hasFoundGoal;

	@Override
	public String algorithmName() {
		return "Flood Fill";
	}

	@Override
	protected Solution doFindSolution(Board startState) {
		s = new Solution();
		floodedBoard = new int[startState.getHeight()][startState.getWidth()];
		state = startState;
		
		Path best = new Path();
		Location bestBox = null;
		Map<Location, Path> boxToGoalPaths = new HashMap<Location, Path>();
		
		// Get first box done
		for (Location box : startState.getBoxLocations()) {
			Path toBox = getPath(startState.getAgentLocation(), box);
			Path toGoal = getPath(box, startState.getGoalLocation());
			Path totalPath = new Path(toBox, toGoal);
			boxToGoalPaths.put(box, toGoal);
			
			if (best.moves > totalPath.moves) {
				best = totalPath;
				bestBox = box;
			}
		}
		
		s.appendAllActions(best.actions);
		boxToGoalPaths.remove(bestBox);

		// Rest of the boxes
		while (startState.getBoxLocations().size() != 0) {
			best = new Path();
			
			for (Map.Entry<Location, Path> boxPath : boxToGoalPaths.entrySet()) {
				Path path = boxPath.getValue();
				
				if (best.moves > path.moves * 2) {
					Path toGoal = new Path(getReversedActions(path.actions), path.moves);
					best = new Path(path, toGoal);
				}
			}
			s.appendAllActions(best.actions);
			boxToGoalPaths.remove(bestBox);
		}
		
		return s;
	}
	
	private Path getPath(Location from, Location to) {
		hasFoundGoal = false;
		floodFill(to, from, 0);
		
		List<Action> actions = new LinkedList<Action>();
		int moves = 0;
		
		// TODO think of way to handle actually pushing the box
		
		return new Path(actions, moves);
	}
	
	private void floodFill(Location curr, Location goal, int dist) {
		int x = curr.getX();
		int y = curr.getY();
		
		if (x == goal.getX() && y == goal.getY()) {
			hasFoundGoal = true;
			return;
		}
		
		if (state.isWall(x, y) || state.isOutOfBounds(x, y)) {
			return;
		}
		
		floodedBoard[y][x] = dist;
		dist++;
		
		if (!hasFoundGoal) floodFill(new Location(x + 1, y), goal, dist);
		if (!hasFoundGoal) floodFill(new Location(x, y + 1), goal, dist);
		if (!hasFoundGoal) floodFill(new Location(x - 1, y), goal, dist);
		if (!hasFoundGoal) floodFill(new Location(x, y - 1), goal, dist);
	}
	
	private List<Action> getReversedActions(List<Action> acts) {
		List<Action> rev = new LinkedList<Action>();
		for (Action act : acts) {
			rev.add(reverseAction(act));
		}
		return rev;
	}
	
	private Action reverseAction(Action a) {
		switch (a) {
		case UP:
			return Action.DOWN;
		case DOWN:
			return Action.UP;
		case LEFT:
			return Action.RIGHT;
		case RIGHT:
			return Action.LEFT;
		default:
			return null;
		}
	}
	
	private class Path {
		public List<Action> actions;
		public int moves;

		public Path() {
			moves = Integer.MAX_VALUE;
		}
		
		public Path(List<Action> gActions, int gMoves) {
			actions = gActions;
			moves = gMoves;
		}
		
		public Path(Path one, Path two) {
			one.actions.addAll(two.actions);
			actions = one.actions;
			moves = one.moves + two.moves;
		}
	}

}
