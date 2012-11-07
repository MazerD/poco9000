package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
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
	private static final int STEP_TIME_MILLIS = 1000;

	private Board state;
	private Iterator<Action> actions;
	private JPanel panel;

	/**
	 * This calls actionPerformed at regular intervals, which causes animation
	 * to be updated
	 */
	private Timer timer;

	public PocoFrame(Board startState, Iterator<Action> agentActions) {
		state = startState;
		actions = agentActions;

		this.setSize(new Dimension(SQUARE_WIDTH * state.getWidth() + 8,
				SQUARE_WIDTH * state.getHeight() + 28));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new MainPanel();
		add(panel);
		repaint();

		timer = new Timer(STEP_TIME_MILLIS, this);
		timer.start();
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

	private class MainPanel extends JPanel {

		@Override
		public void paint(Graphics g) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());

			for (int x = 0; x < state.getWidth(); x++) {
				for (int y = 0; y < state.getHeight(); y++) {
					inner: {
						switch (state.getSquareType(x, y)) {
						case WALL:
							break inner;
						case GOAL:
							g.setColor(new Color(200, 255, 200));
							break;
						case EMPTY:
							g.setColor(Color.WHITE);
							break;
						}

						int xScreen = x * SQUARE_WIDTH + MARGIN;
						int yScreen = y * SQUARE_WIDTH + MARGIN;
						g.fillRect(xScreen, yScreen, INTERIOR_WIDTH,
								INTERIOR_WIDTH);

						switch (state.getSquareContents(x, y)) {
						case AGENT:
							g.setColor(Color.ORANGE);
							g.fillOval(xScreen + MARGIN, yScreen + MARGIN,
									OBJECT_WIDTH, OBJECT_WIDTH);
							break;
						case BOX:
							g.setColor(Color.CYAN);
							g.fillOval(xScreen + MARGIN, yScreen + MARGIN,
									OBJECT_WIDTH, OBJECT_WIDTH);
							break;
						default:
							break;
						}
					}
				}
			}
		}
	}

}
