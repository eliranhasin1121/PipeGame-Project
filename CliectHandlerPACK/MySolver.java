/*
 * Created by Eliran Suisa & Eliran Hasin 17_02_18
 */
package CliectHandlerPACK;
// THIS_IS_SPECIFIC_SOLVER_ADAPTER_TO_PIPE_GAME_IF_YOU_NEED_TO_MAKE_NEW_ADAPTER_THEN_CREATE_NEW_CLASS_THAT_IMPLEMENTS_SOLVER!!!
import java.util.ArrayList;

import javax.xml.stream.events.StartDocument;

import AlgPACK.PipeGameSearchable;
import AlgPACK.Searcher;
import AlgPACK.State;

public class MySolver implements Solver<ArrayList<String>,ArrayList<String>> { // THE SOLVER ADAPTER !!
	
	//data members:
	Searcher<char[][]> searcher; // The BFS..DFS... the tool to search. this is an interface..
	
	public MySolver(Searcher searcher) { // the main will decide which alg to use..
		// initialize the way to search from main project.
		this.searcher = searcher;
	}
	
	
	@Override	
	public ArrayList<String> solve(ArrayList<String> problem) {
		// this function just redirect the issue to the algorithms.
		ArrayList<State<char[][]>> temp = new ArrayList<>();
		temp = searcher.search(new PipeGameSearchable(problem));
		return convertFromStateToString(temp);
	}
	public ArrayList<String> convertFromStateToString(ArrayList<State<char[][]>> theAnswer)
	{
		State<char[][]> start = theAnswer.get(theAnswer.size()-1);
		State<char[][]> goal = theAnswer.get(0);
		ArrayList<String> theStringAnswer = new ArrayList<>(); 
		for(int i=0;i<goal.getState().length;i++)
		{
			for(int j=0; j<goal.getState()[i].length; j++)
			{
				if (((start.getState()[i][j]=='-') && (goal.getState()[i][j]=='|')) || ((start.getState()[i][j]=='|') && (goal.getState()[i][j]=='-')) || ((start.getState()[i][j]=='L') && (goal.getState()[i][j]=='F')) || 
						   ((start.getState()[i][j]=='F') && (goal.getState()[i][j]=='7')) || ((start.getState()[i][j]=='7') && (goal.getState()[i][j]=='J')) || ((start.getState()[i][j]=='J') && (goal.getState()[i][j]=='L')))
				{
					theStringAnswer.add(i+","+j+",1");
				}
				
				else if (((start.getState()[i][j]=='L') && (goal.getState()[i][j]=='7')) || ((start.getState()[i][j]=='F') && (goal.getState()[i][j]=='J')) || ((start.getState()[i][j]=='J') && (goal.getState()[i][j]=='F')) || ((start.getState()[i][j]=='7') && (goal.getState()[i][j]=='L')) || 
						   ((start.getState()[i][j]=='F') && (goal.getState()[i][j]=='7')))
				{
					theStringAnswer.add(i+","+j+",2");
				}
				else if (((start.getState()[i][j]=='L') && (goal.getState()[i][j]=='J')) || ((start.getState()[i][j]=='F') && (goal.getState()[i][j]=='L')) || ((start.getState()[i][j]=='7') && (goal.getState()[i][j]=='F')) || 
						   ((start.getState()[i][j]=='J') && (goal.getState()[i][j]=='7')))
				{
					theStringAnswer.add(i+","+j+",3");
				}
			}
		}
		return theStringAnswer;	
	}
	

}
