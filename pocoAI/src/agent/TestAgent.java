package agent;

import java.util.Random;

import simulation.Action;
import simulation.Board;

public class TestAgent implements Agent {

	public TestAgent() {
		solutionLength = 10;
	}
	
	public TestAgent(int solutionLength) {
		this.solutionLength = solutionLength;
	}
	
	private final int solutionLength;
	private Random gen = new Random();
	
	@Override
	public Solution findSolution(Board startState) {
		Solution s = new Solution();
		for (int i = 0; i < solutionLength; i++) {
			s.appendAction(getRandomAction());
		}
		
		s.addStatistic("Steps", solutionLength);
		s.addStatistic("Statistic2", "nonsense");
		s.addStatistic("Statistic3", "nonsense");
		s.addStatistic("Statistic4", "nonsense");
		
		return s;
	}

	@Override
	public String algorithmName() {
		return "Random (" + gen.hashCode() + ")";
	}

	private Action getRandomAction() {
		return Action.values()[gen.nextInt(4)];
	}
}
