package mypackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Stack;

public class MyMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello, World!");
		System.out.println("Another line!");
		
		int[] initial = {
				2, 3, 7, 4, 5,
				1, -1, 11, -1, 8,
				6, 10, 0, 12, 15,
				9, -1, 14, -1, 20,
				13, 16, 17, 18, 19
		};
		
		int[] goal = {
				1, 2, 3, 4, 5,
				6, -1, 7, -1, 8,
				9, 10, 0, 11, 12,
				13, -1, 14, -1, 15,
				16, 17, 18, 19, 20
		};
		
		transformA(initial);
		transformA(goal);
		
		Board i = new Board(initial, 0, null);
		Board g = new Board(goal, 0, null);

		/*
		Board t = g.up().up().right().right().down();
		g.printBoard();
		System.out.println("h(g) = " + g.getH());
		t.printBoard();
		System.out.println("h(t) = " + t.getH());
		System.out.println("h(i) = " + i.getH());
		*/
		
		PriorityQueue<Board> open = new PriorityQueue<Board>();
		ArrayList<Board> closed = new ArrayList<Board>();
		Stack<Board> path = new Stack<Board>();
		
		open.add(i);
		boolean goalFound = false;
		
		while(!open.isEmpty() && !goalFound)
		{
			Board current = open.poll();
			
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
			else
			{
				Board[] nexts = current.getSuccessors();
				
				if (nexts != null)
				{
					for (Board n : nexts)
					{
						if (!open.contains(n) && !closed.contains(n))
						{
							open.add(n);
						}
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
		}
		
		int count = 0;
		
		while (!path.isEmpty())
		{
			count++;
			Board n = path.pop();
			
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
	
	public static void transformA(int[] a)
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
	
	public static void transformB(int[] a)
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
	
	public static void printArray(int[] a)
	{
		System.out.print("[ ");
		for(int i = 0; i < a.length; i++)
		{
			if(i != 0 && i%5 == 0) System.out.println();
			System.out.print("" + a[i] + ' ');
		}
		System.out.println("]");
	}
}

class Board implements Comparable<Board>
{
	private int[] board;
	private int h; // Manhattan distance to goal
	private int g; // Actual cost so far
	private int f; // Estimate from initial to goal
	private int z;
	private Board predecessor;
	
	public Board(int[] board, int g, Board predecessor)
	{
		this.board = board;
		this.g = g;
		this.predecessor = predecessor;
		this.computeH();
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
	
	public Board up()
	{
		if (this.z >= 20) return null;
		if (this.board[z+5] == -1) return null;
		
		int[] newBoard = Arrays.copyOf(this.board, this.board.length);
		newBoard[this.z] = newBoard[this.z+5];
		newBoard[this.z+5] = 0;
		
		return new Board(newBoard, this.g + 1, this);
	}
	
	public Board down()
	{
		if (this.z < 5) return null;
		if (this.board[this.z-5] == -1) return null;
		
		int[] newBoard = Arrays.copyOf(this.board, this.board.length);
		newBoard[this.z] = newBoard[this.z-5];
		newBoard[this.z-5] = 0;
		
		return new Board(newBoard, this.g + 1, this);
	}
	
	public Board left()
	{
		if (this.z%5 == 4) return null;
		if (board[this.z+1] == -1) return null;
		
		int[] newBoard = Arrays.copyOf(this.board, this.board.length);
		newBoard[this.z] = newBoard[this.z+1];
		newBoard[this.z+1] = 0;
		
		return new Board(newBoard, this.g + 1, this);
	}
	
	public Board right()
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
		System.out.print("[ ");
		for(int i = 0; i < this.board.length; i++)
		{
			if(i%5 == 0) System.out.println();
			System.out.print("" + board[i] + ' ');
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
