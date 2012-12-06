package agent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import simulation.Action;
import simulation.Board;
import simulation.Location;

public class FloodFillAgent extends AbstractAgent {
	
	private Solution s;
	private int[][] floodedBoard;
	private Board originalState;
	private Board currentState;
	
	private final int NOT_VISITED = -1;
	private final int CANNOT_MOVE = Integer.MAX_VALUE;
	
	@Override
	public String algorithmName() {
		return "Flood Fill";
	}

	@Override
	protected Solution doFindSolution(Board startState) {
		s = new Solution();
		floodedBoard = new int[startState.getHeight()][startState.getWidth()];
		originalState = startState;
		currentState = startState.clone();
		
		Path best = new Path();
		Location bestBox = null;
		Map<Location, Path> boxToGoalPaths = new HashMap<Location, Path>();
		
//		s.appendAllActions(getPath(startState.getAgentLocation(), startState.getGoalLocation(), true).actions);
		
		// Get first box done
		for (Location box : startState.getBoxLocations()) {
			Path toBox = getPath(startState.getAgentLocation(), box, true);
			Path toGoal = getPath(box, startState.getGoalLocation(), false);
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
		while (!boxToGoalPaths.isEmpty()) {
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
	
	private Path getPath(Location from, Location to, boolean isToBox) {
		floodFill(to, isToBox);
		
		// Debugging
		for (int h = 0; h < floodedBoard.length; h++) {
			for (int w = 0; w < floodedBoard[h].length; w++) {
				System.out.print("[" + floodedBoard[h][w] + "]");
			}
			System.out.println();
		}
		
		List<Action> actions = new LinkedList<Action>();
		Location fromClone = new Location(from.getX(), from.getY());
		
		if (isToBox) {
			actions.addAll(getActions(fromClone, 0, true));
		} else {
			// TODO need to make sure to find a path that is actually POSSIBLE
			List<Action> boxActions = getActions(fromClone, -1, false);
			
			// Find actions for Agent
			Iterator<Action> iter = boxActions.iterator();
			while (iter.hasNext()) {
				Action curAct = iter.next();
				
				// Use actions to act as what "side" the agent needs to be on to do the move
				Action wantedSide = reverseAction(curAct);
				Action currentSide = getAgentSide(from);
				if (wantedSide != currentSide) {
					int x = fromClone.getX() + wantedSide.getDX();
					int y = fromClone.getY() + wantedSide.getDY();
					boolean isReachable = floodFill(new Location(x, y), false);
					
					if (isReachable) {
						actions.addAll(getActions(currentState.getAgentLocation(), -1, true));
					} else {
						// get possible locations for box
						
						// Move box so it is reachable
						// Store actions
						
						x = fromClone.getX() + wantedSide.getDX();
						y = fromClone.getY() + wantedSide.getDY();
						floodFill(new Location(x, y), false);
						actions.addAll(getActions(currentState.getAgentLocation(), -1, true));
						
						// get new actions
					}
					
					// Add the act of moving the box
					// NO THIS IS OLD SHIT AND WON'T WORK WITH NEW POSITION. FUCK YOU. JUST. FUCK. YOU.
					actions.add(curAct);
				}
			}
			
		}
		
		// TODO think of way to handle actually pushing the box
		
		return new Path(actions, actions.size());
	}
	
	private List<Action> getActions(Location start, int end, boolean doMoveAgent) {
		List<Action> actions = new LinkedList<Action>();
		int wanted = floodedBoard[start.getY()][start.getX()] - 1;
		while (wanted != end) {
			Action nextAct = getNexAction(start, wanted);
			actions.add(nextAct);
			if (doMoveAgent) currentState.moveAgent(nextAct);
			wanted--;
			
			// Update agent location
			start.setX(start.getX() + nextAct.getDX());
			start.setY(start.getY() + nextAct.getDY());
		}
		return actions;
	}
	
	private Action getAgentSide(Location box) {
		int agentX = currentState.getAgentLocation().getX();
		int agentY = currentState.getAgentLocation().getY();
		
		int boxX = box.getX();
		int boxY = box.getY();
		
		if (boxX - 1 == agentX && boxY == agentY) {
			return Action.LEFT;
		} else if (boxX + 1 == agentX && boxY == agentY) {
			return Action.RIGHT;
		} else if (boxX == agentX && boxY + 1 == agentY) {
			return Action.DOWN;
		} else {
			return Action.UP;
		}
	}
	
	private List<Action> makeReachable(Location box, Action side) {
		List<Action> actions = new LinkedList<Action>();
		
		switch (side) {
		case DOWN:
			if (currentState.canMoveAgent(Action.UP)) {
				currentState.moveAgent(Action.UP);
//				int x = start.getX() + nextAct.getDX());
//				int y = start.getY() + nextAct.getDY());
			}
		}
		
		return actions;
	}
	
	private boolean floodFill(Location goal, boolean boxIsGoal) {
		// Set to constant
		for (int h = 0; h < floodedBoard.length; h++) {
			for (int w = 0; w < floodedBoard[h].length; w++) {
				floodedBoard[h][w] = NOT_VISITED;
			}
		}
		
		int goalX = goal.getX();
		int goalY = goal.getY();
		if (currentState.isOutOfBounds(goalX, goalY) || currentState.isWall(goalX, goalY) ||
				(!boxIsGoal && currentState.hasBox(goalX, goalY))) {
			return false;
		}
		floodedBoard[goalY][goalX] = 0;
		
		int openCells = originalState.getHeight() * originalState.getWidth();
		int curDist = 0;
		while (openCells != 0) {
			openCells--;
			for (int h = 0; h < floodedBoard.length; h++) {
				for (int w = 0; w < floodedBoard[h].length; w++) {
					if (floodedBoard[h][w] == curDist) {
						int newDist = curDist + 1;
						
						// Left
						setFloodValue(w - 1, h, newDist);
						
						// Right
						setFloodValue(w + 1, h, newDist);
						
						// Up
						setFloodValue(w, h - 1, newDist);
						
						// Down
						setFloodValue(w, h + 1, newDist);
					}
				}
			}
			curDist++;
		}
		return true;
	}
	
	private void setFloodValue(int x, int y, int val) {
		if (!currentState.isOutOfBounds(x, y) && floodedBoard[y][x] == NOT_VISITED) {
			if (currentState.isWall(x, y) || currentState.hasBox(x, y)) {
				floodedBoard[y][x] = CANNOT_MOVE;
			} else {
				floodedBoard[y][x] = val;
			}
		}
	}
	
	private Action getNexAction(Location curr, int wanted) {
		int x = curr.getX();
		int y = curr.getY();
		
		if (!currentState.isOutOfBounds(x + 1, y) && floodedBoard[y][x + 1] == wanted) {
			return Action.RIGHT;
		} else if (!currentState.isOutOfBounds(x - 1, y) && floodedBoard[y][x - 1] == wanted) {
			return Action.LEFT;
		} else if (!currentState.isOutOfBounds(x, y + 1) && floodedBoard[y + 1][x] == wanted) {
			return Action.DOWN;
		} else {
			return Action.UP;
		}
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
