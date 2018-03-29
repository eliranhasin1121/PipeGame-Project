/*
 * Created by Eliran Suisa & Eliran Hasin 17_02_18
 */
package AlgPACK;

import java.util.ArrayList;

public interface Searcher<STATE_TYPE>
{
	public ArrayList<State<STATE_TYPE>> search(Searchable<STATE_TYPE> searchable);
	public int getNumOfNodesEvaluated();
}
