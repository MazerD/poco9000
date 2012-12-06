package agent.heuristics;

import java.util.HashMap;
import java.util.LinkedList;

import simulation.Action;
import simulation.Board;
import simulation.Location;

public class PathFindingHeuristic implements Heuristic {

	@Override
	public int value(Board state) {
		int val = 0;
		HashMap<Location, Integer> shortestPaths = findShortestPaths(state);
		for (Location box : state.getBoxLocations()) {
			val += shortestPaths.get(box);
		}
		return val;
	}

	@Override
	public String name() {
		return "Path Finding";
	}

	private static HashMap<Location, Integer> findShortestPaths(Board state) {
		HashMap<Location, Integer> map = new HashMap<Location, Integer>();
		map.put(state.getGoalLocation(), 0);
		LinkedList<Location> queue = new LinkedList<Location>();
		queue.add(state.getGoalLocation());
		
		while (!queue.isEmpty()) {
			Location cur = queue.removeFirst();
			
			Location[] adjacent = new Location[] { new Location(cur, Action.LEFT),
					new Location(cur, Action.RIGHT), new Location(cur, Action.UP),
					new Location(cur, Action.DOWN) };
			
			for (Location l : adjacent) {
				if (!state.isWall(l.getX(), l.getY()) && !map.containsKey(l)) {
					map.put(l, map.get(cur) + 1);
					queue.addLast(l);
				}
			}
		}
		
		return map;
	}
}
