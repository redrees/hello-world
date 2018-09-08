package mypackage;

import java.util.Arrays;

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
		
		Board i = new Board(initial, 0);
		Board g = new Board(goal, 0);
		
		Board t = g.up().up().right().right().down();
		
		g.printBoard();
		System.out.println("h(g) = " + g.getH());
		t.printBoard();
		System.out.println("h(t) = " + t.getH());
		System.out.println("h(i) = " + i.getH());
		
		
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

class Board
{
	private int[] board;
	private int h; // Manhattan distance to goal
	private int g; // Actual cost so far
	private int f; // Estimate from initial to goal
	private int z;
	
	public Board(int[] board, int g)
	{
		this.board = board;
		this.g = g;
		this.computeH();
	}
	
	private void computeH()
	{
		h = 0;
		for(int i = 0; i < board.length; i++)
		{
			if (board[i] > 0)
			{
				h += Math.abs(((board[i]-1)%5) - (i%5)) + Math.abs(board[i] - i - 1)/5;
			}
			else if (board[i] == 0)
			{
				z = i;
			}
		}
	}
	
	public int getH()
	{
		return h;
	}
	
	public int getG()
	{
		return g;
	}
	
	public int getF()
	{
		return f;
	}
	
	public Board up()
	{
		if (z >= 20) return null;
		
		int[] newBoard = Arrays.copyOf(board, board.length);
		newBoard[z] = newBoard[z+5];
		newBoard[z+5] = 0;
		
		return new Board(newBoard, this.g + 1);
	}
	
	public Board down()
	{
		if (z < 5) return null;
		
		int[] newBoard = Arrays.copyOf(board, board.length);
		newBoard[z] = newBoard[z-5];
		newBoard[z-5] = 0;
		
		return new Board(newBoard, this.g + 1);
	}
	
	public Board left()
	{
		if (z%5 == 4) return null;
		
		int[] newBoard = Arrays.copyOf(board, board.length);
		newBoard[z] = newBoard[z+1];
		newBoard[z+1] = 0;
		
		return new Board(newBoard, this.g + 1);
	}
	
	public Board right()
	{
		if (z%5 == 0) return null;
		
		int[] newBoard = Arrays.copyOf(board, board.length);
		newBoard[z] = newBoard[z-1];
		newBoard[z-1] = 0;
		
		return new Board(newBoard, this.g + 1);
	}
	
	public void printBoard()
	{
		System.out.print("[ ");
		for(int i = 0; i < board.length; i++)
		{
			if(i%5 == 0) System.out.println();
			System.out.print("" + board[i] + ' ');
		}
		System.out.println("]");
	}
}
