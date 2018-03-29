/*
 * Created by Eliran Suisa & Eliran Hasin 17_02_18
 */
package AlgPACK;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;

public abstract class CoSearcher<STATE_TYPE> implements Searcher<STATE_TYPE>
{
	protected Queue openList;
	private int evaluatedNodes;
	protected HashSet<State<STATE_TYPE>> hs = new HashSet<>(); 
	public CoSearcher()
	{
		this.openList = new PriorityQueue<State<STATE_TYPE>>();
		evaluatedNodes = 0;
	}
	protected State<STATE_TYPE> popOpenList()
	{
		evaluatedNodes++;
		return (State<STATE_TYPE>) openList.poll();
	}
	public void upNumOfNodes()
	{
		this.evaluatedNodes++;
	}
	@Override
	public int getNumOfNodesEvaluated()
	{
		return evaluatedNodes;	
	}
	@Override
	public abstract ArrayList<State<STATE_TYPE>> search(Searchable<STATE_TYPE> searchable);
	
	public ArrayList<State<STATE_TYPE>> backTrace(State<STATE_TYPE> init, State<STATE_TYPE> goal)
	{
		ArrayList<State<STATE_TYPE>> stp = new ArrayList<>();
		while (goal!=init)
		{
			stp.add(goal);
			goal = goal.getCameFrom();
		}
		stp.add(init);
		return stp;
	}
	public void addToOpenList(State<STATE_TYPE> s)
	{
		this.openList.add(s);
	}
	
}
