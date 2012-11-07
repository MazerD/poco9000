package main;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;

import simulation.Action;
import simulation.Board;
import simulation.BoardType;
import ui.PocoFrame;

public class Game {

	public static void main(String[] args) {
		// Create board
		Board b = new Board(BoardType.EASY);
		
		// Create agent
		
		// Give board data to agent, and run agent's solution algorithm
		ArrayList<Action> actions = new ArrayList<Action>();
		actions.add(Action.UP);
		actions.add(Action.LEFT);
		actions.add(Action.DOWN);
		actions.add(Action.LEFT);
		actions.add(Action.UP);
		Iterator<Action> solution = actions.iterator();
		
		// Give agent's solution output to ui
		JFrame frame = new PocoFrame(b, solution);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
}
