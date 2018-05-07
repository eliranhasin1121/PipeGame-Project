/*
 * Created by Eliran Suisa & Eliran Hasin 17_02_18
 */
package AlgPACK;

import java.util.ArrayList;
import java.util.LinkedList;

public class BFS<STATE_TYPE> extends CoSearcher<STATE_TYPE> {

	@Override
	public ArrayList<State<STATE_TYPE>> search(Searchable<STATE_TYPE> searchable) {
		this.openList = new LinkedList<State<STATE_TYPE>>();
		openList.add(searchable.getInitialState());
		while (!openList.isEmpty())
		{
			State<STATE_TYPE> n = popOpenList();
			hs.add(n);
			if (searchable.isGoalState(n))
			{
			//	System.out.println("is goal!");
				return backTrace(searchable.getInitialState(), n);
			}
			ArrayList<State<STATE_TYPE>> successors = searchable.getAllPossibileStates(n);
			for (State<STATE_TYPE> s : successors)
			{
				if (!hs.contains(s) && !openList.contains(s))
				{
					s.setCameFrom(n);
					addToOpenList(s);
					hs.add(s);
				}
			}askdlmas
			}
		return null;
		
	}
	

	

}
