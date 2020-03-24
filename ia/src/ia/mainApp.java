package ia;
import javax.swing.*;
import java.awt.*;
import java.lang.Exception;

public class mainApp {
	public static void main(String args[]) throws Exception {
		Environment env = new Environment(6,6);
		String[][] envArray = {	{"G","B","G","W","W","G"},
								{"W","O","W","G","B","O"},
								{"W","W","O","W","G","W"},
								{"W","W","W","O","W","G"},
								{"W","B","B","B","O","W"},
								{"W","W","W","W","W","W"},
		};
		Vector2D defaultmove = new Vector2D(0,0);
		updateEnvironment(envArray,env);
		TransitionModel tm = new TransitionModel();
		new TransitionHandler(tm,defaultmove);
		RewardModel rm = new RewardModel();
		State[][] states = generateStates(env);
		Policy p = new Policy(states,tm,rm);
		valueIteration(0.99,0.000000000000000001,p);
		printRewards(env, rm);
		printUtilities(states);
	}
	
	public static State[][] generateStates(Environment env) throws Exception{
		int x = env.getNetwork()[0].length;
		int y = env.getNetwork().length;
		if (x < 1 ||y  < 1) {
			return null;
		} else {
			State[][] state = new State[x][y];
			for (int a = 0; a < x; a++) {
				for (int b = 0; b < y; b++) {
					Vector2D vec = new Vector2D(a,b);
					State st = new State(null,env,vec,0.0);
					st.generateActions();
					state[a][b] = st;
				}
			}
			return state;
		}
	}
	
	public static void valueIteration(double discount, double error, Policy p) throws Exception{
		while (true) {
			State[][]sta = p.getStates();
			RewardModel rm = p.getRewardModel();
			TransitionModel tm = p.getTransitionModel();
			State[][] s = sta.clone();
			int x = s[0].length;
			int y = s.length;
			double delta = 0;
			double internaldelta = 0;
			for (int a = 0; a < x; a++) {
				for (int b = 0; b < y; b++) {
					if (sta[b][a].isRewardable()){
						Action[] acts = sta[b][a].getActions();
						double reward = sta[b][a].getReward(rm);
						double expectimax = Double.NEGATIVE_INFINITY;
						for (int c = 0; c < acts.length; c++) {
							//System.out.println(c);
							double intravalue = discount*TransitionHandler.expectedUtility(sta[b][a], acts[c], p);
							if (intravalue > expectimax){
								expectimax = intravalue;
							}
						}
						double iutility = reward + expectimax;
						internaldelta = Math.abs(iutility - sta[b][a].getUtility());
						s[b][a].setUtility(iutility);
						if (internaldelta > delta){
							delta = internaldelta;
						}
					}
				}
			}
			p.setStates(s);
			if (delta < error*(1-discount)/discount) {
				break;
			}
		}
		return;
	}
	
	public static void printUtilities(State[][] state) {
		int x = state[0].length;
		int y = state.length;
		for (int a = 0; a < x; a++) {
			for (int b =0; b < y; b++) {
				System.out.println("Square : ("+a+","+b+") Utility : "+state[b][a].getUtility());
			}
		}
	}
	
	public static void printRewards(Environment env, RewardModel rm) throws Exception {
		Square[][] network = env.getNetwork();
		int x = network[0].length;
		int y = network.length;
		for (int a = 0; a < x; a++) {
			for (int b =0; b < y; b++) {
				if (network[b][a].isMoveValid()){
					System.out.println("Square : ("+a+","+b+") Reward : "+network[b][a].getReward(rm));
				} else {
					System.out.println("Square : ("+a+","+b+") Reward : None!");
				}
			}
		}
	}
	
	public static void printActions(Policy p) {
		State[][] s = p.getStates();
		int x = s[0].length;
		int y = s.length;
		for (int a = 0; a < x; a++) {
			for (int b = 0; b < y; b++) {
				System.out.println("Square : ("+a+","+b+")");
				Action[] act = s[b][a].getActions();
				for (int c = 0; c < act.length; c++) {
					Vector2D vec = act[c].getVector2D();
					System.out.println("Vector : ("+vec.x()+","+vec.y()+")");
				}
			}
		}
		return;
	}
	
	public static void updateEnvironment(String[][] envArray, Environment env) {
		int x = envArray[0].length;
		int y = envArray.length;
		for(int a = 0; a < x; a++) {
			for(int b = 0; b < y; b++) {
				Square sq = env.getSquare(a, b);
				if (envArray[b][a] == "W") {
					sq.setSquareState(SquareState.WHITE);
				} else if (envArray[b][a] == "B") {
					sq.setSquareState(SquareState.WALL);
				} else if (envArray[b][a] == "G") {
					sq.setSquareState(SquareState.GREEN);
				} else if (envArray[b][a] == "O") {
					sq.setSquareState(SquareState.ORANGE);
				} else {
					sq.setSquareState(SquareState.UNINITIALIZED);
				}	
				env.setSquare(sq, a, b);
			}
		}
	}
	
	public State findState(Vector2D vec, State[][] s) {
		int x = s[0].length;
		int y = s.length;
		for (int a = 0; a < x; a++) {
			for (int b = 0; b < y; b++) {
				if (s[b][a].getVector2D().equals(vec)) {
					return s[b][a];
				}
			}
		}
		return null;
	}
}
