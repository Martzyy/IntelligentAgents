package ia;
import javax.swing.*;
import java.awt.*;
import java.lang.Exception;
import java.util.Random;

public class mainApp {
	public static void main(String args[]) throws Exception {
		Environment env = new Environment(6,6);
		double discount = 0.99;
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
		valueIteration(discount,0.1,p);
		printUtilities(states);
		plotPolicy(p, discount);
		printPolicy(p);
		
		Environment env2 = new Environment(6,6);
		double discount2 = 0.99;
		String[][] envArray2 = {	{"G","B","G","W","W","G"},
									{"W","O","W","G","B","O"},
									{"W","W","O","W","G","W"},
									{"W","W","W","O","W","G"},
									{"W","B","B","B","O","W"},
									{"W","W","W","W","W","W"},
		};
		Vector2D defaultmove2 = new Vector2D(0,0);
		updateEnvironment(envArray2,env2);
		TransitionModel tm2 = new TransitionModel();
		new TransitionHandler(tm2,defaultmove2);
		RewardModel rm2 = new RewardModel();
		State[][] states2 = generateStates(env2);
		Policy p2 = new Policy(states2,tm2,rm2);
		policyIteration(p2,discount2);
		printUtilities(states2);
		plotPolicy(p2, discount2);
		printPolicy(p2);
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
		int count = 0;
		while (true) {
			State[][]sta = p.getStates();
			RewardModel rm = p.getRewardModel();
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
			count = count + 1;
			if (delta < error*(1-discount)/discount) {
				break;
			}
		}
		System.out.println("Iterations : "+ count);
		return;
	}
	
	
	public static void policyIteration(Policy p, double discount) throws Exception {
		State[][] sta = p.getStates();
		RewardModel rm = p.getRewardModel();
		Random rand = new Random();
		int x = sta[0].length;
		int y = sta.length;
		Action[][] policy = new Action[y][x];
		Vector2D west = new Vector2D(-1,0);
		Vector2D south = new Vector2D(0,1);
		Vector2D east = new Vector2D(1,0);
		Vector2D north = new Vector2D(0,-1);
		for (int a = 0; a < x; a++) {
			for (int b = 0; b < y; b++) {
				policy[b][a] = new Action();
				if(sta[b][a].isRewardable()) {
					int randint = rand.nextInt(4);
					if (randint == 0) {
						policy[b][a].setVector2D(north);
					} else if (randint == 1) {
						policy[b][a].setVector2D(west);
					} else if (randint == 2) {
						policy[b][a].setVector2D(east);
					} else if (randint == 3) {
						policy[b][a].setVector2D(south);
					}
				} else {
					policy[b][a].setVector2D(new Vector2D(0,0));
				}
			}
		}
		p.setPolicy(policy);
		boolean changed = true;
		int count = 0;
		while(changed) {
			sta = p.getStates();
			State[][] s = sta.clone();
			changed = false;
			policyEvaluation(p, discount);
			for (int a = 0; a < x; a++) {
				for (int b = 0; b < y; b++) {
					if (sta[b][a].isRewardable()){
						Action[] acts = sta[b][a].getActions();
						double expectimax = sta[b][a].getEnvironment().getSquare(sta[b][a].getVector2D()).getReward(rm)+TransitionHandler.expectedUtility(sta[b][a], p.getPolicy()[b][a], p);
						for (int c = 0; c < acts.length; c++) {
							//System.out.println(c);
							double intravalue = sta[b][a].getEnvironment().getSquare(sta[b][a].getVector2D()).getReward(rm)+TransitionHandler.expectedUtility(sta[b][a], acts[c], p);
							if (intravalue > expectimax){
								policy[b][a] = acts[c];
								expectimax = intravalue;
								changed = true;
								s[b][a].setUtility(expectimax);
							}
						}
					} else {
						policy[b][a] = new Action();
						policy[b][a].setVector2D(new Vector2D(0,0));
					}
				} 
			}
			p.setStates(s);
			count++;
			System.out.println("Iteration : "+ count);
		}
		return;
	}
	
	public static void plotPolicy(Policy p, double discount) throws Exception {
		State[][] s = p.getStates();
		int x = s[0].length;
		int y = s.length;
		Action[][] policy = new Action[y][x];
		for (int a = 0; a < x; a++) {
			for (int b = 0; b < y; b++) {
				if (s[b][a].isRewardable()){
					Action[] acts = s[b][a].getActions();
					double expectimax = Double.NEGATIVE_INFINITY;
					for (int c = 0; c < acts.length; c++) {
						//System.out.println(c);
						double intravalue = TransitionHandler.expectedUtility(s[b][a], acts[c], p);
						if (intravalue > expectimax){
							policy[b][a] = acts[c];
							expectimax = intravalue;
						}
					}
				} else {
					policy[b][a] = new Action();
					policy[b][a].setVector2D(new Vector2D(0,0));
				}
			}
		}
		p.setPolicy(policy);
	}
	
	public static void policyEvaluation(Policy p, double discount) throws Exception{
		RewardModel rm = p.getRewardModel();
		State[][] s = p.getStates();
		int x = s[0].length;
		int y = s.length;
		Action[][] policy = p.getPolicy();
		for (int a = 0; a < x; a++) {
			for (int b = 0; b < y; b++) {
					if (s[b][a].isRewardable()) {
						s[b][a].setUtility(s[b][a].getReward(rm)+discount*TransitionHandler.expectedUtility(s[b][a], policy[b][a], p));
					}
				}
			}
		return;
	}
	
	
	public static void printPolicy(Policy p) {
		Action[][] policy = p.getPolicy();
		int x = policy[0].length;
		int y = policy.length;
		Vector2D west = new Vector2D(-1,0);
		Vector2D south = new Vector2D(0,1);
		Vector2D east = new Vector2D(1,0);
		Vector2D north = new Vector2D(0,-1);
		for (int a =0; a<x ; a++) {
			for (int b = 0; b < y ; b++) {
				if (policy[b][a].getVector2D().equals(north)) {
					System.out.print(" ^ ");
				} else if (policy[b][a].getVector2D().equals(east)) {
					System.out.print(" > ");
				} else if (policy[b][a].getVector2D().equals(south)) {
					System.out.print(" v ");
				} else if (policy[b][a].getVector2D().equals(west)) {
					System.out.print(" < ");
				} else {
					System.out.print(" - ");
				}
			}
			System.out.println();
		}
	}
	
	public static void printUtilities(State[][] state) {
		System.out.println("Reference utilities of states. in (row, col) format : ");
		int x = state[0].length;
		int y = state.length;
		for (int a = 0; a < x; a++) {
			for (int b =0; b < y; b++) {
				System.out.println("Square : ("+a+","+b+") Utility : "+state[b][a].getUtility());
			}
		}
	}
	
	public static void printRewards(Environment env, RewardModel rm) throws Exception {
		System.out.println("Reference rewards of states. in (row, col) format : ");
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
