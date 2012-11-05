package main;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;

import simulation.Action;
import simulation.Board;
import ui.PocoFrame;

public class Game {

	public static void main(String[] args) {
		// Create board
		Board b = new Board();
		
		// Create agent
		
		// Give board data to agent, and run agent's solution algorithm
		Iterator<Action> solution = new ArrayList<Action>().iterator();
		
		// Give agent's solution output to ui
		JFrame frame = new PocoFrame(b, solution);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
}
