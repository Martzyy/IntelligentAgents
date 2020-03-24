package ia;

public class Square {
	private String name;
	private SquareState squarestate;
	
	//Constructors
	public Square(String name, SquareState squarestate) {
		this.name = name;
		this.squarestate = squarestate;
		return;
	}
	
	public Square() {
		this.name = "";
		this.squarestate = SquareState.UNINITIALIZED;
		return;
	}
	
	//CRUD Methods
	public void setName(String name) {
		this.name = name;
		return;
	}
	
	public String getName() {
		return this.name;
	}
	
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
