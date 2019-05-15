package snake;

import java.util.Random;

public class Food {
	private int x;
	private int y;
	private Snake snake;
	
	public Food(Snake snake) {
		this.snake=snake;
		this.x=new Random().nextInt(20);
		this.y= new Random().nextInt(20);
		while (snake.occupied(x, y)) {
			this.x=new Random().nextInt(20);
			this.y= new Random().nextInt(20);
		}
		
		
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
