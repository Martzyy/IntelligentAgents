package ia;
import java.lang.Exception;

public class RewardModel {
	private SquareState[] keys;
	private double[] values;
	
	//Constructors
	public RewardModel(SquareState[] keys, double[] values) throws Exception{
		if (keys.length != values.length) {
			throw new Exception("Invalid key/values!");
		} else {
			this.keys = keys;
			this.values = values;
			return;
		}
	}
	
	public RewardModel() {
		this.keys = new SquareState[3];
		this.keys[0] = SquareState.WHITE;
		this.keys[1] = SquareState.GREEN;
		this.keys[2] = SquareState.ORANGE;
		this.values = new double[3];
		this.values[0] = -0.04;
		this.values[1] = 1.0;
		this.values[2] = -1.0;
		return;
	}
	
	//CRUD Methods
	public void setValue(int index, double value) {
		this.values[index] = value;
	}
	
	public double getValue(SquareState sqs) throws Exception{
		int index = -1;
		for (int a = 0; a < this.keys.length; a++) {
			if (this.keys[a] == sqs) {
				index = a;
				break;
			}
		}
		if (index == -1) {
			throw new Exception("SquareState not found!");
		} else {
			return this.values[index];
		}
	}
}
