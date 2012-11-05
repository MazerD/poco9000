package simulation;

/**
 * I am assuming the origin (0, 0) is in the top left, and down is the positive
 * y direction, as with swing.
 * 
 * Also, this interface assumes there is only one agent. This may need to be
 * changed in the future.
 * 
 */
public class Board {

	/**
	 * Moves the agent one square in the indicated direction
	 * @param agentAction The direction in which to move
	 */
	public void moveAgent(Action agentAction) {
		// TODO
	}

	/**
	 * Gets the contents of the square at the given location.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public SquareContents getSquareContents(int x, int y) {
		// TODO
		return SquareContents.EMPTY;
	}

	/**
	 * Gets the square type at the given location
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public SquareType getSquareType(int x, int y) {
		// TODO
		return SquareType.EMPTY;
	}

	/**
	 * Gets the width of the board
	 * @return
	 */
	public int getWidth() {
		// TODO
		return 5;
	}

	/**
	 * Gets the height of the board
	 * @return
	 */
	public int getHeight() {
		// TODO
		return 5;
	}
}
