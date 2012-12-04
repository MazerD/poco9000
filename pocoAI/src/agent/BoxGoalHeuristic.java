package agent;

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
			if (box.getX() > state.getGoalLocation().getX())
				val += (box.getX() - state.getGoalLocation().getX());
			else val += (state.getGoalLocation().getX() - box.getX());
			if (box.getY() > state.getGoalLocation().getY())
				val += (box.getY() - state.getGoalLocation().getY());
			else val += (state.getGoalLocation().getY() - box.getY());
		}
		
		return val;
	}

	@Override
	public String name() {

		return "A*: Agent - Goal Manhattan Distance";
	}

}