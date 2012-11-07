package simulation;

public class Square {
	
	private SquareType type;
	private SquareContents contents;
	
	public Square(SquareType gType, SquareContents gContents) {
		type = gType;
		contents = gContents;
	}

	public SquareType getType() {
		return type;
	}

	public void setType(SquareType gType) {
		type = gType;
	}

	public SquareContents getContents() {
		return contents;
	}

	public void setContents(SquareContents gContents) {
		contents = gContents;
	}

}
