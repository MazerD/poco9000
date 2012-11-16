package simulation;

public class BoardFactory {

	public static Board customBoard(int size, Location agent, Location goal,
			Location[] boxes, Location[] walls) {
		
		Square[][] board = new Square[size][size];
		for (int h = 0; h < board.length; h++) {
			for (int w = 0; w < board[h].length; w++) {
				board[h][w] = new Square(SquareType.EMPTY, SquareContents.EMPTY);
			}
		}
		
		for (Location wall : walls) {
			board[wall.getY()][wall.getX()].setType(SquareType.WALL);
		}
		
		for (Location box : boxes) {
			board[box.getY()][box.getX()].setContents(SquareContents.BOX);
		}
		
		board[agent.getY()][agent.getX()].setContents(SquareContents.AGENT);
		board[goal.getY()][goal.getX()].setType(SquareType.GOAL);

		return new Board(board);
	}

	public static Board easyBoard() {
		return customBoard(5, new Location(3, 3), new Location(0, 0),
				new Location[] { l(2, 3), l(3, 2) },
				new Location[] { l(0, 2), l(1, 2), l(3, 1), l(4, 4) });
	}
	
	// This was to make the arrays of new locations more readable
	private static Location l(int x, int y) {
		return new Location(x, y);
	}
}
