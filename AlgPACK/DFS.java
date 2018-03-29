/*
 * Created by Eliran Suisa & Eliran Hasin 17_02_18
 */
package AlgPACK;

import java.util.ArrayList;
import java.util.Stack;

public class DFS<STATE_TYPE>  extends CoSearcher<STATE_TYPE> {

	@Override
	public ArrayList<State<STATE_TYPE>> search(Searchable<STATE_TYPE> searchable) {
			// TODO Auto-generated method stub
			Stack<State<STATE_TYPE>> myStack = new Stack<>();
			myStack.push(searchable.getInitialState());
			while (!myStack.isEmpty())
			{
				State<STATE_TYPE> n = myStack.pop();
				this.upNumOfNodes();
				//COLOR??
				if (searchable.isGoalState(n))
				{
					//System.out.println("this is goal!");
					return backTrace(searchable.getInitialState(), n);
				}
				ArrayList<State<STATE_TYPE>> posibileStates = searchable.getAllPossibileStates(n);
				for (State<STATE_TYPE> s : posibileStates)
				{
					if (!hs.contains(s) && (!myStack.contains(s)))
					{
					s.setCameFrom(n);
					hs.add(s);
					myStack.push(s);
					}
				}
			}
			return null;
		}
	}
