/*
 * Created by Eliran Suisa & Eliran Hasin 17_02_18
 */
package AlgPACK;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class BestFirstSearch<STATE_TYPE> extends CoSearcher<STATE_TYPE> {

	@Override
	public ArrayList<State<STATE_TYPE>> search(Searchable<STATE_TYPE> searchable) {
		this.openList= new PriorityQueue<State<STATE_TYPE>>();
		this.addToOpenList((searchable.getInitialState()));
		while (!openList.isEmpty())
		{
			State<STATE_TYPE> n = this.popOpenList();
			//System.out.println(n.getCost());
			hs.add(n);
			if (searchable.isGoalState(n))
			{
				
			//	System.out.println("overed : " + this.getNumOfNodesEvaluated() + "evaluated nodes!");
				return backTrace(searchable.getInitialState(), n);
			}
			ArrayList<State<STATE_TYPE>> posibileStates = searchable.getAllPossibileStates(n);
			for (State<STATE_TYPE> state : posibileStates)
			{
				STATE_TYPE ar = state.getState();

				if (!hs.contains(state) && !(openList.contains(state))) {
					if (state!=searchable.getInitialState())
					{
						state.setCameFrom(n);
						addToOpenList(state);
					}
				}
			}
		}
		return null;
	}



}
