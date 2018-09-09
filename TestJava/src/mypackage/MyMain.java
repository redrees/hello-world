package mypackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Stack;

public class MyMain {

	public static void main(String[] args) {
		
		int[] initial = {
				2, 3, 7, 4, 5,
				1, -1, 11, -1, 8,
				6, 10, 0, 12, 15,
				9, -1, 14, -1, 20,
				13, 16, 17, 18, 19
		};
		
		// Transform board representation for easier heuristic calculation
		transformA(initial);
		
		Board i = new Board(initial, 0, null);

		PriorityQueue<Board> open = new PriorityQueue<Board>();
		ArrayList<Board> closed = new ArrayList<Board>();
		Stack<Board> path = new Stack<Board>();
		
		open.add(i);
		boolean goalFound = false;
		
		// A* algorithm to find a solution path
		while(!open.isEmpty() && !goalFound)
		{
			Board current = open.poll();
			
			// If at the goal state, record path taken
			if (current.isGoalState())
			{
				path.push(current);
				Board n = current;
				while((n=n.getPredecessor()) != null)
				{
					path.push(n);
				}
				goalFound = true;
			}
			// If not at the goal state, process successors
			else
			{
				Board[] nexts = current.getSuccessors();
				
				for (Board n : nexts)
				{
					// Unvisited successor boards are added to open PQ
					if (!open.contains(n) && !closed.contains(n))
					{
						open.add(n);
					}
					// If the successor has been visited and has lesser estimate, replace or put it back into open PQ
					else
					{
						Collection<Board> c;
						
						if (open.contains(n)) c = open;
						else c = closed;
						
						Iterator<Board> iter = c.iterator();
						boolean cont = true;
						while(iter.hasNext() && cont)
						{
							Board t = iter.next();
							if(n.equals(t) && n.compareTo(t) < 0)
							{
								c.remove(n);
								open.add(n);
								
								cont = false;
							}
						}
					}
				}
			}
		}
		
		int count = 0;
		
		// Print all board states along the solution path
		while (!path.isEmpty())
		{
			count++;
			Board n = path.pop();
			
			// Transform the board representation back to what it actually is
			transformB(n.getBoard());
			
			System.out.println("" + count + "th state");
			n.printBoard();
			System.out.println();
		}
		
		/*
		transformA(goal);
		printArray(goal);
		transformB(goal);
		printArray(goal);
		*/
	}

	// >6 -> +1
	// >7 -> +2
	// >10 -> +3
	// >13 -> +4
	// >14 -> +5
	
	private static void transformA(int[] a)
	{
		for(int i = 0; i < a.length; i++)
		{
			if (a[i] > 14) a[i]++;
			if (a[i] > 13) a[i]++;
			if (a[i] > 10) a[i]++;
			if (a[i] > 7) a[i]++;
			if (a[i] > 6) a[i]++;
		}
	}
	
	private static void transformB(int[] a)
	{
		for(int i = 0; i < a.length; i++)
		{
			if (a[i] > 19) a[i]--;
			if (a[i] > 17) a[i]--;
			if (a[i] > 13) a[i]--;
			if (a[i] > 9) a[i]--;
			if (a[i] > 7) a[i]--;
		}
	}
}

class Board implements Comparable<Board>
{
	private int[] board;
	private int h; // Heuristics to goal (Manhattan distance)
	private int g; // Actual cost so far
	private int f; // Estimate from initial to goal
	private int z; // Empty space location
	private Board predecessor;
	
	public Board(int[] board, int g, Board predecessor)
	{
		this.board = board;
		this.g = g;
		this.predecessor = predecessor;
		this.computeH();
		this.computeF();
	}
	
	private void computeH()
	{
		this.h = 0;
		for(int i = 0; i < this.board.length; i++)
		{
			if (this.board[i] > 0)
			{
				this.h += Math.abs(((this.board[i]-1)%5) - (i%5)) + Math.abs(this.board[i] - i - 1)/5;
			}
			else if (this.board[i] == 0)
			{
				z = i;
			}
		}
	}
	
	private void computeF()
	{
		this.f = this.g + this.h;
	}
	
	public int getH()
	{
		return this.h;
	}
	
	public int getG()
	{
		return this.g;
	}
	
	public int getF()
	{
		return this.f;
	}
	
	public int[] getBoard()
	{
		return this.board;
	}
	
	public Board getPredecessor()
	{
		return this.predecessor;
	}
	
	private Board up()
	{
		if (this.z >= 20) return null;
		if (this.board[z+5] == -1) return null;
		
		int[] newBoard = Arrays.copyOf(this.board, this.board.length);
		newBoard[this.z] = newBoard[this.z+5];
		newBoard[this.z+5] = 0;
		
		return new Board(newBoard, this.g + 1, this);
	}
	
	private Board down()
	{
		if (this.z < 5) return null;
		if (this.board[this.z-5] == -1) return null;
		
		int[] newBoard = Arrays.copyOf(this.board, this.board.length);
		newBoard[this.z] = newBoard[this.z-5];
		newBoard[this.z-5] = 0;
		
		return new Board(newBoard, this.g + 1, this);
	}
	
	private Board left()
	{
		if (this.z%5 == 4) return null;
		if (board[this.z+1] == -1) return null;
		
		int[] newBoard = Arrays.copyOf(this.board, this.board.length);
		newBoard[this.z] = newBoard[this.z+1];
		newBoard[this.z+1] = 0;
		
		return new Board(newBoard, this.g + 1, this);
	}
	
	private Board right()
	{
		if (this.z%5 == 0) return null;
		if (this.board[z-1] == -1) return null;
		
		int[] newBoard = Arrays.copyOf(this.board, this.board.length);
		newBoard[this.z] = newBoard[this.z-1];
		newBoard[this.z-1] = 0;
		
		return new Board(newBoard, this.g + 1, this);
	}
	
	public Board[] getSuccessors()
	{
		Board[] r;
		int count = 0;
		
		Board up = this.up();
		Board down = this.down();
		Board left = this.left();
		Board right = this.right();
		
		if (up != null) count++;
		if (down != null) count++;
		if (left != null) count++;
		if (right != null) count++;
		
		if (count == 0) return null;
		
		r = new Board[count];
		count = 0;
		
		if (up != null) r[count++] = up;
		if (down != null) r[count++] = down;
		if (left != null) r[count++] = left;
		if (right != null) r[count++] = right;
		
		return r;
	}
	
	public void printBoard()
	{
		System.out.print("[");
		for(int i = 0; i < this.board.length; i++)
		{
			if(i%5 == 0 && i != 0) System.out.println();
			System.out.print(" " + board[i]);
		}
		System.out.println("]");
	}
	
	public boolean isGoalState()
	{
		if (h != 0) return false;
		
		return true;
	}
	
	public int compareTo(Board c) throws IllegalArgumentException
	{
		if (c == null) throw new IllegalArgumentException("Board can't be compared to null");
		
		if (this.f > c.getF()) return 1;
		if (this.f < c.getF()) return -1;
		
		return 0;
	}
	
	@Override
	public boolean equals(Object o) throws IllegalArgumentException
	{
		if (o == null) throw new IllegalArgumentException("Board can't be compared to null");
		if (!(o instanceof Board)) return false;
		
		return Arrays.equals(this.board, ((Board)o).getBoard());
	}
}
