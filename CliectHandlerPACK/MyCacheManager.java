package CliectHandlerPACK;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
/*
 * Created by Eliran Suisa & Eliran Hasin 17_02_18
 */
import java.util.Map;

public class MyCacheManager implements CacheManager<ArrayList<String>> {
	
	//data members:
	Integer ProblemID = new Integer(0);
	Map<Integer,ArrayList<String>> hashONLINE = new HashMap<>();
	
	
	@Override
	public void save(Integer ProblemID, ArrayList<String> dataToSave) {
		int i;
		/* this function will apply if the solution isnt exist, we are going to solver and then inject the solution to our cachemanager..*/
		try {
//			System.out.println("MyCacheManager : trying to save solution to new file");
			this.ProblemID = ProblemID;
			File file = new File(ProblemID.toString()); // openning new file called :  "ProblemID"(the hashcode(unique..)).
			file.createNewFile();
			FileWriter fop = new FileWriter(file); // a way to write to our file.
			for (i=0;i<dataToSave.size()-1;i++)
			{
				fop.write(dataToSave.get(i));
				fop.write("\n");
				fop.flush(); // must flush after each writing..
			}
			fop.write(dataToSave.get(i));
			hashONLINE.put(ProblemID,dataToSave); // insert the new solution to our cache..
			fop.flush(); // maybe redundant?
			fop.close();
		}catch (Exception e) {
			// TODO: handle exception
//			System.out.println("MyCacheManager : something got wrong with save new solution..");
		}
	}

	
	@Override
	public ArrayList<String> load(ArrayList<String> theSolution) {
		// trying 2 ways to solve, first from cacheONLINE, if not exist in cache -> trying to load from file..
		if (hashONLINE.containsValue(theSolution)) // if exist in our cache online
			return hashONLINE.get(theSolution.hashCode());
		else // reading from file.
		{
			Integer ProbID = theSolution.hashCode();
			try {
			String lineReader = new String();
			ArrayList<String> ar = new ArrayList<>();
			File fileName = new File(ProbID.toString());
			BufferedReader read = new BufferedReader(new FileReader(fileName)); // tool to read from file (like from stream..decorator..)
			while ((lineReader = read.readLine()) != null)
			{
				ar.add(lineReader); // reading the solution from file..
			}
//			System.out.println("MyCacheManager : loading solution completed.");
			read.close();
			hashONLINE.put(ProbID, ar); // insert to hashcache online..
			return ar;
			}catch (Exception e) {
				// TODO: handle exception
//				System.out.println("MyCacheManager : error with reading file..");
			}
		}
		return null; 
	}

	
	@Override
	public boolean isExist(ArrayList<String> ClientProblem) {
		/* cheking 3 situations:
		 * 1) exist in hashONLINE
		 * 2) exist in file
		 * 3) ELSE : return false, then going to work with solver..
		 */
		ProblemID = ClientProblem.hashCode();
		if (hashONLINE.containsValue(ClientProblem)) // exist in hashONLINE
			return true;
		
		else if (Files.exists(Paths.get(ProblemID.toString()))) // exist in some file.
			return true;
		else
			return false;
	}

}
