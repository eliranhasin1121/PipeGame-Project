/*
 * Created by Eliran Suisa & Eliran Hasin 17_02_18
 */
package AlgPACK;

import java.util.ArrayList;

public abstract class CoSearchable<TYPE> implements Searchable<TYPE> // created for common dataMembers.
{
	State<TYPE> initialState;
	State<TYPE> goalState;
	ArrayList<State<TYPE>> allPossibileStates;
}
