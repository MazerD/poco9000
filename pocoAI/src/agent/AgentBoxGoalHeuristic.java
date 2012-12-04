package agent;

import java.util.Iterator;
import java.util.List;

import simulation.Board;
import simulation.Location;

public class AgentBoxGoalHeuristic implements Heuristic {

	@Override
	public int value(Board state) {
		List<Location> boxes = state.getBoxLocations();
		int val = 0;
		
		Iterator<Location> iter = boxes.iterator();
		Location box;
		int closestBox = 999999999;
		int agentDist;
		
		while (iter.hasNext())
		{
			agentDist = 0;
			box = iter.next();
			if (box.getX() > state.getGoalLocation().getX())
				val += (box.getX() - state.getGoalLocation().getX());
			else val += (state.getGoalLocation().getX() - box.getX());
			if (box.getY() > state.getGoalLocation().getY())
				val += (box.getY() - state.getGoalLocation().getY());
			else val += (state.getGoalLocation().getY() - box.getY());
			
			if (box.getX() > state.getAgentLocation().getX())
				agentDist += (box.getX() - state.getAgentLocation().getX());
			else agentDist += (state.getAgentLocation().getX() - box.getX());
			if (box.getY() > state.getAgentLocation().getY())
				agentDist += (box.getY() - state.getAgentLocation().getY());
			else agentDist += (state.getAgentLocation().getY() - box.getY());
			
			if (agentDist < closestBox)
				closestBox = agentDist;
		}
		val = (val * 5) + closestBox;
		
		return val;
	}

	@Override
	public String name() {
		return "A*: Box - Goal and Agent - Box\nManhattan Distance";
	}

}
