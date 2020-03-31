package ia;

public class Vector2D {
	private int x;
	private int y;
	
	public Vector2D (int x, int y) {
		this.x = x;
		this.y = y;
		return;
	}
	
	public void setVector(int x, int y) {
		this.x = x;
		this.y = y;
		return;
	}
	
	public Vector2D add(Vector2D vector) {
		return new Vector2D(this.x+vector.x(),this.y+vector.y());
	}
	
	public Vector2D negate() {
		return new Vector2D(-this.x, -	this.y);
	}
	
	public int x() {
		return this.x;
	}
	
	public void x(int x) {
		this.x += x;
		return;
	}
	
	public int y() {
		return this.y;
	}
	
	public void y(int y) {
		this.y += y;
		return;
	}
	
	public boolean equals(Vector2D vec) {
		if (vec.x == this.x && vec.y == this.y) {
			return true;
		} else {
			return false;
		}
	}
}
