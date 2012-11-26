package simulation;

import java.util.ArrayList;
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

	private Square[][] board;
	private int xPos;
	private int yPos;
	private Location goalLocation;
	
	//TODO: Be careful with this -- the list will need to be updated whenever a box is moved
	private List<Location> boxLocations = new ArrayList<Location>();

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
		board[yPos][xPos].setContents(SquareContents.EMPTY);

		int tempX = xPos + agentAction.getDX();
		int tempY = yPos + agentAction.getDY();

		if (hasBox(tempX, tempY)) {
			int moveToX = tempX + agentAction.getDX();
			int moveToY = tempY + agentAction.getDY();

			if (hasBox(moveToX, moveToY) || isWall(moveToX, moveToY)) {
				// Can't move
				// Throw error?
			} else if (board[moveToY][moveToX].getType() == SquareType.GOAL) {
				board[tempY][tempX].setContents(SquareContents.EMPTY);
				xPos = tempX;
				yPos = tempY;
			} else {
				board[moveToY][moveToX].setContents(SquareContents.BOX);
				xPos = tempX;
				yPos = tempY;
			}

		} else if (isWall(tempX, tempY)) {
			// Can't move
			// Throw error?
		} else {
			xPos = tempX;
			yPos = tempY;
		}

		board[yPos][xPos].setContents(SquareContents.AGENT);
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
	 * @return The typpe of square at the given location
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
		// Deep copy just in case
		return new Location(goalLocation.getX(), goalLocation.getY());
	}

	/**
	 * @return All of the box locations
	 */
	public List<Location> getBoxLocations() {
		// Deep copy just in case
		List<Location> ret = new ArrayList<Location>();

		for (Location loc : boxLocations) {
			ret.add(new Location(loc.getX(), loc.getY()));
		}

		return ret;
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
		return board[y][x].getContents() == SquareContents.EMPTY;
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
		return !isOutOfBounds(x, y) && board[y][x].getContents() == SquareContents.BOX;
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
}
