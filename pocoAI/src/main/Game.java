package main;

import javax.swing.JFrame;

import simulation.Board;
import simulation.BoardFactory;
import ui.PocoFrame;
import agent.Agent;
import agent.DepthFirstAgent;
import agent.HeuristicAgent;
import agent.SolutionSet;
import agent.heuristics.PathFindingHeuristic;

public class Game {

	public static void main(String[] args) {
		// Create board
		Board b = BoardFactory.easyBoard();
		
		// Add any number of agent objects as parameters to runAll()
		//SolutionSet set = runAll(b, new DepthFirstAgent(), new TestAgent(30));
		SolutionSet set = runAll(b, new DepthFirstAgent(), new HeuristicAgent(new PathFindingHeuristic()));
		
		// Give agent's solution output to ui
		JFrame frame = new PocoFrame(b, set);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private static SolutionSet runAll(Board startState, Agent... agents) {
		SolutionSet set = new SolutionSet();
		for (Agent a : agents) {
			set.addSolution(a.algorithmName(), a.findSolution(startState.clone()));
		}
		return set;
	}
}
