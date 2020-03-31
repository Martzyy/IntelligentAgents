package ia;

public enum SquareState {
	WALL,WHITE,GREEN,ORANGE,UNINITIALIZED;
	public boolean isValid(SquareState sq) {
		if (sq == SquareState.UNINITIALIZED || sq == SquareState.WALL) {
			return false;
		} else {
			return true;
		}
	}
}
