/*
 * Created by Eliran Suisa & Eliran Hasin 17_02_18
 */
package CliectHandlerPACK;

public interface Solver<TYPE_PROBLEM,TYPE_SOLUTION> {
	TYPE_SOLUTION solve(TYPE_PROBLEM problem);
}
