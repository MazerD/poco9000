package agent;

import simulation.Board;

public abstract class AbstractAgent implements Agent {

	@Override
	public final Solution findSolution(Board startState) {
		long startTime = System.currentTimeMillis();
		Solution s = doFindSolution(startState);
		s.addStatistic("Time (ms)", System.currentTimeMillis() - startTime);
		s.addStatistic("Steps", s.length());
		return s;
	}

	protected abstract Solution doFindSolution(Board startState);
}
