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
public class Board {

	private Square[][] board;
	private int xPos;
	private int yPos;
	private Location goalLoaction;
	private List<Location> boxLocations;

	public Board(BoardType boardType) {
		// Not sure which type would be best
		boxLocations = new ArrayList<Location>();

		switch (boardType) {
		case EASY:
			board = new Square[5][5];
			for (int h = 0; h < getHeight(); h++) {
				for (int w = 0; w < getWidth(); w++) {
					// Set outside walls
					if (isOutsideWall(h, w, getHeight(), getWidth())) {
						board[h][w] = new Square(SquareType.WALL,
								SquareContents.EMPTY);
					}

					// Set goal in top left
					else if (h == 1 && w == 1) {
						board[h][w] = new Square(SquareType.GOAL,
								SquareContents.EMPTY);
						goalLoaction = new Location(w, h);
					}

					// Set boxes
					else if ((h == 2 && w == 2) || (h == 2 && w == 3)) {
						board[h][w] = new Square(SquareType.EMPTY,
								SquareContents.BOX);
						boxLocations.add(new Location(w, h));
					}

					// The rest
					else {
						board[h][w] = new Square(SquareType.EMPTY,
								SquareContents.EMPTY);
					}

				}
			}
			// Start position
			xPos = 3;
			yPos = 3;
			board[yPos][xPos].setContents(SquareContents.AGENT);

		case MEDIUM:
			return;
		case HARD:
			return;
		}
	}

	private static boolean isOutsideWall(int h, int w, int height, int width) {
		return h == 0 || w == 0 || h == height - 1 || w == width - 1;
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
		return new Location(goalLoaction.getX(), goalLoaction.getY());
	}

	/**
	 * @return All of the box locations
	 */
	public List<Location> getBoxlLocations() {
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
		return board[y][x].getContents() == SquareContents.BOX;
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
		return board[y][x].getType() == SquareType.WALL;
	}

	/**
	 * @return the deep clone of the board
	 */
	public Square[][] getDeepClone() {
		Square[][] clone = new Square[getWidth()][getHeight()];
		for (int h = 0; h < getHeight(); h++) {
			for (int w = 0; w < getWidth(); w++) {
				clone[h][w] = new Square(board[h][w].getType(),
						board[h][w].getContents());
			}
		}
		return clone;
	}
}
