package ia;

public class Environment {
	public Square[][] network;
	
	//Constructors
	public Environment(Square[][] network) {
		this.network = network;
		return;
	}
	
	public Environment() {
		this.network = null;
		return;
	}

	public Environment(int xpos, int ypos) {
		this.network = new Square[ypos][xpos];
		for (int a = 0; a < xpos; a++) {
			for (int b = 0; b < ypos; b++) {
				this.network[b][a] = new Square();
			}
		}
		return;
	}
	
	//CRUD Methods
	public void setSquare(Square square, int xpos, int ypos) {
		this.network[ypos][xpos] = square;
		return;
	}
	
	public Square getSquare(Vector2D vec) {
		return this.getSquare(vec.x(),vec.y());
	}
	public Square getSquare(int xpos, int ypos){
		return this.network[ypos][xpos];
	}
	
	public Square[][] getNetwork(){
		return this.network;
	}
	
	//Essential Methods
	public Vector2D searchSquare(Square sq){
		if (network.length == 0 || network[0].length == 0) {
			return null;
		}
		int width = network[0].length;
		int height = network.length;
		for (int a = 0; a < width; a++) {
			for (int b = 0; b < height; b++) {
				if (this.network[b][a] == sq) {
					return new Vector2D(a,b);
				}
			}
		}
		return null;
	}
	
	public Square searchVector2D(Vector2D vec){
		if (network.length == 0 || network[0].length == 0) {
			return null;
		}
		if (vec.y()<0 || vec.x()<0||vec.x()>this.network[0].length-1||vec.y()>this.network.length-1) {
			return null;
		}
		return this.network[vec.y()][vec.x()];
	}
}
