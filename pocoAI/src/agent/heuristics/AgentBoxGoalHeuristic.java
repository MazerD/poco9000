package agent.heuristics;

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
			//Distance from box to goal
			val += Math.abs(box.getX() - state.getGoalLocation().getX());
			val += Math.abs(box.getY() - state.getGoalLocation().getY());
			//Distance from agent to box
			agentDist += Math.abs(box.getX() - state.getAgentLocation().getX());
			agentDist += Math.abs(box.getY() - state.getAgentLocation().getY());
			
			if (agentDist < closestBox)
				closestBox = agentDist;
		}
		val += closestBox;
		
		return val;
	}

	@Override
	public String name() {
		return "Manhattan Distance (Box to Goal + Agent to Box)";
	}

}
