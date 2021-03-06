package simulation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * I am assuming the origin (0, 0) is in the top left, and down is the positive
 * y direction, as with swing.
 * 
 * Also, this interface assumes there is only one agent. This may need to be
 * changed in the future.
 * 
 */
public class Board implements Cloneable {

	// This is for keeping track of how many boards are created, which is used
	// for the nodes visited statistic
	private static ThreadLocal<Integer> COUNTER = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			return 0;
		}
	};
	public static int getCount() { return COUNTER.get(); }
	public static void resetCount() { COUNTER.set(0); }
	
	private Square[][] board;
	private int xPos;
	private int yPos;
	private Location goalLocation;
	private List<Location> boxLocations = new LinkedList<Location>();

	public Board(Square[][] boardData) {
		board = boardData;
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				switch (getSquareContents(x, y)) {
				case AGENT:
					xPos = x;
					yPos = y;
					break;
				case BOX:
					boxLocations.add(new Location(x, y));
					break;
				default:
					break;
				}

				switch (getSquareType(x, y)) {
				case GOAL:
					goalLocation = new Location(x, y);
				default:
					break;
				}
			}
		}
		
		//This keeps track of how many boards are created
		COUNTER.set(COUNTER.get() + 1);
	}

	/**
	 * Moves the agent one square in the indicated direction.
	 * <ul>
	 * <li>If the square the agent wishes to move to is empty, it simply moves
	 * the agent to that square.</li>
	 * <li>If the square contains a box, and the square on the other side of the
	 * box is empty (ie, the agent can push the box), then it moves the agent,
	 * and moves the box to the next square.</li>
	 * <li>If the square contains a wall or a box that cannot be pushed, it does
	 * nothing.</li>
	 * </ul>
	 * 
	 * @param agentAction
	 *            The direction in which to move
	 */
	public void moveAgent(Action agentAction) {
		if (canMoveAgent(agentAction)) {
			board[yPos][xPos].setContents(SquareContents.EMPTY);

			int tempX = xPos + agentAction.getDX();
			int tempY = yPos + agentAction.getDY();

			if (hasBox(tempX, tempY)) {
				int moveToX = tempX + agentAction.getDX();
				int moveToY = tempY + agentAction.getDY();

				board[tempY][tempX].setContents(SquareContents.EMPTY);
				xPos = tempX;
				yPos = tempY;

				//int index = boxLocations.lastIndexOf(new Location(tempX, tempY));
				
				// Replace previous line with an iterator through the list
				// On x, y position match, replace index with the match to get the last occurrence
				
				int index = -1;
				Iterator<Location> iter = boxLocations.iterator();
				Location tempL;
				int i = 0;
				while (iter.hasNext())
				{
					tempL = iter.next();
					if ((tempL.getX() == tempX) && (tempL.getY() == tempY))
						index = i;
					i++;
				}

				if (board[moveToY][moveToX].getType() == SquareType.GOAL) {
					boxLocations.remove(index);
				} else {
					boxLocations.set(index, new Location(moveToX, moveToY));
					board[moveToY][moveToX].setContents(SquareContents.BOX);
				}

			} else {
				xPos = tempX;
				yPos = tempY;
			}

			board[yPos][xPos].setContents(SquareContents.AGENT);
		}
	}

	public boolean canMoveAgent(Action agentAction) {
		int tempX = xPos + agentAction.getDX();
		int tempY = yPos + agentAction.getDY();

		if (hasBox(tempX, tempY)) {
			int moveToX = tempX + agentAction.getDX();
			int moveToY = tempY + agentAction.getDY();

			if (hasBox(moveToX, moveToY) || isWall(moveToX, moveToY))
				return false;

		} else if (isWall(tempX, tempY)) {
			return false;
		}

		return true;
	}

	/**
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @return The contents of the square at the given location
	 */
	public SquareContents getSquareContents(int x, int y) {
		return board[y][x].getContents();
	}

	/**
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @return The type of square at the given location
	 */
	public SquareType getSquareType(int x, int y) {
		return board[y][x].getType();
	}

	/**
	 * @return The width of the board
	 */
	public int getWidth() {
		return board[0].length;
	}

	/**
	 * @return The height of the board
	 */
	public int getHeight() {
		return board.length;
	}

	/**
	 * @return The agent's current location
	 */
	public Location getAgentLocation() {
		return new Location(xPos, yPos);
	}

	/**
	 * @return The goal's location
	 */
	public Location getGoalLocation() {
		return goalLocation;
	}

	/**
	 * @return All of the box locations
	 */
	public List<Location> getBoxLocations() {
		return boxLocations;
	}

	/**
	 * Determines if a given location is empty or not.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @return true if the square is empty, false otherwise
	 */
	public boolean isEmpty(int x, int y) {
		return !isOutOfBounds(x, y) && board[y][x].getContents() == SquareContents.EMPTY;
	}
	
	public boolean isEmpty(Location l) {
		return isEmpty(l.getX(), l.getY());
	}

	/**
	 * Determines if a given location has a box.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @return true if the square has a box, false otherwise
	 */
	public boolean hasBox(int x, int y) {
		return !isOutOfBounds(x, y)
				&& board[y][x].getContents() == SquareContents.BOX;
	}

	/**
	 * Determines if a given location is a wall.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @return true if the square is a wall, false otherwise
	 */
	public boolean isWall(int x, int y) {
		return isOutOfBounds(x, y) || board[y][x].getType() == SquareType.WALL;
	}

	public boolean isOutOfBounds(int x, int y) {
		return x < 0 || y < 0 || x >= getWidth() || y >= getHeight();
	}
	
	/**
	 * @return the deep clone of the board
	 */
	@Override
	public Board clone() {
		Square[][] clone = new Square[getHeight()][getWidth()];
		for (int h = 0; h < getHeight(); h++) {
			for (int w = 0; w < getWidth(); w++) {
				clone[h][w] = new Square(board[h][w].getType(),
						board[h][w].getContents());
			}
		}

		Board b = new Board(clone);
		return b;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Board))
			return false;

		Board b = (Board) other;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (!b.board[i][j].equals(board[i][j]))
					return false;
			}
		}

		return true;
	}
	
	@Override
	public String toString() {
		String s = "";
		
		for (int i = 0; i < this.getWidth() + 2; i++) {
			s += "X";
		}
		s += "\n";
		
		for (int h = 0; h < board.length; h++) {
			s += "X";
			for (int w = 0; w < board[h].length; w++) {
				s += board[h][w].toString();
			}
			s += "X\n";
		}
		
		for (int i = 0; i < this.getWidth() + 2; i++) {
			s += "X";
		}
		
		return s;
	}
	
}
