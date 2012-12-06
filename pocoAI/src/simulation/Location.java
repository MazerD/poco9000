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
	
	public Location(Location prev, Action action) {
		x = prev.x + action.getDX();
		y = prev.y + action.getDY();
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
	
	@Override
	public boolean equals(Object other) {
		return (other instanceof Location) && ((Location) other).x == x
				&& ((Location) other).y == y;
	}
	
	@Override
	public int hashCode() {
		return 7 * x + 13 * y;
	}
	
	public Location clone() {
		return new Location(x, y);
	}

}