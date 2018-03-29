/*
 * Created by Eliran Suisa & Eliran Hasin 17_02_18
 */
package CliectHandlerPACK;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

import AlgPACK.BFS;
import AlgPACK.BestFirstSearch;
import AlgPACK.CostStateGrader;
import AlgPACK.DFS;
import AlgPACK.HillClimbing;
import AlgPACK.State;

public class MyClientHandler implements ClientHandler {
	//data members:
	private Solver solver;
	private CacheManager cachemanager;
	private ArrayList<String> ClientProblem = new ArrayList<>();
	private ArrayList<String> ClientSolution = new ArrayList<>();
	
	
	public MyClientHandler() {
		// TODO Auto-generated constructor stub
		cachemanager = new MyCacheManager();
		solver = new MySolver(new BestFirstSearch<char[][]>()); // IF_WE_NEED_TO_CHANGE_THE_PROBLEM_THEN_WE_NEED_TO_CHANGE_THE_"char[][]"_INPUT
	}
	
	
	@Override
	public void handleClient(InputStream inputClient, OutputStream outputClient) throws IOException {
		
//		System.out.println("Starting to handle client.");
		BufferedReader bf = new BufferedReader(new InputStreamReader(inputClient)); // CONVERT_INPUT_STREAMING_TO_TEXT
		PrintWriter pw = new PrintWriter(outputClient,true); // WRITING_OUTPUT_STREAMING_AS_TEXT
		String lineReader = new String();
		while (!(lineReader = bf.readLine()).equals("done")) // read text until the "done" string from client..
		{
			ClientProblem.add(lineReader); // insert client output to my arrayString.	
		}
		Integer x = ClientProblem.hashCode();
		if (cachemanager.isExist(ClientProblem)) // checking if the solve is exist in cacheManager.
		{
			ClientSolution = (ArrayList<String>) cachemanager.load(ClientProblem); // loading solution..
			for (int i=0; i<ClientSolution.size();i++)
				pw.println(ClientSolution.get(i)); // writing each string to user.
		}
		else
		{
			// solver solution.
			//System.out.println("Solution isn't exist in CM, trying to solve it..");
			ClientSolution = (ArrayList<String>) solver.solve(ClientProblem);
			ClientSolution.add("done");
			cachemanager.save(x,ClientSolution);
			for (int i=0; i<ClientSolution.size();i++)
			{
				pw.println(ClientSolution.get(i)); // writing each string to user.
			}
		}
		
		
		
	}

}
