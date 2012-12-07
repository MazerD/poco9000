package simulation;

public class BoardFactory {

	public static Board customBoard(int width, int height, Location agent,
			Location goal, Location[] boxes, Location[] walls) {

		Square[][] board = new Square[height][width];
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

	public static Board smallBoard() {
		return customBoard(5, 5, new Location(3, 3), new Location(0, 0),
				new Location[] { l(2, 3), l(3, 2) }, new Location[] { l(0, 2),
						l(1, 2), l(3, 1), l(4, 4) });
	}

	public static Board mediumBoard() {
		return customBoard(10, 8, new Location(4, 4), new Location(0, 5),
				new Location[] { l(1, 1), l(4, 6)},
				new Location[] { l(0, 2), l(1, 2), l(2, 2), l(4, 2), l(5, 2),
						l(2, 3), l(4, 3), l(9, 3), l(6, 4), l(7, 4), l(2, 5),
						l(4, 5), l(5, 5), l(6, 5), l(7, 5), l(2, 6), l(2, 7),
						l(8, 7), l(9, 7) });
	}

	public static Board largeBoard() {
		return customBoard(15, 13, new Location(0, 5), new Location(14, 10),
				new Location[] { l(7, 1), l(9, 11) },
				new Location[] { l(0, 0), l(1, 0), l(2, 0), l(3, 0), l(4, 0),
						l(5, 0), l(6, 0), l(7, 0), l(8, 0), l(12, 0), l(13, 0),
						l(14, 0), l(0, 1), l(1, 1), l(2, 1), l(3, 1), l(12, 1),
						l(13, 1), l(14, 1), l(0, 2), l(1, 2), l(2, 2), l(3, 2),
						l(11, 2), l(0, 3), l(1, 3), l(2, 3), l(3, 3), l(0, 4),
						l(1, 4), l(2, 4), l(11, 4), l(12, 4), l(2, 5), l(5, 5),
						l(6, 5), l(8, 5), l(9, 5), l(10, 5), l(11, 5), l(5, 6),
						l(10, 6), l(0, 7), l(2, 7), l(5, 7), l(10, 7), l(2, 8),
						l(3, 8), l(4, 8), l(5, 8), l(6, 8), l(7, 8), l(9, 8),
						l(10, 8), l(11, 8), l(11, 9), l(12, 9), l(0, 10),
						l(1, 10), l(2, 10), l(3, 10), l(12, 10), l(13, 10),
						l(0, 11), l(1, 11), l(2, 11), l(3, 11), l(4, 11),
						l(12, 11), l(13, 11), l(14, 11), l(0, 12), l(1, 12),
						l(2, 12), l(3, 12), l(4, 12), l(5, 12), l(6, 12),
						l(7, 12), l(8, 12), l(12, 12), l(13, 12), l(14, 12) });
	}

	// This was to make the arrays of new locations more readable
	private static Location l(int x, int y) {
		return new Location(x, y);
	}
}
