package agent;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import simulation.Action;
import simulation.Board;
import agent.heuristics.Heuristic;

public class HeuristicAgent extends AbstractAgent {

	private Heuristic heuristic;
	private Collection<Board> visited = new LinkedList<Board>();
	PriorityQueue<Move> candidates = new PriorityQueue<Move>(4, new HeurComp());
	
	public HeuristicAgent(Heuristic h)
	{
		heuristic = h;
	}
	
	@Override
	public String algorithmName() {
		return "A*: " + heuristic.name();
	}

	@Override
	protected Solution doFindSolution(Board startState) {
		//s.addStatistic("Manhattan Distance", heuristic.value(startState));
		visited.clear();
		candidates.clear();

		//Initialize visited and candidates with startState
		visited.add(startState);
		for (Action a : Action.values()) {
			if (startState.canMoveAgent(a)) {
				Board moved = startState.clone();
				moved.moveAgent(a);				
				candidates.add(new Move(moved, a, 1, null));
			}
		}
		
		//The search...
		Move m;
		while (!candidates.isEmpty()) {
			//Take the best candidate
			m = candidates.remove();
			
			//If m is a solution state, create and return the solution
			if (m.getBoard().getBoxLocations().isEmpty()) {
				return makeSolution(m);
			}

			//Mark m as visited
			visited.add(m.getBoard());

			//Add all unvisited nodes adjacent to m to the candidate pool
			for (Action a : Action.values()) {
				if (m.getBoard().canMoveAgent(a)) {
					
					Board moved = m.getBoard().clone();
					moved.moveAgent(a);
					
					if (!visited.contains(moved)) {
						boolean hasOther = false;
						
						for (Move move : candidates) {
							if (move.getBoard().equals(moved)) {
								hasOther = true;
								if (m.getCost() + 1 < move.cost) {
									move.cost = m.getCost() + 1;
									move.prev = m;
								}
							}
						}

						if (!hasOther) {
							candidates.add(new Move(moved, a, m.getCost() + 1, m));
						}
					}
				}
			}
		}

		//No solution possible
		return new Solution();
	}
	
	private static Solution makeSolution(Move m) {
		Solution s = new Solution();
		while (m != null) {
			s.prependAction(m.getAction());
			m = m.getPrev();
		}
		return s;
	}
	
	private class Move
	{
		private Action a;
		private Board b;
		private int cost;
		private Move prev;
		
		public Move(Board b, Action a, int cost, Move prev)
		{
			this.b = b;
			this.a = a;
			this.cost = cost;
			this.prev = prev;
		}
		
		public Action getAction()
		{
			return a;
		}
		
		public Board getBoard()
		{
			return b;
		}
		
		public int getCost() {
			return cost;
		}
		
		public Move getPrev() {
			return prev;
		}
	}
	
	private class HeurComp implements Comparator<Move>
	{

		@Override
		public int compare(Move arg0, Move arg1) {
			int x = heuristic.value(arg0.getBoard()) + arg0.getCost();
			int y = heuristic.value(arg1.getBoard()) + arg1.getCost();
			
			if (x > y)
				return 1;
			else if (x < y)
				return -1;
			else
				return 0;
		}
	}
}
