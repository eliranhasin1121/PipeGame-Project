package AlgPACK;

public interface StateGrader<TYPE_STATE>{
	int grade(State<TYPE_STATE> s); // give a grade to a certain state - how close it is to the solution
}
