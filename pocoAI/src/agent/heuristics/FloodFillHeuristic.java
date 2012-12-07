package agent.heuristics;

import java.util.HashMap;

import simulation.Board;
import simulation.Location;

public class FloodFillHeuristic implements Heuristic {
	
	private Board state;
	private static int[][] floodedBoard;
	
	private final int NOT_VISITED = -1;
	private final int CANNOT_MOVE = Integer.MAX_VALUE;
	
	@Override
	public int value(Board state) {
		this.state = state;
		
		int val = 0;
		HashMap<Location, Integer> paths = getPaths(state);
		for (Location box : state.getBoxLocations()) {
			val += paths.get(box);
		}
		return val;
	}

	@Override
	public String name() {
		return "Flood Fill";
	}
	
	private HashMap<Location, Integer> getPaths(Board state) {
		HashMap<Location, Integer> map = new HashMap<Location, Integer>();
		if (floodedBoard == null) floodFill(state.getGoalLocation());
		for (Location box : state.getBoxLocations()) {
			map.put(box, floodedBoard[box.getY()][box.getX()]);
		}
		return map;
	}
	
	private int[][] floodFill(Location goal) {
		floodedBoard = new int[state.getHeight()][state.getWidth()];
		
		// Set to constant
		for (int h = 0; h < floodedBoard.length; h++) {
			for (int w = 0; w < floodedBoard[h].length; w++) {
				floodedBoard[h][w] = NOT_VISITED;
			}
		}
		
		floodedBoard[goal.getY()][goal.getX()] = 0;
		
		int openCells = state.getHeight() * state.getWidth();
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
		return floodedBoard;
	}
	
	private void setFloodValue(int x, int y, int val) {
		if (!state.isOutOfBounds(x, y) && floodedBoard[y][x] == NOT_VISITED) {
			if (state.isWall(x, y) /*|| state.hasBox(x, y)*/) {
				floodedBoard[y][x] = CANNOT_MOVE;
			} else {
				floodedBoard[y][x] = val;
			}
		}
	}

}
