package ia;

public class Action {
	private Environment env;
	private Square square;
	private Vector2D vec;
	
	//Constructors
	public Action(Square square, Vector2D vec, Environment env) {
		this.square = square;
		this.vec = vec;
		this.env = env;
		return;
	}
	
	public Action() {
		this.square = null;
		this.vec = null;
		this.env = null;
		return;
	}
	
	//CRUD Methods
	public void setSquare(Square square) {
		this.square = square;
		return;
	}
	
	public Square getSquare() {
		return this.square;
	}
	
	public void setVector2D(Vector2D vec) {
		this.vec = vec;
		return;
	}
	
	public Vector2D getVector2D() {
		return this.vec;
	}
	
	public void setEnvironment(Environment env) {
		this.env = env;
		return;
	}
	
	public Environment getEnvironment() {
		return this.env;
	}
	
	//Essential Methods
	public boolean isValid() {
		Vector2D sqvec = this.env.searchSquare(this.square);
		Vector2D nxtvec = sqvec.add(this.vec);
		Square sq = this.env.searchVector2D(nxtvec);
		if (sq == null||sq.getSquareState() == SquareState.UNINITIALIZED||sq.getSquareState()==SquareState.WALL) {
			return false;
		} else {
			return true;
		}
	}
}
