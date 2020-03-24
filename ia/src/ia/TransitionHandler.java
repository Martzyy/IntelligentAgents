package ia;

public class TransitionHandler {
	private static TransitionModel tm;
	private static Vector2D defaultmove;
	
	//Constructor
	public TransitionHandler(TransitionModel tm,Vector2D defaultmove) {
		TransitionHandler.tm = tm;
		TransitionHandler.defaultmove = defaultmove;
		return;
	}
	
	public static Vector2D[] getVectors(Vector2D vec) {
		Vector2D north = new Vector2D(-1,0);
		Vector2D east = new Vector2D(0,1);
		Vector2D south = new Vector2D(1,0);
		Vector2D west = new Vector2D(0,-1);
		if (vec.equals(north)) {
			return new Vector2D[] {north, east, west};
		} else if (vec.equals(east)) {
			return new Vector2D[] {east, north, south};
		} else if (vec.equals(south)) {
			return new Vector2D[] {south, east, west};
		} else if (vec.equals(west)) {
			return new Vector2D[] {west, south, north};
		} return null;
	}
	
	//Essential Methods
	public static double expectedUtility(State s, Action act,Policy p) {
		Action[] acts = s.getActions();
		int len = acts.length;
		Vector2D[] vectors = null;
		for (int a = 0; a < len; a++) {
			if(act.equals(acts[a])) {
				vectors = getVectors(acts[a].getVector2D());
			}
		}
		double[] probability = new double[] {tm.front(),tm.left(),tm.right()};
		double utility = 0;
		int lenv;
		if (vectors == null) {
			lenv = 0;
		} else {
			lenv = vectors.length;
		}
		for (int b = 0; b < lenv; b++) {
			for (int a = 0; a < len; a++) {
				if (acts[a].getVector2D().equals(vectors[b])) {
					//System.out.println("Square : ("+s.getVector2D().x()+","+s.getVector2D().y()+")");
					//System.out.println("Action Vector : ("+acts[a].getVector2D().x()+","+acts[a].getVector2D().y()+")");
					if (acts[a].isValid()) {
						utility += probability[b]*p.findState(s.getVector2D().add(vectors[b])).getUtility();
						//System.out.println("Action Valid. Utility : " + utility);
					} else {
						utility += probability[b]*p.findState(s.getVector2D().add(defaultmove)).getUtility();
						//System.out.println("Action Invalid. Utility : "+utility);
					}
				}
			}
		}
		return utility;
	}
}
