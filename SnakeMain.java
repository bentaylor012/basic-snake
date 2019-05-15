package snake;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

enum Direction{
	UP,DOWN,LEFT,RIGHT,DIE
}

public class SnakeMain extends Application{
	private int STAGE_WIDTH=20;
	private int STAGE_HEIGHT=20;
	private int SQUARE_SIZE=30;
	private int[] START= {STAGE_WIDTH/2,STAGE_HEIGHT/2};
	private Snake SNAKE;
	private int SCORE=0;
	private double DELAY=0.1;
	private Food FOOD;
	private Direction DIR=Direction.DOWN;
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) {
		HBox box = new HBox();
		Button button=new Button("New Game");
		BorderPane p=new BorderPane();
		Canvas canvas=new Canvas(STAGE_WIDTH*SQUARE_SIZE,STAGE_HEIGHT*SQUARE_SIZE);
		box.getChildren().add(button);
		TextField text= new TextField();
		text.appendText("SCORE: "+Integer.toString(SCORE));
		box.getChildren().add(text);
		p.setTop(box);
		p.setCenter(canvas);
		
		GraphicsContext gc=canvas.getGraphicsContext2D();
		
		p.setOnKeyPressed(new moveSnake(gc));
		
		SNAKE=new Snake(START);
		FOOD=new Food(SNAKE);
		
		printBoard(gc);
		button.setOnAction(new newGame(gc,text));
		primaryStage.setScene(new Scene(p));
		primaryStage.show();
		startGame(gc,text);
		
		
	}
	
	
	class newGame implements EventHandler<ActionEvent> {
		private GraphicsContext gc;
		private TextField text;
		
		public newGame(GraphicsContext gc, TextField text) {
			this.gc=gc;
			this.text=text;
		}
		@Override
		public void handle(ActionEvent e) {
			//System.out.println("button clicked");
			SNAKE=new Snake(START);
			FOOD=new Food(SNAKE);
			SCORE=0;
			DIR=Direction.DOWN;
			startGame(gc, text);
		}
	}
	
	public void printBoard(GraphicsContext gc) {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, STAGE_WIDTH*SQUARE_SIZE, STAGE_HEIGHT*SQUARE_SIZE);
		
		gc.setFill(Color.GREEN);
		gc.fillOval(FOOD.getX()*SQUARE_SIZE, FOOD.getY()*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
		
		
		int i=0;
		for (int[] square:SNAKE.getSnake()) {
			if (i==0) 
				gc.setFill(Color.BLUE);
			
			else
				gc.setFill(Color.BLACK);
			gc.fillRect(square[0]*SQUARE_SIZE, square[1]*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
			i++;
		}		
	}
	
	public void startGame(GraphicsContext gc, TextField text) {
		PauseTransition wait = new PauseTransition(Duration.seconds(DELAY));
		wait.setOnFinished(new PlayGame(gc,wait, text));
		wait.play();
		
	}
	private class PlayGame implements EventHandler<ActionEvent>{
		
		private GraphicsContext gc;
		private PauseTransition wait;
		private TextField text;

		public PlayGame(GraphicsContext gc, PauseTransition wait, TextField text){
			this.gc=gc;
			this.wait=wait;
			this.text=text;
		}

		@Override
		public void handle(ActionEvent e) {
			ArrayList<int[]> hold =SNAKE.getSnake();
			int[] front= new int[2];
			front[0]=hold.get(0)[0];
			front[1]=hold.get(0)[1];
			
			switch(DIR) {
			
			case UP:
				front[1]-=1;
				break;
				
			case DOWN:
				front[1]+=1;
				break;
				
			case LEFT:
				front[0]-=1;
				break;
				
			case RIGHT:
				front[0]+=1;
				break;
			case DIE:
				wait.stop();
				gc.setFill(Color.RED);
				gc.fillRect(0, 0, STAGE_WIDTH*SQUARE_SIZE, STAGE_HEIGHT*SQUARE_SIZE);
				gc.setFill(Color.BLACK);
				gc.fillText("SCORE: "+Integer.toString(SCORE), (STAGE_WIDTH-1)*SQUARE_SIZE/2, 
						(STAGE_HEIGHT-1)*SQUARE_SIZE/2);
				break;
			}
				
			if (DIR!=Direction.DIE) {
				checkAte(front, text);
				checkCollision(front);
				printBoard(gc);
				wait.playFromStart();
			}
		}
	}
	
	private class moveSnake implements EventHandler<KeyEvent>  {
		private GraphicsContext gc;
	
		public moveSnake(GraphicsContext gc) {
			this.gc=gc;
		}
		@Override
		public void handle(KeyEvent e) {
			e.consume();
			ArrayList<int[]> hold =SNAKE.getSnake();
			int[] front =hold.get(0);
			switch(e.getText()) {
			case "w":
				if(DIR!=Direction.DOWN)
					DIR=Direction.UP;
				break;
			case "a":
				if(DIR!=Direction.RIGHT)
					DIR=Direction.LEFT;
				break;
			case "s":
				if(DIR!=Direction.UP)
					DIR=Direction.DOWN;
				break;
			case "d":
				if(DIR!=Direction.LEFT)
					DIR=Direction.RIGHT;
				break;
			}
			
			
		}
		
	}
	
	public void checkAte(int[] front, TextField text) {
		if (front[0]==FOOD.getX()&&front[1]==FOOD.getY()) {
			FOOD=new Food(SNAKE);
			SNAKE.grow(front);
			SCORE+=1;
			text.clear();
			text.appendText("SCORE: "+Integer.toString(SCORE));
			
		}
		else {
			SNAKE.movement(front);
		}
	}
	
	public void checkCollision(int [] front) {
		ArrayList<int[]> tempSnake=SNAKE.getSnake();
		if (front[0]<0||front[0]>STAGE_WIDTH-1||front[1]<0||front[1]>STAGE_HEIGHT-1) {
			DIR=Direction.DIE;	
		}
		
		for (int i=2; i<tempSnake.size(); i++) {
			if (front[0]==tempSnake.get(i)[0]&&front[1]==tempSnake.get(i)[1]) {
				DIR=Direction.DIE;
				break;
			}
		}
		
	}
}
	

