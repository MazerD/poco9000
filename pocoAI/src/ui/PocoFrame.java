package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.Timer;

import simulation.Action;
import simulation.Board;

/**
 * Takes a Board indicating the start state, and a series of agent actions, and
 * animates the agent taking these actions on the board
 * 
 * @author Marjie
 * 
 */
public class PocoFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 3837689039443649717L;
	private static final int SQUARE_WIDTH = 30;
	private static final int MARGIN = 2;
	private static final int INTERIOR_WIDTH = SQUARE_WIDTH - (2 * MARGIN);
	private static final int OBJECT_WIDTH = INTERIOR_WIDTH - (2 * MARGIN);
	private static final int ARC_WIDTH = OBJECT_WIDTH / 4;
	private static final int STEP_TIME_MILLIS = 1000;

	private Board state;
	private Iterator<Action> actions;

	/**
	 * This calls actionPerformed at regular intervals, which causes animation
	 * to be updated
	 */
	private Timer timer;

	public PocoFrame(Board startState, Iterator<Action> agentActions) {
		state = startState;
		actions = agentActions;

		this.setPreferredSize(new Dimension(SQUARE_WIDTH * state.getWidth(),
				SQUARE_WIDTH * state.getHeight()));
		// this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		System.out.println("Board size: (" + state.getWidth() + ", "
				+ state.getHeight() + ")\n");

		repaint();

		timer = new Timer(STEP_TIME_MILLIS, this);
		timer.start();
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		for (int x = 0; x < state.getWidth(); x++) {
			for (int y = 0; y < state.getHeight(); y++) {

				switch (state.getSquareType(x, y)) {
				case WALL:
					return;
				case GOAL:
					g.setColor(new Color(200, 255, 200));
					break;
				case EMPTY:
					g.setColor(Color.WHITE);
					break;
				}

				int xScreen = x * SQUARE_WIDTH + MARGIN;
				int yScreen = y * SQUARE_WIDTH + MARGIN;
				g.fillRect(xScreen, yScreen, INTERIOR_WIDTH, INTERIOR_WIDTH);

				switch (state.getSquareContents(x, y)) {
				case AGENT:
					g.setColor(Color.ORANGE);
					g.fillOval(xScreen + MARGIN, yScreen + MARGIN,
							OBJECT_WIDTH, OBJECT_WIDTH);
					System.out.println("Agent at (" + x + "," + y + ")");
					break;
				case BOX:
					g.setColor(Color.GREEN);
					g.fillRoundRect(xScreen + MARGIN, yScreen + MARGIN,
							OBJECT_WIDTH, OBJECT_WIDTH, ARC_WIDTH, ARC_WIDTH);
					break;
				default:
					break;
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!actions.hasNext()) {
			timer.stop();
			return;
		}

		state.moveAgent(actions.next());
		repaint();
	}

}
