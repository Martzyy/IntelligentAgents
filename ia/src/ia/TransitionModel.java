package ia;
import java.lang.Exception;

public class TransitionModel {
	private double front;
	private double left;
	private double right;
	public TransitionModel(double front, double left, double right) throws Exception {
		if (front + left + right != 1.0){
			throw new Exception("Invalid Transition Model!");
		} else{
			this.front = front;
			this.left = left;
			this.right = right;
			return;
		}
	}
	
	public TransitionModel() {
		this.front = 0.8;
		this.right = 0.1;
		this.left = 0.1;
		return;
	}
	
	public double left() {
		return this.left;
	}
	
	public double right() {
		return this.right;
	}
	
	public double front() {
		return this.front;
	}
	
	public void setModel(double front, double left, double right) throws Exception {
		if (front + left + right != 1.0){
			throw new Exception("Invalid Transition Model!");
		} else{
			this.front = front;
			this.left = left;
			this.right = right;
			return;
		}
	}
}
