package agent.heuristics;

import java.util.Iterator;
import java.util.List;


import simulation.Board;
import simulation.Location;

public class BoxGoalHeuristic implements Heuristic {

	@Override
	public int value(Board state) {
		List<Location> boxes = state.getBoxLocations();
		int val = 0;
		
		Iterator<Location> iter = boxes.iterator();
		Location box;
		
		while (iter.hasNext())
		{
			box = iter.next();
			val += Math.abs(box.getX() - state.getGoalLocation().getX());
			val += Math.abs(box.getY() - state.getGoalLocation().getY());
		}
		
		return val;
	}

	@Override
	public String name() {
		return "Manhattan Distance (Box to Goal)";
	}

}