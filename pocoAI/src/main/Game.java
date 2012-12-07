package main;

import javax.swing.JFrame;

import simulation.Board;
import simulation.BoardFactory;
import ui.PocoFrame;
import agent.*;
import agent.heuristics.*;

public class Game {

	public static void main(String[] args) {
		// Create board
		Board b = BoardFactory.easyBoard();
		
		// Add any number of agent objects as parameters to runAll()
		SolutionSet set = runAll(b, new HeuristicAgent(
				new AgentBoxGoalHeuristic()), new HeuristicAgent(
				new BoxGoalHeuristic()));
		


		// Give agent's solution output to ui
		JFrame frame = new PocoFrame(b, set);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public static SolutionSet runAll(Board startState, Agent... agents) {
		SolutionSet set = new SolutionSet();
		for (Agent a : agents) {
			set.runAndAddSolution(startState.clone(), a);
		}
		return set;
	}
}
