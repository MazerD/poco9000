package simulation;

/**
 * An object that encapsulates a location on the board.
 * 
 * @author Melissa
 */
public class Location {
	/** x coordinate */
	private int x;
	/** y coordinate */
	private int y;

	/**
	 * @param gX
	 *            x coordinate
	 * @param gY
	 *            y coordinate
	 */
	public Location(int gX, int gY) {
		x = gX;
		y = gY;
	}

	/**
	 * @return the x coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param gX
	 *            the new x coordinate
	 */
	public void setX(int gX) {
		x = gX;
	}

	/**
	 * @return the y coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param gX
	 *            the new y coordinate
	 */
	public void setY(int gY) {
		y = gY;
	}

}