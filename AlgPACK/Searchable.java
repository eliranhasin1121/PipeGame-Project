/*
 * Created by Eliran Suisa & Eliran Hasin 17_02_18
 */
package AlgPACK;

import java.util.ArrayList;

public interface Searchable<TYPE> {
	public State<TYPE> getInitialState();
	public boolean isGoalState(State<TYPE> ifGoal); // created.
	public ArrayList<State<TYPE>> getAllPossibileStates(State<TYPE> s);
}


