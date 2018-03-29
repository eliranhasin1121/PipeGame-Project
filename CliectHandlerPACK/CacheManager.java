/*
 * Created by Eliran Suisa & Eliran Hasin 17_02_18
 */
package CliectHandlerPACK;

public interface CacheManager<TYPE> {
	public void save(Integer ProblemID,TYPE dataToSave);
	TYPE load(TYPE theSolution);
	boolean isExist(TYPE theSolution);
}
