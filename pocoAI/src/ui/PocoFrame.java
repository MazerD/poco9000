package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import simulation.Action;
import simulation.Board;
import agent.Solution;
import agent.SolutionSet;
import agent.SolutionSet.SolutionCompletionListener;

/**
 * Takes a Board indicating the start state, and a series of agent actions, and
 * animates the agent taking these actions on the board
 * 
 * @author Marjie
 * 
 */
public class PocoFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 3837689039443649717L;
	private static final int BOARD_MARGIN = 10;
	private static final int SQUARE_WIDTH = 30;
	private static final int MARGIN = 1;
	private static final int INTERIOR_WIDTH = SQUARE_WIDTH - (2 * MARGIN);
	private static final int OBJECT_WIDTH = INTERIOR_WIDTH - (2 * MARGIN);
	private static final int STEP_TIME_MILLIS = 300;

	private Board startState;
	private SolutionSet solutions;
	private JPanel animationPanel;
	private JLabel infoText;
	private StatisticsPanel statsPanel;

	private Board state;
	private Iterator<Action> actions = null;
	private Timer timer;
	private int step = 0;

	public PocoFrame(Board startState, SolutionSet solutions) {
		super("Poco 9000");
		this.startState = startState;
		state = startState.clone();
		this.solutions = solutions;
		timer = new Timer(STEP_TIME_MILLIS, this);

		this.setLayout(new BorderLayout());

		animationPanel = new MainPanel();
		statsPanel = new StatisticsPanel();
		ControlPanel cp = new ControlPanel();
		solutions.addListener(cp);
		add(cp, BorderLayout.NORTH);
		add(animationPanel, BorderLayout.CENTER);
		add(statsPanel, BorderLayout.SOUTH);
		
		pack();
		repaint();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (actions == null) {
			timer.stop();
			infoText.setText("--");
			return;
		}

		if (!actions.hasNext()) {
			timer.stop();
			infoText.setText("--");
			state = startState.clone();
			actions = null;
		} else {
			state.moveAgent(actions.next());
			step++;
			infoText.setText("" + step);
		}

		repaint();
	}

	private class ControlPanel extends JPanel implements SolutionCompletionListener {

		private static final long serialVersionUID = -7012821875692908254L;
		private JComboBox algSelection;

		public ControlPanel() {
			algSelection = new JComboBox(solutions.algorithmSet().toArray());
			algSelection.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					timer.stop();
					state = startState.clone();
					animationPanel.repaint();
					infoText.setText("--");
					
					Object sel = algSelection.getSelectedItem();
					if (sel != null) {
						Solution s = solutions.getSolution((String) sel);
						statsPanel.displaySolutionStats(s);
					}
				}

			});

			JButton start = new JButton("Start");
			start.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Object sel = algSelection.getSelectedItem();
					if (sel == null) {
						infoText.setText("NULL");
					} else {
						Solution s = solutions.getSolution((String) sel);
						if (s != null) {
							actions = s.actions();
							step = 0;
							infoText.setText("0");
							timer.start();
						}
					}
				}

			});
			
			infoText = new JLabel("--");

			add(algSelection);
			add(start);
			add(infoText);
			
			algSelection.setSelectedIndex(0);
		}
		
		@Override
		public void solutionComplete(String algorithmName) {
			if (algSelection.getSelectedItem().equals(algorithmName)) {
				Object sel = algSelection.getSelectedItem();
				if (sel != null) {
					Solution s = solutions.getSolution((String) sel);
					statsPanel.displaySolutionStats(s);
				}
			}
		}

	}

	private class MainPanel extends JPanel {

		private static final long serialVersionUID = 6201087626842474181L;
		private int boardWidth;
		private int boardHeight;

		public MainPanel() {
			boardWidth = startState.getWidth() * SQUARE_WIDTH + 2 * BOARD_MARGIN;
			boardHeight = startState.getHeight() * SQUARE_WIDTH + 2 * BOARD_MARGIN;
			this.setPreferredSize(new Dimension(boardWidth + 15, boardHeight + 15));
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			
			int widthMargin = (this.getWidth() - boardWidth) / 2;
			int heightMargin = (this.getHeight() - boardHeight) / 2;
			
			g.setColor(new Color(38, 74, 155));
			g.fillRect(widthMargin, heightMargin, boardWidth, boardHeight);

			for (int x = 0; x < state.getWidth(); x++) {
				for (int y = 0; y < state.getHeight(); y++) {
					inner: {
						switch (state.getSquareType(x, y)) {
						case WALL:
							break inner;
						case GOAL:
							g.setColor(new Color(173, 204, 192));
							break;
						case EMPTY:
							g.setColor(new Color(222, 230, 247));
							break;
						}

						int xScreen = x * SQUARE_WIDTH + MARGIN + widthMargin + BOARD_MARGIN;
						int yScreen = y * SQUARE_WIDTH + MARGIN + heightMargin + BOARD_MARGIN;
						g.fillRect(xScreen, yScreen, INTERIOR_WIDTH,
								INTERIOR_WIDTH);

						switch (state.getSquareContents(x, y)) {
						case AGENT:
							g.setColor(new Color(219, 174, 96));
							g.fillOval(xScreen + MARGIN, yScreen + MARGIN,
									OBJECT_WIDTH, OBJECT_WIDTH);
							break;
						case BOX:
							g.setColor(new Color(62, 145, 89));
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
	
	private class StatisticsPanel extends JPanel {

		private static final long serialVersionUID = -2784642256448035063L;
		private JTextArea text;
		
		public StatisticsPanel() {
			text = new JTextArea();
			setLayout(new BorderLayout());
			add(new JScrollPane(text), BorderLayout.CENTER);
			setPreferredSize(new Dimension(200, 100));
		}
		
		public void displaySolutionStats(Solution s) {
			if (s != null) {
				text.setText("");
				Collection<String> stats = s.statisticKeySet();
				for (String stat : stats) {
					text.append(stat + ": " + s.getStatistic(stat).toString()
							+ "\n");
				}
			} else {
				text.setText("Algorithm running...");
			}
		}
		
	}

}
