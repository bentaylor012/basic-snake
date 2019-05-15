package snake;

import java.util.ArrayList;
import java.util.Arrays;

public class Snake {
	private ArrayList<int[]> SNAKE_POS=new ArrayList<int[]>();
	
	public Snake(int[] startSpot) {
		int[] head = {startSpot[0],startSpot[1]};
		this.SNAKE_POS.add(head);
	}
	/*
	 * this simulates the movement of the snake basically pushing each position up one and making the 
	 * front the new position.
	 */
	public ArrayList<int[]> movement(int[] updatedFront){
		for (int i=SNAKE_POS.size()-1;i>=1;i--) {
			//System.out.println("from->to: "+SNAKE_POS.get(i)[0]+" "+SNAKE_POS.get(i)[1]+
			//		" -> "+SNAKE_POS.get(i-1)[0]+" "+SNAKE_POS.get(i-1)[1]);
			SNAKE_POS.get(i)[0]=(SNAKE_POS.get(i-1)[0]);
			SNAKE_POS.get(i)[1]=(SNAKE_POS.get(i-1)[1]);
		}
		SNAKE_POS.get(0)[0]=updatedFront[0];
		SNAKE_POS.get(0)[1]=updatedFront[1];
		return SNAKE_POS;
	}
	
	/*
	 * when the snake eats a dot this will be triggered increasing the size of the snake by one
	 * and placing the last dot at the end of the snake.
	 */
	public ArrayList<int[]> grow(int[] updatedFront) {
		if (SNAKE_POS.size()==1) {
			int[] hold= {SNAKE_POS.get(0)[0],SNAKE_POS.get(0)[1]};
			SNAKE_POS.add(hold);
		}
		else
		{
			int[] hold= {SNAKE_POS.get(SNAKE_POS.size()-1)[0],SNAKE_POS.get(SNAKE_POS.size()-1)[1]};
			SNAKE_POS.add(hold);
			for (int i=SNAKE_POS.size()-2;i>=1;i--) {
				SNAKE_POS.get(i)[0]=SNAKE_POS.get(i-1)[0];
				SNAKE_POS.get(i)[1]=SNAKE_POS.get(i-1)[1];
			}
		}
		
		SNAKE_POS.get(0)[0]=updatedFront[0];
		SNAKE_POS.get(0)[1]=updatedFront[1];
		
		return SNAKE_POS;
	}
	
	public ArrayList<int[]> getSnake(){
		return SNAKE_POS;
	}
	
	public boolean occupied(int x, int y) {
		for (int[] piece:SNAKE_POS) {
			if (piece[0]==x&&piece[1]==y)
				return true;
		}
		return false;
	}

}
