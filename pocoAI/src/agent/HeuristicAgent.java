package agent;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import simulation.Action;
import simulation.Board;

public class HeuristicAgent extends AbstractAgent {

	private Heuristic heuristic;
	private Collection<Board> visited = new LinkedList<Board>();
	private Solution s;
	
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
		s = new Solution();
		s.addStatistic("Manhattan Distance", heuristic.value(startState));
		visited.clear();
		solve(startState);
		
		return s;
	}
	
	private boolean solve(Board state)
	{
		if (state.getBoxLocations().size() == 0) {
			return true;
		}
		
		visited.add(state);
		
		PriorityQueue<Move> candidates = new PriorityQueue<Move>(4, new HeurComp());
		
		//System.out.println("Start cycle.");
		
		for (Action a : Action.values()) {
			if (state.canMoveAgent(a)) {
				Board moved = state.clone();
				moved.moveAgent(a);
				
				//System.out.println("Added " + a + " at distance " + heuristic.value(moved));
				
				candidates.add(new Move(moved, a));
			}
		}
		
		Iterator<Move> iter = candidates.iterator();
		Move m;

		while (iter.hasNext())
		{
			m = iter.next();
			//System.out.println(heuristic.value(m.getBoard()));
			//System.out.println(m.getAction());
		}
		
		iter = candidates.iterator();
		while (iter.hasNext())
		{
			m = iter.next();
			if (!visited.contains(m.getBoard()) && solve(m.getBoard())) {
				s.prependAction(m.getAction());
				return true;
			}
		}
		
		return false;
	}
	
	private class Move
	{
		private Action a;
		private Board b;
		
		public Move(Board b, Action a)
		{
			this.b = b;
			this.a = a;
		}
		
		public Action getAction()
		{
			return a;
		}
		
		public Board getBoard()
		{
			return b;
		}
	}
	
	private class HeurComp implements Comparator<Move>
	{

		@Override
		public int compare(Move arg0, Move arg1) {
			int x = heuristic.value(arg0.getBoard());
			int y = heuristic.value(arg1.getBoard());
			
			if (x > y)
				return 1;
			else if (x < y)
				return -1;
			else
				return 0;
		}
	}
}
