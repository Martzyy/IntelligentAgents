package ia;

public class Square {
	private SquareState squarestate;
	
	//Constructors
	public Square(SquareState squarestate) {
		this.squarestate = squarestate;
		return;
	}
	
	public Square() {
		this.squarestate = SquareState.UNINITIALIZED;
		return;
	}
	
	//CRUD Methods
	
	public double getReward(RewardModel rm) throws Exception {
		return rm.getValue(this.squarestate);
	}
	
	public void setSquareState(SquareState squarestate) {
		this.squarestate = squarestate;
		return;
	}
	
	public SquareState getSquareState() {
		return this.squarestate;
	}
	
	public boolean isMoveValid() {
		if (squarestate.isValid(this.squarestate)) {
			return true;
		} else {
			return false;
		}
	}
}
