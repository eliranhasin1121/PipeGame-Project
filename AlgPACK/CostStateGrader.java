package AlgPACK;

public class CostStateGrader<TYPE_STATE> implements StateGrader<TYPE_STATE>{

	@Override
	public int grade(State<TYPE_STATE> s) {
	int  grader=(int) ((-1)*s.getCost());
		return grader;
	}

}
