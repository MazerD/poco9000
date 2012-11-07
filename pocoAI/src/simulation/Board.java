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
	
	private Square[][] board;
	private int xPos;
	private int yPos;
	
	public Board(BoardType boardType) {
		switch(boardType) {
		case EASY:
			board = new Square[5][5];
			for (int h = 0; h < getHeight(); h++) {
				 for (int w = 0; w < getWidth(); w++) {
					 // Set outside walls
					 if (isOutsideWall(h, w, getHeight(), getWidth())) {
						 board[h][w] = new Square(SquareType.WALL, SquareContents.EMPTY);
					 }
					 
					 // Set goal in top left
					 else if (h == 1 && w == 1) {
						 board[h][w] = new Square(SquareType.GOAL, SquareContents.EMPTY);
					 }
					 
					 // Set boxes
					 else if ((h == 2 && w == 2) || (h == 2 && w == 3)) {
						 board[h][w] = new Square(SquareType.EMPTY, SquareContents.BOX);
					 }
					 
					 // The rest
					 else  {
						 board[h][w] = new Square(SquareType.EMPTY, SquareContents.EMPTY);
					 }
					 
				 }
			}
			// Start position
			xPos = 3;
			yPos = 3;
			
		case MEDIUM:
			return;
		case HARD:
			return;
		}
	}
	
	private static boolean isOutsideWall(int h, int w, int height, int width){
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
	// Should this do any error checking?
	public void moveAgent(Action agentAction) {
		board[yPos][xPos].setContents(SquareContents.EMPTY);
		switch (agentAction) {
		case LEFT:
			xPos--;
		case RIGHT:
			xPos++;
		case UP:
			yPos--;
		case DOWN:
			yPos++;
		}
		board[yPos][xPos].setContents(SquareContents.AGENT);
	}

	/**
	 * Gets the contents of the square at the given location.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public SquareContents getSquareContents(int x, int y) {
		if (x == xPos && y == yPos)
			return SquareContents.AGENT;
		return board[y][x].getContents();
	}

	/**
	 * Gets the square type at the given location
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public SquareType getSquareType(int x, int y) {
		return board[y][x].getType();
	}

	/**
	 * Gets the width of the board
	 * @return
	 */
	public int getWidth() {
		return board[0].length;
	}

	/**
	 * Gets the height of the board
	 * @return
	 */
	public int getHeight() {
		return board.length;
	}
	
	/**
	 * @return the deep clone of the board
	 */
	public Square[][] getDeepClone() {
		Square[][] clone = new Square[getWidth()][getHeight()];
		for (int h = 0; h < getHeight(); h++) {
			 for (int w = 0; w < getWidth(); w++) {
				 clone[h][w] = new Square(board[h][w].getType(), board[h][w].getContents());
			 }
		}
		return clone;
	}
}
