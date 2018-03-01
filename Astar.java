import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Astar {
	public static final int COST = 1;
	public static  int ai=0;
	public static  int aj=0;

	static class Cell{  
		int heuristicCost = 0; //Heuristic cost
		int finalCost = 0; //G+H
		int i, j;
		Cell parent; 
		Cell(int i, int j){
			this.i = i;
			this.j = j; 
		}
	}
	static Cell [][] grid = new Cell[ai][aj];
	static PriorityQueue<Cell> open ;
	public    static boolean closed[][];
	static int startI, startJ;
	static int endI, endJ;

	public static void setBlocked(int i, int j){
		grid[i][j] = null;
	}

	public static void setStartCell(int i, int j){
		startI = i;
		startJ = j;
	}

	public static void setEndCell(int i, int j){
		endI = i;
		endJ = j; 
	}

	static void checkAndUpdateCost(Cell current, Cell t, int cost){
		if(t == null || closed[t.i][t.j])return;
		int t_final_cost = t.heuristicCost+cost;

		boolean inOpen = open.contains(t);
		if(!inOpen ){

			t.finalCost = t_final_cost;
			t.parent = current;
			//System.out.println("parrent"+t.parent.finalCost+"child"+t.finalCost);
			//System.out.println("parrent"+current.finalCost+"child"+t.finalCost);
			open.add(t);
		}
	}

	public static void aStarAlgorithm(){ 

		//add the start location to open list.
		open.add(grid[startI][startJ]);

		Cell current;

		while(true){ 

			current = open.poll();

			if(current==null)
				break;
			closed[current.i][current.j]=true; 

			if(current.equals(grid[endI][endJ])){
				return; 
			} 

			Cell t;  
			if(current.i-1>=0){
				t = grid[current.i-1][current.j];
				checkAndUpdateCost(current, t, current.finalCost+COST); 
			} 

			if(current.j-1>=0){
				t = grid[current.i][current.j-1];
				// System.out.println("tfc"+current.heuristicCost+"t.f"+t.heuristicCost);
				checkAndUpdateCost(current, t, current.finalCost+COST); 
			}

			if(current.j+1<grid[0].length){

				t = grid[current.i][current.j+1];
				checkAndUpdateCost(current, t, current.finalCost+COST); 
			}

			if(current.i+1<grid.length){

				t = grid[current.i+1][current.j];
				checkAndUpdateCost(current, t, current.finalCost+COST); 
			}
		} 
	}

	public static void matrix( int x, int y, int si, int sj, int ei, int ej,ArrayList<Integer> wi,ArrayList<Integer> wj){

		char[][] fiArray=new char[ai][aj];
		grid = new Cell[x][y];
		closed = new boolean[x][y];
		open = new PriorityQueue<>((Object o1, Object o2) -> {
			Cell c1 = (Cell)o1;
			Cell c2 = (Cell)o2;

			return c1.finalCost<c2.finalCost?-1:
				c1.finalCost>c2.finalCost?1:0;
		});


		//Set start position
		setStartCell(si, sj);  //Setting to 0,0 by default. Will be useful for the UI part
		//System.out.println("open"+open.size());
		//Set End Location
		setEndCell(ei, ej); 

		for(int i=0;i<x;++i){
			for(int j=0;j<y;++j){
				grid[i][j] = new Cell(i, j);
				grid[i][j].heuristicCost = Math.abs(i-endI)+Math.abs(j-endJ);
				//  System.out.print(grid[i][j].heuristicCost+" ");
			}
			//  System.out.println("\n");
		}
		grid[si][sj].finalCost = 0;

		/*
		             Set blocked cells. Simply set the cell values to null
		             for blocked cells.
		 */
		for(int i=0;i<wi.size();++i){
			setBlocked(wi.get(i), wj.get(i));
		}

		//Display initial map
		// System.out.println("Grid: "+x);
		for(int i=0;i<x;++i){
			for(int j=0;j<y;++j){
				if(i==si&&j==sj)

				{//System.out.print("S  "); 
					fiArray[i][j]='S';
				}
				else if(i==ei && j==ej)
				{//System.out.print("E  ");  //Destination
					fiArray[i][j]='E';
				}
				else if(grid[i][j]!=null)
				{//System.out.printf("0  ");
					fiArray[i][j]='.';
				}
				else
				{//System.out.print("W  "); 
					fiArray[i][j]='W';
				}
			}
			// System.out.println();
		} 
		// System.out.println();

		aStarAlgorithm(); 
		// System.out.println("\nScores for cells: "+y);
		for(int i=0;i<x;++i){
			for(int j=0;j<y;++j){
				if(grid[i][j]!=null)
				{//System.out.printf("%-3d ", grid[i][j].finalCost);
					// System.out.println(closed[i][j]);
					if(grid[i][j].finalCost!=0  )
						fiArray[i][j]='\"';  
				}
				else
				{//System.out.print("W  ");
					fiArray[i][j]='W';
				}
			}
			// System.out.println();
		}
		// System.out.println();

		if(closed[endI][endJ]){
			//Trace back the path 
			//System.out.println("Path: ");
			Cell current = grid[endI][endJ];
			// System.out.print("["+current.i+","+current.j+"]");
			while(current.parent!=null){
				//System.out.print(" -> "+"["+current.parent.i+","+current.parent.j+"]");
				fiArray[current.parent.i][current.parent.j]='*';
				current = current.parent;
			} 
			System.out.println("\n");
			System.out.println("\n**************SOLUTION MAP******************\n");
			fiArray[si][sj]='S';
			fiArray[ei][ej]='E';
			for(int i=0;i<x;++i){
				for(int j=0;j<y;++j){     
					System.out.print(fiArray[i][j]+" ");
				}
				System.out.println("\n");    
			}

		}else System.out.println("No possible path");
	} 





	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\Users\\P7009073\\Desktop\\map.txt");
		BufferedReader in = new BufferedReader(new FileReader(file)); 

		try{
			int si = 0,sj=0,ei=0,ej=0;
			ArrayList<Integer> wi=new ArrayList<Integer>();
			ArrayList<Integer> wj=new ArrayList<Integer>();
			//int ci=(int) line.count();
			String dimension =in.readLine();
			StringBuilder input = new StringBuilder();
			int i=0,j=0;
			System.out.println("\n****************INPUT MAP*******************\n");
			while(dimension !=null)
			{
				System.out.println(dimension);
				dimension=dimension.replaceAll("\\s*","");
				input.append(dimension).append("\n");

				for( j=0; j<dimension.length(); j++){
					char typeSymbol = dimension.charAt(j);
					//	matrix[i]=dimension.toCharArray();
					if(typeSymbol == 'W'||typeSymbol == 'w'){
						wi.add(i);
						wj.add(j);
					}
					else if(typeSymbol == 'S'||typeSymbol == 's'){
						si=i;
						sj=j;
					}
					else if(typeSymbol == 'E'||typeSymbol == 'e'){
						ei=i;
						ej=j;
					}
					//
					//System.out.print(matrix[i][j]+" ");
				}
				//System.out.println("\n");	
				dimension=in.readLine();

				i++;
			}
			ai=i--;
			aj=j--;
			//String a=input.toString();
			//String[] a1=a.split("\n");
			matrix( ai,aj, si, sj, ei, ej,wi,wj ); 
		}
		catch(IOException e){
			//	throw e;
		}
	}
}
