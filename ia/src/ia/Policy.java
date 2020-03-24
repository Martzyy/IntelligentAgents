package ia;

public class Policy{
	public Action[][] policy;
	public State[][] states;
	public TransitionModel tm;
	public RewardModel rm;
	
	//Constructor
	public Policy(State[][] states, Action[][] policy, TransitionModel tm, RewardModel rm) {
		this.tm = tm;
		this.states = states;
		this.policy = policy;
		this.rm = rm;
		return;
	}
	
	public Policy(State[][] states, TransitionModel tm, RewardModel rm) {
		this.tm = tm;
		this.states = states;
		this.policy = null;
		this.rm = rm;
		return;
	}
	
	//CRUD Methods
	public void setPolicy(Action [][] policy) {
		this.policy = policy;
		return;
	}
	
	public State[][] getStates(){
		return this.states;
	}
	
	public TransitionModel getTransitionModel() {
		return this.tm;
	}
	
	public RewardModel getRewardModel() {
		return this.rm;
	}
	
	public void setStates(State[][] sta) {
		this.states = sta;
		return;
	}
	
	//Essential Methods
	public State findState(Vector2D vec) {
		return this.states[vec.y()][vec.x()];
	}
}
