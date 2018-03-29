/*
 * Created by Eliran Suisa & Eliran Hasin 17_02_18
 */
package AlgPACK;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

import javax.xml.transform.Templates;

public class PipeGameSearchable extends CoSearchable<char[][]>
{
	HashSet<State<char[][]>> ClosedGroup = new HashSet<>();
	HashSet<ArrayList<String>> hs = new HashSet<>();
	int[] XandY = new int[5];
	int boardSize = 0;
	int[] EndPoint = new int[2];
	int[] StartPoint = new int[2];
	public PipeGameSearchable(ArrayList<String> problem) // CONSTRUCTURE.
	{ 
		int i,j;
		this.initialState = new State<char[][]>(Convertor(problem));
		this.allPossibileStates = new ArrayList<State<char[][]>>();
		boolean StartAndEnd = false;
		boardSize = this.initialState.getState().length * this.initialState.getState()[0].length;
		
		for (i=0;i<initialState.getState().length;i++)
		{
			for (j=0;j<initialState.getState()[i].length; j++)
				if (initialState.getState()[i][j] == 's')
					{StartPoint[0] = i; StartPoint[1]= j; StartAndEnd = true; break;} // keep our start position (x,y).POINT.
			if (StartAndEnd == true)
				break;
		}
		StartAndEnd = false;
		
		for (i=0;i<initialState.getState().length;i++)
		{
			for (j=0;j<initialState.getState()[i].length; j++)
				if (initialState.getState()[i][j] == 'g')
					{EndPoint[0] = i; EndPoint[1] = j;StartAndEnd = true; break;} // keep our end position (x,y).POINT.
			if(StartAndEnd == true)
				break;
		}
	}
	
	@Override
	public State<char[][]> getInitialState() {return this.initialState;} // RETURN_THE_INIT_STATE (THIS_IS_THE_ORIGINAL_PROBLEM!)
	
	@Override
	public boolean isGoalState(State<char[][]> ifGoal) // FUNCTION_TO_CHECK_IF_CURRENT_STATE_ARE_"GOAL"
	{
		// TODO Auto-generated method stub
		int i,j;
		boolean there= false;
		boolean right=false,left=false,up=false,down=false; // init our answer to "FALSE"
		int sx = -1,sy=-1;
		for (i=0;i<ifGoal.getState().length;i++)
		{
			for (j=0;j<ifGoal.getState()[i].length; j++)
			{
				if (ifGoal.getState()[i][j] == 's')
					{sx = i; sy = j;there=true ; break;}
			}
			if (there)
				break;
		}
		/*
		 * Sending our player to travel on the board.. if the start position can travel, then flow..
		 */
		if ( (isValid(ifGoal.getState(), sx, sy+1)) && (CheckMyRight(ifGoal.getState()[sx][sy+1])) )
			right = flow(ifGoal.getState(), sx, sy+1);
		if (right==true)
		{	
//			System.out.println("the goal is : " + this.charToArray(ifGoal.getState()));
			return true;
		}
		if ( (isValid(ifGoal.getState(), sx, sy-1)) && (CheckMyLeft(ifGoal.getState()[sx][sy-1])) )
			left = flow(ifGoal.getState(), sx, sy-1);
		if (left==true)
		{	
//			System.out.println("the goal is : " + this.charToArray(ifGoal.getState()));
			return true;
		}
		if ( (isValid(ifGoal.getState(), sx-1, sy)) && (CheckMyUp(ifGoal.getState()[sx-1][sy])) )	
			up = flow(ifGoal.getState(), sx-1, sy);
		if (up==true)
		{	
//			System.out.println("the goal is : " + this.charToArray(ifGoal.getState()));
			return true;
		}
		if ( (isValid(ifGoal.getState(), sx+1, sy)) && (CheckMyDown(ifGoal.getState()[sx+1][sy])) )
			down = flow(ifGoal.getState(), sx+1, sy);
		if (down==true)
		{	
//			System.out.println("the goal is : " + this.charToArray(ifGoal.getState()));
			return true;
		}
		return false;
	} 	
	
	@Override
	public ArrayList<State<char[][]>> getAllPossibileStates(State<char[][]> s) // ALL_POSIBILE_STATES_WITH_OPTIMIZATIONS. 
	{
		setAllPossibileStates(s);
		return this.allPossibileStates;
		// TODO Auto-generated method stub
		
	}
	
	public void setAllPossibileStates(State<char[][]> currentState) // SET_ALL_POS_STATES_WITH_"stateCreator"_AND_"stackPosition"
	{
		/*
		 * This function will make all posibile states for this current State<char[][]>
		 * Conditions and Flow-Job:
		 * BASICS:
		 * 1)	if the box is empty, then do nothing.
		 * 2)	if the box contain "|" or "-" then make only 1 rotate.
		 * 3)	if the box contain something else, then rotate 3 times(each time is a new possible state).
		 * OPTIMIZATIONS:
		 * 1)	each round try to "travel" on your board, then manage your "cost"  for each state
		 * 2)	at the end, sort the "ALL_POSIBILE_STATES by cost, then send it to "SEARCHER"
		 */
		char[][][] temp = new char[100][][];
		int i=0,j=0;
		int t=0;
		double sumAfterRotate= 0;
		int startx=-1,starty=-1,endx=-1,endy=-1;
		boolean StartAndEnd=false;
		startx = this.StartPoint[0];
		starty = this.StartPoint[1];
		endx = this.EndPoint[0];
		endy = this.EndPoint[1];
		int[] stuckPosition= new int[5];
		/*
		 * STARTING THE ALGO!
		 */
		// all of those functions will start to travel from s
		if ( (isValid(currentState.getState(), startx, starty+1)) && (CheckMyRight(currentState.getState()[startx][starty+1])) )
		{
			stuckPosition = getStuckPoint(currentState.getState(), startx, starty+1, endx, endy);
			statesCreator(currentState.getState(), stuckPosition[0], stuckPosition[1], stuckPosition[2], stuckPosition[3]);
		}
		if ( (isValid(currentState.getState(), startx, starty-1)) && (CheckMyLeft(currentState.getState()[startx][starty-1])) )
		{
			stuckPosition = getStuckPoint(currentState.getState(), startx, starty-1, endx, endy);
			statesCreator(currentState.getState(), stuckPosition[0], stuckPosition[1], stuckPosition[2], stuckPosition[3]);
		}
		if ( (isValid(currentState.getState(), startx-1, starty)) && (CheckMyUp(currentState.getState()[startx-1][starty])) )
		{
			stuckPosition = getStuckPoint(currentState.getState(), startx-1, starty, endx, endy);
			statesCreator(currentState.getState(), stuckPosition[0], stuckPosition[1], stuckPosition[2], stuckPosition[3]);
		}
		if ( (isValid(currentState.getState(), startx+1, starty)) && (CheckMyDown(currentState.getState()[startx+1][starty])) )
		{
			stuckPosition = getStuckPoint(currentState.getState(), startx+1, starty, endx, endy);
			statesCreator(currentState.getState(), stuckPosition[0], stuckPosition[1], stuckPosition[2], stuckPosition[3]);
		}
		// This command will find all the ways to start position if we can't travel from start.
		statesCreator(currentState.getState(), StartPoint[0], StartPoint[1], -1, -1);
		
		

	}
	
	public void statesCreator(char [][] board,int stuckx,int stucky,int camex,int camey) // STATES_CREATOR_FOR_EACH_STATE
	{
		int k=0;
		char[][][] temp = new char[100][][];
		if (board[stuckx][stucky] == 's')
		{
			if ( (isValid(board,stuckx + 1,stucky)) && board[stuckx+1][stucky] != ' ') // if i can move down.
			{
				if (board[stuckx+1][stucky] == '-')
				{
					temp[k] = cloner(board);
					temp[k][stuckx+1][stucky] = '|';
					State<char[][]> tempState0 = new State<char[][]>(temp[k]);
					tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp0 = charToArray(tempState0.getState());
					if (!hs.contains(temp0))
					{
					hs.add(temp0);
					this.allPossibileStates.add(tempState0);
					}
					k++;
				}
				else if (board[stuckx+1][stucky] == '|')
				{
					if (false)
						System.out.println("skip this piece, you are not stuck.");
				}
				else
				{
					temp[k] = cloner(board);
					temp[k][stuckx+1][stucky] = 'L';
					State<char[][]> tempState0 = new State<char[][]>(temp[k]);
					tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp0 = charToArray(tempState0.getState());
					if (!hs.contains(temp0))
					{
					hs.add(temp0);
					this.allPossibileStates.add(tempState0);
					}
					k++;
					temp[k] = cloner(board);
					temp[k][stuckx+1][stucky] = 'J';
					State<char[][]> tempState1 = new State<char[][]>(temp[k]);
					tempState1.setCost(LengthAndDistance(tempState1, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp1 = charToArray(tempState1.getState());
					if (!hs.contains(temp1))
					{
					hs.add(temp1);
					this.allPossibileStates.add(tempState1);
					}
					k++;
				}
					
			}
			if ( (isValid(board,stuckx - 1,stucky)) && board[stuckx-1][stucky] != ' ') // if i can move up.
			{
				if (board[stuckx-1][stucky] == '-')
				{
					temp[k] = cloner(board);
					temp[k][stuckx-1][stucky] = '|';
					State<char[][]> tempState0 = new State<char[][]>(temp[k]);
					tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp0 = charToArray(tempState0.getState());
					if (!hs.contains(temp0))
					{
					hs.add(temp0);
					this.allPossibileStates.add(tempState0);
					}
					k++;
				}
				else if (board[stuckx-1][stucky] == '|')
				{
					if (false)
						System.out.println("skip this piece, you are not stuck.");
				}
				else
				{
					temp[k] = cloner(board);
					temp[k][stuckx-1][stucky] = '7';
					State<char[][]> tempState0 = new State<char[][]>(temp[k]);
					tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp0 = charToArray(tempState0.getState());
					if (!hs.contains(temp0))
					{
					hs.add(temp0);
					this.allPossibileStates.add(tempState0);
					}
					k++;
					temp[k] = cloner(board);
					temp[k][stuckx-1][stucky] = 'F';
					State<char[][]> tempState1 = new State<char[][]>(temp[k]);
					tempState1.setCost(LengthAndDistance(tempState1, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp1 = charToArray(tempState1.getState());
					if (!hs.contains(temp1))
					{
					hs.add(temp1);
					this.allPossibileStates.add(tempState1);
					}
					k++;
				}
					
			}
			if ( (isValid(board,stuckx,stucky+1)) && board[stuckx][stucky+1] != ' ') // if i can move right.
			{
				if (board[stuckx][stucky+1] == '|')
				{
					temp[k] = cloner(board);
					temp[k][stuckx][stucky+1] = '-';
					State<char[][]> tempState0 = new State<char[][]>(temp[k]);
					tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp0 = charToArray(tempState0.getState());
					if (!hs.contains(temp0))
					{
					hs.add(temp0);
					this.allPossibileStates.add(tempState0);
					}
					k++;
				}
				else if (board[stuckx][stucky+1] == '-')
				{
					if (false)
						System.out.println("skip this piece, you are not stuck.");
				}
				else
				{
					temp[k] = cloner(board);
					temp[k][stuckx][stucky+1] = 'J';
					State<char[][]> tempState0 = new State<char[][]>(temp[k]);
					tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp0 = charToArray(tempState0.getState());
					if (!hs.contains(temp0))
					{
					hs.add(temp0);
					this.allPossibileStates.add(tempState0);
					}
					k++;
					temp[k] = cloner(board);
					temp[k][stuckx][stucky+1] = '7';
					State<char[][]> tempState1 = new State<char[][]>(temp[k]);
					tempState1.setCost(LengthAndDistance(tempState1, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp1 = charToArray(tempState1.getState());
					if (!hs.contains(temp1))
					{
					hs.add(temp1);
					this.allPossibileStates.add(tempState1);
					}
					k++;
				}
					
			}
			if ( (isValid(board,stuckx,stucky-1)) && board[stuckx][stucky-1] != ' ') // if i can move left.
			{
				if (board[stuckx][stucky-1] == '|')
				{
					temp[k] = cloner(board);
					temp[k][stuckx][stucky-1] = '-';
					State<char[][]> tempState0 = new State<char[][]>(temp[k]);
					tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp0 = charToArray(tempState0.getState());
					if (!hs.contains(temp0))
					{
					hs.add(temp0);
					this.allPossibileStates.add(tempState0);
					}
					k++;
				}
				else if (board[stuckx][stucky-1] == '-')
				{
					if (false)
						System.out.println("skip this piece, you are not stuck.");
				}
				else
				{
					temp[k] = cloner(board);
					temp[k][stuckx][stucky-1] = 'L';
					State<char[][]> tempState0 = new State<char[][]>(temp[k]);
					tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp0 = charToArray(tempState0.getState());
					if (!hs.contains(temp0))
					{
					hs.add(temp0);
					this.allPossibileStates.add(tempState0);
					}
					k++;
					temp[k] = cloner(board);
					temp[k][stuckx][stucky-1] = 'F';
					State<char[][]> tempState1 = new State<char[][]>(temp[k]);
					tempState1.setCost(LengthAndDistance(tempState1, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp1 = charToArray(tempState1.getState());
					if (!hs.contains(temp1))
					{
					hs.add(temp1);
					this.allPossibileStates.add(tempState1);
					}
					k++;
				}
					
			}
					
		}
		if (board[stuckx][stucky] == '|')// then rotate only my partners.
		{
			if ( (isValid(board,stuckx + 1,stucky)) && board[stuckx+1][stucky] != ' ' && (camex != stuckx + 1)) // | came from up to down
			{
				if (board[stuckx+1][stucky] == '-')
				{
					temp[k] = cloner(board);
					temp[k][stuckx+1][stucky] = '|';
					State<char[][]> tempState0 = new State<char[][]>(temp[k]);
					tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp0 = charToArray(tempState0.getState());
					if (!hs.contains(temp0))
					{
					hs.add(temp0);
					this.allPossibileStates.add(tempState0);
					}
					k++;
				}
				else if(board[stuckx+1][stucky] == '7' || board[stuckx+1][stucky] == 'F')
				{
					{
						temp[k] = cloner(board);
						temp[k][stuckx+1][stucky] = 'L';
						State<char[][]> tempState1 = new State<char[][]>(temp[k]);
						tempState1.setCost(LengthAndDistance(tempState1, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp1 = charToArray(tempState1.getState());
						if (!hs.contains(temp1))
						{
						hs.add(temp1);
						this.allPossibileStates.add(tempState1);
						}
						k++;
						temp[k] = cloner(board);
						temp[k][stuckx+1][stucky] = 'J';
						State<char[][]> tempState0 = new State<char[][]>(temp[k]);
						tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp0 = charToArray(tempState0.getState());
						if (!hs.contains(temp0))
						{
						hs.add(temp0);
						this.allPossibileStates.add(tempState0);
						}
						k++;
					}
				}
			}
			else if ( (isValid(board,stuckx - 1,stucky)) && board[stuckx-1][stucky] != ' ' && (camex != stuckx - 1)) // | came from down to up
			{
				if (board[stuckx-1][stucky] == '-')
				{
					temp[k] = cloner(board);
					temp[k][stuckx-1][stucky] = '|';
					State<char[][]> tempState0 = new State<char[][]>(temp[k]);
					tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp0 = charToArray(tempState0.getState());
					if (!hs.contains(temp0))
					{
					hs.add(temp0);
					this.allPossibileStates.add(tempState0);
					}
					k++;
				}
				else if(board[stuckx-1][stucky] == 'L' || board[stuckx-1][stucky] == 'J')
				{
					{
						temp[k] = cloner(board);
						temp[k][stuckx-1][stucky] = 'F';
						State<char[][]> tempState1 = new State<char[][]>(temp[k]);
						tempState1.setCost(LengthAndDistance(tempState1, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp1 = charToArray(tempState1.getState());
						if (!hs.contains(temp1))
						{
						hs.add(temp1);
						this.allPossibileStates.add(tempState1);
						}
						k++;
						temp[k] = cloner(board);
						temp[k][stuckx-1][stucky] = '7';
						State<char[][]> tempState0 = new State<char[][]>(temp[k]);
						tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp0 = charToArray(tempState0.getState());
						if (!hs.contains(temp0))
						{
						hs.add(temp0);
						this.allPossibileStates.add(tempState0);
						}
						k++;
					}
				}
			}
		}
		if (board[stuckx][stucky] == '-')// then rotate only my partners.
		{
			if ( (isValid(board,stuckx,stucky+1)) && board[stuckx][stucky+1] != ' ' && (camey != stucky + 1)) // - came from left to right
			{
				if (board[stuckx][stucky+1] == '|')
				{
					temp[k] = cloner(board);
					temp[k][stuckx][stucky+1] = '-';
					State<char[][]> tempState0 = new State<char[][]>(temp[k]);
					tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp0 = charToArray(tempState0.getState());
					if (!hs.contains(temp0))
					{
					hs.add(temp0);
					this.allPossibileStates.add(tempState0);
					}
					k++;
				}
				else if(board[stuckx][stucky+1] == 'F' || board[stuckx][stucky+1] == 'L')
				{
					{
						temp[k] = cloner(board);
						temp[k][stuckx][stucky+1] = '7';
						State<char[][]> tempState1 = new State<char[][]>(temp[k]);
						tempState1.setCost(LengthAndDistance(tempState1, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));

						ArrayList<String> temp1 = charToArray(tempState1.getState());
						if (!hs.contains(temp1))
						{
						hs.add(temp1);
						this.allPossibileStates.add(tempState1);
						}
						k++;
						temp[k] = cloner(board);
						temp[k][stuckx][stucky+1] = 'J';
						State<char[][]> tempState0 = new State<char[][]>(temp[k]);
						tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp0 = charToArray(tempState0.getState());
						if (!hs.contains(temp0))
						{
						hs.add(temp0);
						this.allPossibileStates.add(tempState0);
						}
						k++;
					}
				}
			}
			else if ( (isValid(board,stuckx,stucky-1)) && board[stuckx][stucky-1] != ' ' && (camey != stucky-1)) // - came from right to left
			{
				if (board[stuckx][stucky-1] == '|')
				{
					temp[k] = cloner(board);
					temp[k][stuckx][stucky-1] = '-';
					State<char[][]> tempState0 = new State<char[][]>(temp[k]);
					tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp0 = charToArray(tempState0.getState());
					if (!hs.contains(temp0))
					{
					hs.add(temp0);
					this.allPossibileStates.add(tempState0);
					}
					k++;
				}
				else if(board[stuckx][stucky-1] == '7' || board[stuckx][stucky-1] == 'J')
				{
					{
						temp[k] = cloner(board);
						temp[k][stuckx][stucky-1] = 'F';
						State<char[][]> tempState1 = new State<char[][]>(temp[k]);
						tempState1.setCost(LengthAndDistance(tempState1, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp1 = charToArray(tempState1.getState());
						if (!hs.contains(temp1))
						{
						hs.add(temp1);
						this.allPossibileStates.add(tempState1);
						}
						k++;
						temp[k] = cloner(board);
						temp[k][stuckx][stucky-1] = 'L';
						State<char[][]> tempState0 = new State<char[][]>(temp[k]);
						tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp0 = charToArray(tempState0.getState());
						if (!hs.contains(temp0))
						{
						hs.add(temp0);
						this.allPossibileStates.add(tempState0);
						}
						k++;
					}
				}
			}
		}
		if (board[stuckx][stucky] == '7') // then i stucked from left or from down.
		{
			if ((camey == stucky -1) && (isValid(board,stuckx,stucky-1))) // IF I CAME FROM LEFT
			{
				temp[k] = cloner(board);
				temp[k][stuckx][stucky] = 'J';
				State<char[][]> tempState0 = new State<char[][]>(temp[k]);
				tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
				ArrayList<String> temp0 = charToArray(tempState0.getState());
				if (!hs.contains(temp0))
				{
				hs.add(temp0);
				this.allPossibileStates.add(tempState0);
				}
				k++;
			}
			if ((camex == stuckx + 1) && (isValid(board,stuckx+1,stuckx))) // IF I CAME FROM DOWN
			{
				temp[k] = cloner(board);
				temp[k][stuckx][stucky] = 'F';
				State<char[][]> tempState0 = new State<char[][]>(temp[k]);
				tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
				ArrayList<String> temp0 = charToArray(tempState0.getState());
				if (!hs.contains(temp0))
				{
				hs.add(temp0);
				this.allPossibileStates.add(tempState0);
				}
				k++;
			}
			if ( (isValid(board,stuckx,stucky-1)) && board[stuckx][stucky-1] != ' ' && (camey != stucky - 1)) // trying to change the left side.
			{
				if (board[stuckx][stucky-1] == '|')
				{
					temp[k] = cloner(board);
					temp[k][stuckx][stucky-1] = '-';
					State<char[][]> tempState1 = new State<char[][]>(temp[k]);
					tempState1.setCost(LengthAndDistance(tempState1, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp1 = charToArray(tempState1.getState());
					if (!hs.contains(temp1))
					{
					hs.add(temp1);
					this.allPossibileStates.add(tempState1);
					}
					k++;
				}
				else if(board[stuckx][stucky-1] == '7' || board[stuckx][stucky-1] == 'J')
				{
					{
						temp[k] = cloner(board);
						temp[k][stuckx][stucky-1] = 'F';
						State<char[][]> tempState2 = new State<char[][]>(temp[k]);
						tempState2.setCost(LengthAndDistance(tempState2, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp2 = charToArray(tempState2.getState());
						if (!hs.contains(temp2))
						{
						hs.add(temp2);
						this.allPossibileStates.add(tempState2);
						}
						k++;
						temp[k] = cloner(board);
						temp[k][stuckx][stucky-1] = 'L';
						State<char[][]> tempState3 = new State<char[][]>(temp[k]);
						tempState3.setCost(LengthAndDistance(tempState3, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp3 = charToArray(tempState3.getState());
						if (!hs.contains(temp3))
						{
						hs.add(temp3);
						this.allPossibileStates.add(tempState3);
						}
						k++;
					}
				}
			}
			else if ( (isValid(board,stuckx+1,stucky)) && board[stuckx+1][stucky] != ' ' && (camex != stuckx+1)) // - trying to change my down side
			{
				if (board[stuckx+1][stucky] == '-')
				{
					temp[k] = cloner(board);
					temp[k][stuckx+1][stucky] = '|';
					State<char[][]> tempState1 = new State<char[][]>(temp[k]);
					tempState1.setCost(LengthAndDistance(tempState1, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp1 = charToArray(tempState1.getState());
					if (!hs.contains(temp1))
					{
					hs.add(temp1);
					this.allPossibileStates.add(tempState1);
					}
					k++;
				}
				else if(board[stuckx+1][stucky] == '7' || board[stuckx+1][stucky] == 'F')
				{
					{
						temp[k] = cloner(board);
						temp[k][stuckx+1][stucky] = 'L';
						State<char[][]> tempState2 = new State<char[][]>(temp[k]);
						tempState2.setCost(LengthAndDistance(tempState2, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp2 = charToArray(tempState2.getState());
						if (!hs.contains(temp2))
						{
						hs.add(temp2);
						this.allPossibileStates.add(tempState2);
						}
						k++;
						temp[k] = cloner(board);
						temp[k][stuckx+1][stucky] = 'J';
						State<char[][]> tempState3 = new State<char[][]>(temp[k]);
						tempState3.setCost(LengthAndDistance(tempState3, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp3 = charToArray(tempState3.getState());
						if (!hs.contains(temp3))
						{
						hs.add(temp3);
						this.allPossibileStates.add(tempState3);
						}
						k++;
					}
				}
			}
		}
		if (board[stuckx][stucky] == 'L') // then i stucked from up or from right
		{
			if ((camey == stucky +1) && (isValid(board,stuckx,stucky+1))) // IF I CAME FROM RIGHT
			{
				temp[k] = cloner(board);
				temp[k][stuckx][stucky] = 'F';
				State<char[][]> tempState0 = new State<char[][]>(temp[k]);
				tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
				ArrayList<String> temp0 = charToArray(tempState0.getState());
				if (!hs.contains(temp0))
				{
				hs.add(temp0);
				this.allPossibileStates.add(tempState0);
				}
				k++;
			}
			if ((camex == stuckx - 1) && (isValid(board,stuckx-1,stuckx))) // IF I CAME FROM UP
			{
				temp[k] = cloner(board);
				temp[k][stuckx][stucky] = 'J';
				State<char[][]> tempState0 = new State<char[][]>(temp[k]);
				tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
				ArrayList<String> temp0 = charToArray(tempState0.getState());
				if (!hs.contains(temp0))
				{
				hs.add(temp0);
				this.allPossibileStates.add(tempState0);
				}
				k++;
			}
			if ( (isValid(board,stuckx,stucky+1)) && board[stuckx][stucky+1] != ' ' && (camey != stucky + 1)) // trying to change the right side.
			{
				if (board[stuckx][stucky+1] == '|')
				{
					temp[k] = cloner(board);
					temp[k][stuckx][stucky+1] = '-';
					State<char[][]> tempState1 = new State<char[][]>(temp[k]);
					tempState1.setCost(LengthAndDistance(tempState1, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp1 = charToArray(tempState1.getState());
					if (!hs.contains(temp1))
					{
					hs.add(temp1);
					this.allPossibileStates.add(tempState1);
					}
					k++;
				}
				else if(board[stuckx][stucky+1] == 'F' || board[stuckx][stucky+1] == 'L')
				{
					{
						temp[k] = cloner(board);
						temp[k][stuckx][stucky+1] = '7';
						State<char[][]> tempState2 = new State<char[][]>(temp[k]);
						tempState2.setCost(LengthAndDistance(tempState2, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp2 = charToArray(tempState2.getState());
						if (!hs.contains(temp2))
						{
						hs.add(temp2);
						this.allPossibileStates.add(tempState2);
						}
						k++;
						temp[k] = cloner(board);
						temp[k][stuckx][stucky+1] = 'J';
						State<char[][]> tempState3 = new State<char[][]>(temp[k]);
						tempState3.setCost(LengthAndDistance(tempState3, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp3 = charToArray(tempState3.getState());
						if (!hs.contains(temp3))
						{
						hs.add(temp3);
						this.allPossibileStates.add(tempState3);
						}
						k++;
					}
				}
			}
			else if ( (isValid(board,stuckx-1,stucky)) && board[stuckx-1][stucky] != ' ' && (camex != stuckx-1)) // - trying to change my up side
			{
				if (board[stuckx-1][stucky] == '-')
				{
					temp[k] = cloner(board);
					temp[k][stuckx-1][stucky] = '|';
					State<char[][]> tempState1 = new State<char[][]>(temp[k]);
					tempState1.setCost(LengthAndDistance(tempState1, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp1 = charToArray(tempState1.getState());
					if (!hs.contains(temp1))
					{
					hs.add(temp1);
					this.allPossibileStates.add(tempState1);
					}
					k++;
				}
				else if(board[stuckx-1][stucky] == 'L' || board[stuckx-1][stucky] == 'J')
				{
					{
						temp[k] = cloner(board);
						temp[k][stuckx-1][stucky] = '7';
						State<char[][]> tempState2 = new State<char[][]>(temp[k]);
						tempState2.setCost(LengthAndDistance(tempState2, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp2 = charToArray(tempState2.getState());
						if (!hs.contains(temp2))
						{
						hs.add(temp2);
						this.allPossibileStates.add(tempState2);
						}
						k++;
						temp[k] = cloner(board);
						temp[k][stuckx-1][stucky] = 'F';
						State<char[][]> tempState3 = new State<char[][]>(temp[k]);
						tempState3.setCost(LengthAndDistance(tempState3, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp3 = charToArray(tempState3.getState());
						if (!hs.contains(temp3))
						{
						hs.add(temp3);
						this.allPossibileStates.add(tempState2);
						}
						k++;
					}
				}
			}
		}
		if (board[stuckx][stucky] == 'F') // then i stucked from right or from down
		{
			if ((camey == stucky +1) && (isValid(board,stuckx,stucky+1))) // IF I CAME FROM RIGHT
			{
				temp[k] = cloner(board);
				temp[k][stuckx][stucky] = 'L';
				State<char[][]> tempState0 = new State<char[][]>(temp[k]);
				tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
				ArrayList<String> temp0 = charToArray(tempState0.getState());
				if (!hs.contains(temp0))
				{
				hs.add(temp0);
				this.allPossibileStates.add(tempState0);
				}
				k++;
			}
			if ((camex == stuckx + 1) && (isValid(board,stuckx+1,stuckx))) // IF I CAME FROM DOWN
			{
				temp[k] = cloner(board);
				temp[k][stuckx][stucky] = '7';
				State<char[][]> tempState0 = new State<char[][]>(temp[k]);
				tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
				ArrayList<String> temp0 = charToArray(tempState0.getState());
				if (!hs.contains(temp0))
				{
				hs.add(temp0);
				this.allPossibileStates.add(tempState0);
				}
				k++;
			}
			if ( (isValid(board,stuckx,stucky+1)) && board[stuckx][stucky+1] != ' ' && (camey != stucky + 1)) // trying to change the right side.
			{
				if (board[stuckx][stucky+1] == '|')
				{
					temp[k] = cloner(board);
					temp[k][stuckx][stucky+1] = '-';
					State<char[][]> tempState1 = new State<char[][]>(temp[k]);
					tempState1.setCost(LengthAndDistance(tempState1, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp1 = charToArray(tempState1.getState());
					if (!hs.contains(temp1))
					{
					hs.add(temp1);
					this.allPossibileStates.add(tempState1);
					}
					k++;
				}
				else if(board[stuckx][stucky+1] == 'F' || board[stuckx][stucky+1] == 'L')
				{
					{
						temp[k] = cloner(board);
						temp[k][stuckx][stucky+1] = '7';
						State<char[][]> tempState2 = new State<char[][]>(temp[k]);
						tempState2.setCost(LengthAndDistance(tempState2, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp2 = charToArray(tempState2.getState());
						if (!hs.contains(temp2))
						{
						hs.add(temp2);
						this.allPossibileStates.add(tempState2);
						}
						k++;
						temp[k] = cloner(board);
						temp[k][stuckx][stucky+1] = 'J';
						State<char[][]> tempState3= new State<char[][]>(temp[k]);
						tempState3.setCost(LengthAndDistance(tempState3, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp3 = charToArray(tempState3.getState());
						if (!hs.contains(temp3))
						{
						hs.add(temp3);
						this.allPossibileStates.add(tempState3);
						}
						k++;
					}
				}
			}
			else if ( (isValid(board,stuckx+1,stucky)) && board[stuckx+1][stucky] != ' ' && (camex != stuckx+1)) // - trying to change my down side
			{
				if (board[stuckx+1][stucky] == '-')
				{
					temp[k] = cloner(board);
					temp[k][stuckx+1][stucky] = '|';
					State<char[][]> tempState1 = new State<char[][]>(temp[k]);
					tempState1.setCost(LengthAndDistance(tempState1, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp1 = charToArray(tempState1.getState());
					if (!hs.contains(temp1))
					{
					hs.add(temp1);
					this.allPossibileStates.add(tempState1);
					}
					k++;
				}
				else if(board[stuckx+1][stucky] == 'F' || board[stuckx+1][stucky] == '7')
				{
					{
						temp[k] = cloner(board);
						temp[k][stuckx+1][stucky] = 'L';
						State<char[][]> tempState2 = new State<char[][]>(temp[k]);
						tempState2.setCost(LengthAndDistance(tempState2, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp2 = charToArray(tempState2.getState());
						if (!hs.contains(temp2))
						{
						hs.add(temp2);
						this.allPossibileStates.add(tempState2);
						}
						k++;
						temp[k] = cloner(board);
						temp[k][stuckx+1][stucky] = 'J';
						State<char[][]> tempState3 = new State<char[][]>(temp[k]);
						tempState3.setCost(LengthAndDistance(tempState3, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp3 = charToArray(tempState3.getState());
						if (!hs.contains(temp3))
						{
						hs.add(temp3);
						this.allPossibileStates.add(tempState3);
						}
						k++;
					}
				}
			}
		}
		if (board[stuckx][stucky] == 'J') // then i stucked from left or from up
		{
			if ((camey == stucky -1) && (isValid(board,stuckx,stucky-1))) // IF I CAME FROM LEFT
			{
				temp[k] = cloner(board);
				temp[k][stuckx][stucky] = '7';
				State<char[][]> tempState0 = new State<char[][]>(temp[k]);
				tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
				ArrayList<String> temp0 = charToArray(tempState0.getState());
				if (!hs.contains(temp0))
				{
				hs.add(temp0);
				this.allPossibileStates.add(tempState0);
				}
				k++;
			}
			if ((camex == stuckx - 1) && (isValid(board,stuckx-1,stuckx))) // IF I CAME FROM UP
			{
				temp[k] = cloner(board);
				temp[k][stuckx][stucky] = 'L';
				State<char[][]> tempState0 = new State<char[][]>(temp[k]);
				tempState0.setCost(LengthAndDistance(tempState0, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
				ArrayList<String> temp0 = charToArray(tempState0.getState());
				if (!hs.contains(temp0))
				{
				hs.add(temp0);
				this.allPossibileStates.add(tempState0);
				}
				k++;
			}
			if ( (isValid(board,stuckx,stucky-1)) && board[stuckx][stucky-1] != ' ' && (camey != stucky - 1)) // trying to change the left side.
			{

				if (board[stuckx][stucky-1] == '|')
				{
					temp[k] = cloner(board);
					temp[k][stuckx][stucky-1] = '-';
					State<char[][]> tempState1 = new State<char[][]>(temp[k]);
					tempState1.setCost(LengthAndDistance(tempState1, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp1 = charToArray(tempState1.getState());
					if (!hs.contains(temp1))
					{
					hs.add(temp1);
					this.allPossibileStates.add(tempState1);
					}
					k++;
				}
				else if(board[stuckx][stucky-1] == '7' || board[stuckx][stucky-1] == 'J')
				{
					{
						temp[k] = cloner(board);
						temp[k][stuckx][stucky-1] = 'F';
						State<char[][]> tempState2 = new State<char[][]>(temp[k]);
						tempState2.setCost(LengthAndDistance(tempState2, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp2 = charToArray(tempState2.getState());
						if (!hs.contains(temp2))
						{
						hs.add(temp2);
						this.allPossibileStates.add(tempState2);
						}
						k++;
						temp[k] = cloner(board);
						temp[k][stuckx][stucky-1] = 'L';
						State<char[][]> tempState3 = new State<char[][]>(temp[k]);
						tempState3.setCost(LengthAndDistance(tempState3, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp3 = charToArray(tempState3.getState());
						if (!hs.contains(temp3))
						{
						hs.add(temp3);
						this.allPossibileStates.add(tempState3);
						}
						k++;
					}
				}
			}
			else if ( (isValid(board,stuckx-1,stucky)) && board[stuckx-1][stucky] != ' ' && (camex != stuckx-1)) // - trying to change my up side
			{
				if (board[stuckx-1][stucky] == '-')
				{
					temp[k] = cloner(board);
					temp[k][stuckx-1][stucky] = '|';
					State<char[][]> tempState1 = new State<char[][]>(temp[k]);
					tempState1.setCost(LengthAndDistance(tempState1, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
					ArrayList<String> temp1 = charToArray(tempState1.getState());
					if (!hs.contains(temp1))
					{
					hs.add(temp1);
					this.allPossibileStates.add(tempState1);
					}
					k++;
				}
				else if(board[stuckx-1][stucky] == 'J' || board[stuckx-1][stucky] == 'L')
				{
					{
						temp[k] = cloner(board);
						temp[k][stuckx-1][stucky] = '7';
						State<char[][]> tempState2 = new State<char[][]>(temp[k]);
						tempState2.setCost(LengthAndDistance(tempState2, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp2 = charToArray(tempState2.getState());
						if (!hs.contains(temp2))
						{
						hs.add(temp2);
						this.allPossibileStates.add(tempState2);
						}
						k++;
						temp[k] = cloner(board);
						temp[k][stuckx-1][stucky] = 'F';
						State<char[][]> tempState3 = new State<char[][]>(temp[k]);
						tempState3.setCost(LengthAndDistance(tempState3, StartPoint[0], StartPoint[1], EndPoint[0], EndPoint[1]));
						ArrayList<String> temp3 = charToArray(tempState3.getState());
						if (!hs.contains(temp3))
						{
						hs.add(temp3);
						this.allPossibileStates.add(tempState3);
						}
						k++;
					}
				}
			}
		}
	}
	
	public char[][] Convertor(ArrayList<String> array) // CONVERT_FROM_ARRAYLIST_TO_CHAR[][]
	{
		char[][] cloner = new char[array.size()][array.get(0).length()];
		int i,j;
		for (i=0;i<array.size();i++)
			cloner[i] = array.get(i).toCharArray();
		return cloner;
	}
	
	
	public ArrayList<String> charToArray(char[][] toConvert) // CONVERT_FROM_CHAR[][]_TO_ARRAYLIST
	{
		ArrayList<String> trying = new ArrayList<>();
		int i,j;
		StringBuilder sbd = new StringBuilder();
		for (i=0;i<toConvert.length;i++)
		{
			for (j=0;j<toConvert[i].length;j++)
				sbd.append(toConvert[i][j]);
			String ss=sbd.toString();
			trying.add(ss);
			sbd = null;
			sbd = new StringBuilder();
		}
		return trying;		
	}
	
	public char[][] cloner(char[][] toClone) // CREATE_CLONE_OF_CHAR[][]
	{
		char[][] theClone = new char[toClone.length][toClone[0].length];
		for (int i=0;i<theClone.length;i++)
			for (int j=0;j<theClone[i].length;j++)
			{
				theClone[i][j] = toClone[i][j];
			}
		return theClone;
	}
	
	char rotatePiece(char rotateable) // ROTATE_SOME_CHAR_TYPE_AT_THE_BOARD
	{ 
		switch (rotateable) {
		case '|': {
			rotateable = '-';
			return rotateable;
		}
		case '-': {
			rotateable = '|';
			return rotateable;
		}
		case '7': {
			rotateable = 'J';
			return rotateable;
		}
		case 'J': {
			rotateable = 'L';
			return rotateable;
		}
		case 'F': {
			rotateable = '7';
			return rotateable;
		}
		case 'L':{
			rotateable = 'F';
			return rotateable;
		}
				
		default:
			return rotateable;
		}
	}
	
	public boolean CheckMyRight(char check) {
		switch (check) {
		case '-':
			return true;
		case 'J':
			return true;
		case '7':
			return true;
		case 'g':
			return true;
		default:
			return false;
		}
	}
	public boolean CheckMyDown(char check) {
		switch (check) {
		case '|':
			return true;
		case 'J':
			return true;
		case 'L':
			return true;
		case 'g':
			return true;
		default:
			return false;
		}
	}
	public boolean CheckMyLeft(char check) {
		switch (check) {
		case '-':
			return true;
		case 'L':
			return true;
		case 'F':
			return true;
		case 'g':
			return true;
		default:
			return false;
		}
	}
	public boolean CheckMyUp(char check)
{
		switch (check) {
		case '7':
			return true;
		case 'F':
			return true;
		case '|':
			return true;
		case 'g':
			return true;
		default:
			return false;
		}
	}
	public boolean isRightable(char rightable)
	{
		switch(rightable)
		{
		case '-':
			return true;
		case 'F':
			return true;
		case 'L':
			return true;
		case 's':
			return true;
		case 'g':
			return true;
		default:
			return false;
		}
	}
	public boolean isDownable(char downtable)
	{
		switch(downtable)
		{
		case '|':
			return true;
		case 'F':
			return true;
		case '7':
			return true;
		case 's':
			return true;
		case 'g':
			return true;
		default:
			return false;
		}
	}
	public boolean isUpable(char upable)
	{
		switch(upable)
		{
		case '|':
			return true;
		case 'J':
			return true;
		case 'L':
			return true;
		case 's':
			return true;
		case 'g':
			return true;
		default:
			return false;
		}
	}
	public boolean isLeftable(char leftable)
	{
		switch(leftable)
		{
		case '-':
			return true;
		case 'J':
			return true;
		case '7':
			return true;
		case 's':
			return true;
		case 'g':
			return true;
		default:
			return false;
		}
	}
	
	public boolean flow(char[][] board,int xs,int ys) // FLOW_FUNCTION_TO_TRAVEL_ON_THE_BOARD-->HELP_FUNCTION_FOR_"IS_GOAL"
	{
		int camex = -1;
		int camey = -1;
		while(true)
		{
			if (board[xs][ys] == 'g')
				return true;
			if (board[xs][ys] == 's')
				System.out.println("Flow sayed : there is a LOOP!");
			/*
			 * // TRYING TO TRAVEL //
			 */
			if ( (isRightable(board[xs][ys])) && (isValid(board, xs, ys+1)) && (CheckMyRight(board[xs][ys+1])) && (camey != ys+1) ) // "right"
			{
				{camey = ys;++ys;
				 camex = xs;
				}
			}
			else if ( (isUpable(board[xs][ys])) && (isValid(board, xs-1, ys)) && (CheckMyUp(board[xs-1][ys])) && (camex != xs-1) ) // "up"
			{
				{camex = xs; --xs;
				camey = ys;
				}
			}
			else if ( (isDownable(board[xs][ys])) && (isValid(board, xs+1, ys)) && (CheckMyDown(board[xs+1][ys])) && (camex != xs+1) ) // "down"
			{
				{camex = xs;++xs;
				 camey= ys;
				}
			}
			else if ( (isLeftable(board[xs][ys])) && (isValid(board, xs, ys-1)) && (CheckMyLeft(board[xs][ys-1])) && (camey != ys-1)) // "left"
			{
				{camey = ys;--ys;
				 camex = xs;
				}
			}
			else
				return false;
		}
	}
	
	public boolean isValid(char[][] board,int i,int j) // CHECK_THAT_IM_NOT_LEAKING_FROM_THE_BOARD_BORDERS
	{
		if ((i>=0) && (i<board.length) && (j>=0) && (j<board[0].length))
			return true;
		return false;
	}
	
	public int distance(int currentX, int currentY, int xg, int yg) // RETURN_INT_"MANHATAN"_DISTANCE_FROM_CURRENT_STATE_TO_"GOAL"
	{
		int distanceX = currentX - xg;
		int distanceY = currentY - yg;
		if (distanceX <0)
			distanceX = distanceX*(-1);
		if (distanceY < 0)
			distanceY = distanceY*(-1);
		return distanceX + distanceY;
	}
	
	public double LengthAndDistance(State<char[][]> stateToTravel,int xs ,int ys ,int xgoal, int ygoal) // COST_FUNCTION
	{
		int camex = -1;
		int camey = -1;
		int counter = 0;
		int thisIsMyDistance;
		while(true)
		{
			/*
			 * This function are traveling on each state, and giving a grade about how long is my PIPE.
			 */
			if ( (isRightable(stateToTravel.getState()[xs][ys])) && (isValid(stateToTravel.getState(), xs, ys+1)) && (CheckMyRight(stateToTravel.getState()[xs][ys+1])) && (camey != ys+1) ) // "right"
			{
				{camey = ys;++ys; camex = xs;}
				counter++;
			}
			else if ( (isUpable(stateToTravel.getState()[xs][ys])) && (isValid(stateToTravel.getState(), xs-1, ys)) && (CheckMyUp(stateToTravel.getState()[xs-1][ys])) && (camex != xs-1)) // "up"
			{
				{camex = xs; --xs; camey = ys;}
				counter++;
			}
			else if ( (isDownable(stateToTravel.getState()[xs][ys])) && (isValid(stateToTravel.getState(), xs+1, ys)) && (CheckMyDown(stateToTravel.getState()[xs+1][ys])) && (camex != xs+1)) // "down"
			{
				{camex = xs;++xs; camey = ys;}
				counter++;
			}
			else if ( (isLeftable(stateToTravel.getState()[xs][ys])) && (isValid(stateToTravel.getState(), xs, ys-1)) && (CheckMyLeft(stateToTravel.getState()[xs][ys-1])) && (camey != ys-1) ) // "left"
			{
				{camey = ys;--ys; camex = xs;}
				counter++;
			}
			else
			{
				thisIsMyDistance = distance(xs, ys, xgoal, ygoal);
				break;
			}
		}
			return boardSize - counter + thisIsMyDistance;
	}
	public int[] getStuckPoint(char[][] board,int xs ,int ys ,int xgoal, int ygoal) // RETURN_X&Y_OF_STUCK_POSITION_WHEN_TRAVELED
	{
		int camex = StartPoint[0];
		int camey = StartPoint[1];
		int counter = 0;
		while(true)
		{
			/*
			 * This function are traveling on each state, and giving a grade about how long is my PIPE.
			 */
			if ( (isRightable(board[xs][ys])) && (isValid(board, xs, ys+1)) && (CheckMyRight(board[xs][ys+1])) && (camey != ys+1) ) // "right"
			{
				{camey = ys;++ys; camex = xs;}
				counter++;
			}
			else if ( (isUpable(board[xs][ys])) && (isValid(board, xs-1, ys)) && (CheckMyUp(board[xs-1][ys])) && (camex != xs-1)) // "up"
			{
				{camex = xs; --xs; camey = ys;}
				counter++;
			}
			else if ( (isDownable(board[xs][ys])) && (isValid(board, xs+1, ys)) && (CheckMyDown(board[xs+1][ys])) && (camex != xs+1)) // "down"
			{
				{camex = xs;++xs; camey = ys;}
				counter++;
			}
			else if ( (isLeftable(board[xs][ys])) && (isValid(board, xs, ys-1)) && (CheckMyLeft(board[xs][ys-1])) && (camey != ys-1) ) // "left"
			{
				{camey = ys;--ys; camex = xs;}
				counter++;
			}
			else
			{
				XandY[0] = xs;
				XandY[1] = ys;
				XandY[2] = camex;
				XandY[3] = camey;
				XandY[4] = counter;
				return XandY;
			}
		}
		
	}
}

