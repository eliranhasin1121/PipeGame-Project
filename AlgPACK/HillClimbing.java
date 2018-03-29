
// Created by Eliran Suisa & Eliran Hasin 17_02_18

package AlgPACK;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class HillClimbing<TYPE_STATE> extends CoSearcher<TYPE_STATE> {
	
    private long timeToRun;
    private StateGrader<TYPE_STATE> grader;

	  public HillClimbing(StateGrader<TYPE_STATE> grader, long timeToRun) {
	        this.grader = grader;
	        this.timeToRun = timeToRun;
	    }
	  
	@Override
	public ArrayList<State<TYPE_STATE>> search(Searchable<TYPE_STATE> searchable) {
		
		 //Define the current state as an initial state
        State<TYPE_STATE> next = searchable.getInitialState();
        State<TYPE_STATE> temp = searchable.getInitialState();
        ArrayList<State<TYPE_STATE>> result = new  ArrayList<State<TYPE_STATE>>();
        
        long time0 = System.currentTimeMillis();


        //Loop until the goal state is achieved or no more operators can be applied on the current state:
        while (System.currentTimeMillis() - time0 < timeToRun) {
        	this.upNumOfNodes();
        	if (searchable.isGoalState(temp))
        	{
        		
        		//System.out.println("overed : " + this.getNumOfNodesEvaluated() + "evaluated nodes!");
        		return backTrace(searchable.getInitialState(), temp);
        	}
            List<State<TYPE_STATE>> neighbors = searchable.getAllPossibileStates(temp);
            if (Math.random() < 0.7) { // with a high probability
                // find the best one
                int grade = 0;
                for (State<TYPE_STATE> step : neighbors) {
                	step.setCameFrom(next);
                    int g = grader.grade(step);
                    if (g > grade) {
                        grade = g;
                        temp = step;
                    }
                }
            } else {
                temp = neighbors.get(new Random().nextInt(neighbors.size()));
                temp.setCameFrom(next);
            }
        }

        return result;

	}

   
}
