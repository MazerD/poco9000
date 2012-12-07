package agent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import simulation.Board;

public class SolutionSet {

	private HashMap<String, Solution> solutions = new HashMap<String, Solution>();
	private LinkedList<SolutionCompletionListener> listeners = new LinkedList<SolutionCompletionListener>();

	public SolutionSet() {

	}

	public void runAndAddSolution(Board startState, Agent a) {
		solutions.put(a.algorithmName(), null);
		new SolutionHandle(startState, a).start();
	}

	public void addSolution(String algorithmName, Solution sol) {
		solutions.put(algorithmName, sol);
	}

	public Set<String> algorithmSet() {
		return solutions.keySet();
	}

	public Solution getSolution(String algorithmName) {
		return solutions.get(algorithmName);
	}
	
	public void addListener(SolutionCompletionListener l) {
		listeners.add(l);
	}

	private class SolutionHandle extends Thread {

		private Agent a;
		private Board b;

		public SolutionHandle(Board b, Agent a) {
			this.a = a;
			this.b = b;
		}

		@Override
		public void run() {
			Solution s = a.findSolution(b);
			addSolution(a.algorithmName(), s);
			for (SolutionCompletionListener l : listeners) {
				l.solutionComplete(a.algorithmName());
			}
		}

	}

	public static interface SolutionCompletionListener {

		public void solutionComplete(String algorithmName);

	}
}
