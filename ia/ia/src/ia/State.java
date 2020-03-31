package ia;
import java.lang.Exception;

public class State {
	private Action[] actions;
	private Environment env;
	private Vector2D vec;
	private double utility;
	
	//Constructors
	public State(Action[] actions, Environment env, Vector2D vec, double utility) {
		this.actions = actions;
		this.env = env;
		this.vec = vec;
		this.utility = utility;
		return;
	}
	
	public State() {
		this.actions = null;
		this.env = null;
		this.vec = null;
		this.utility = Double.NaN;
		return;
	}
	
	public void setActions(Action[] actions) {
		this.actions = actions;
		return;
	}
	
	public void addAction(Action action) throws Exception{
		int len;
		try {
			len = this.actions.length;
		} catch (Exception e){
			len = 0;
		}
		for (int a = 0; a < len; a++) {
			if (action == this.actions[a]) {
				throw new Exception("Already present!");
			}
		}
		Action[] newarr = new Action[len+1];
		for (int a = 0; a < len; a++) {
			newarr[a] = this.actions[a];
		}
		newarr[len] = action;
		this.actions = newarr;
		return;
	}
	
	public Action[] getActions() {
		return this.actions;
	}
	
	public void setEnvironment(Environment env) {
		this.env = env;
		return;
	}
	
	public Environment getEnvironment() {
		return this.env;
	}
	
	public void setVector2D(Vector2D vec) {
		this.vec = vec;
		return;
	}
	
	public Vector2D getVector2D() {
		return this.vec;
	}
	
	public void setUtility(double utility) {
		this.utility = utility;
		return;
	}
	
	public double getUtility() {
		return this.utility;
	}
	
	//Essential Methods
	public void generateActions() throws Exception{
		Vector2D[] vecs = new Vector2D[4];
		vecs[0] = new Vector2D(-1,0);
		vecs[1] = new Vector2D(0,1);
		vecs[2] = new Vector2D(1,0);
		vecs[3] = new Vector2D(0,-1);
		for (int a = 0; a < 4; a++) {
			this.addAction(new Action(this.env.getSquare(this.vec),vecs[a],env));
		}
		return;
	}
	
	public double getReward(RewardModel rm) throws Exception {
		return this.env.getSquare(this.vec).getReward(rm);
	}
	
	public boolean isRewardable() {
		return this.getEnvironment().getSquare(this.getVector2D()).isMoveValid();
	}
}
