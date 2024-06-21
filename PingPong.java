import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.event.* ;
import javafx.scene.control.* ;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.input.* ;
import javafx.scene.media.*;
import javafx.geometry.*;
import javafx.animation.* ;
import javafx.util.* ;
import java.util.* ;
import java.io.* ;


// javac --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.media PingPong.java
// java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.media PingPong

public class PingPong extends Application
{
	private Stage window ;
	private Scene gameScene ;
	private Pane pane ;
	private Timeline timeline ;
	private Circle ball ;
	private Rectangle player_1 ;
	private Rectangle player_2 ;
	private Media media ;
	private MediaPlayer mediaPlayer ;

	private ConfirmationBox theBox = new ConfirmationBox() ;

	private final String PATH = "";//"C:\\Users\\imran\\P.W\\JAVA\\MyBuilds\\PingPong\\Hit.mp3" ;
	private final int windowWidth = 1000 ;
	private final int windowHeight = 700 ;
	private final int boardSize = 100 ;
	private final int boardSpeed = 15 ;
	private final int ballSize = 20 ;
	private int x_speed = 5 ;
	private int y_speed = 5 ;
	private boolean gamePaused = false ;


	public static void main(String[] args) 
	{
		Application.launch(args) ;
	}


	@Override
	public void start(Stage primaryStage)
	{
		window = primaryStage ;
		window.setTitle("P I N G   P O N G") ;
		window.setResizable(false) ;
		window.setWidth(windowWidth) ;
		window.setHeight(windowHeight) ;

		initialize() ;

		pane = new Pane() ;
		pane.getChildren().addAll(ball, player_1, player_2) ;
		pane.setStyle("-fx-background-color : black") ;

		gameScene = new Scene(pane, windowWidth, windowHeight) ;
		gameScene.setOnKeyPressed(event ->
		{
			if(event.getCode() == KeyCode.W && gamePaused == false)
			{
				if(player_1.getLayoutY() > 0)
					player_1.setLayoutY(player_1.getLayoutY() - boardSpeed) ;
			}

			if(event.getCode() == KeyCode.S && gamePaused == false)
			{
				if(player_1.getLayoutY() < (windowHeight - boardSize) - .5 * boardSize)
					player_1.setLayoutY(player_1.getLayoutY() + boardSpeed) ;
			}
			
			if(event.getCode() == KeyCode.UP && gamePaused == false)
			{
				if(player_2.getLayoutY() > 0)
					player_2.setLayoutY(player_2.getLayoutY() - boardSpeed) ;
			}
			
			if(event.getCode() == KeyCode.DOWN && gamePaused == false)
			{
				if(player_2.getLayoutY() < (windowHeight - boardSize) - .5 * boardSize)
					player_2.setLayoutY(player_2.getLayoutY() + boardSpeed) ;
			}

			if(event.getCode() == KeyCode.ESCAPE)
			{
				if(!gamePaused)
				{
					gamePaused = true ;
					timeline.pause() ;
				}
				else
				{
					gamePaused = false ;
					timeline.play() ;
				}
			}
		}) ;

		play() ;

		window.setScene(gameScene) ;
		window.show() ;
	}


	private void play()
	{
		KeyFrame keyFrame = new KeyFrame(Duration.millis(20), e->
		{
			ball.setCenterX(ball.getCenterX() + x_speed) ; 
			ball.setCenterY(ball.getCenterY() + y_speed) ; 

			if(ball.getCenterY() >= 640 || ball.getCenterY() <= ballSize)
			{
				y_speed = -y_speed ;		// invert the speed in the y-axis so that the ball travels in the opposite direction
			}
			else if(ball.getCenterX() == (player_1.getLayoutX() + 30) && ball.getCenterY() >= player_1.getLayoutY() && ball.getCenterY() <= (player_1.getLayoutY() + boardSize))	// check if the ball bounces off player 1's board
			{
//				mediaPlayer.play() ;
				x_speed = -x_speed ;		// invert the speed in the x-axis inorder to move the ball towards player 2
			}
			else if(ball.getCenterX() == (player_2.getLayoutX() - 15) && ball.getCenterY() >= player_2.getLayoutY() && ball.getCenterY() <= (player_2.getLayoutY() + boardSize))	// check if the ball bounces off player 2's board
			{
//				mediaPlayer.play() ;
				x_speed = -x_speed ;		// invert the speed in the x-axis inorder to move the ball towards player 2
			}
			else if(ball.getCenterX() > windowWidth + ballSize || ball.getCenterX() < -ballSize)
			{
				endGame() ;
			}
		}) ;

		timeline = new Timeline(keyFrame) ;
		timeline.setCycleCount(Timeline.INDEFINITE) ;
		timeline.play() ;
	}


	private void initialize()
	{
		int randonNum = (int)(Math.random() * 4) ;

		switch(randonNum) 
		{
			case 1 : x_speed = -x_speed ;
					 y_speed = -y_speed ;
					 break ;

			case 2 : x_speed = -x_speed ;
					 break ;

			case 3 : y_speed = -y_speed ;
					 break ;
		}

		RadialGradient g = new RadialGradient(	0, 0,
												0.35, 0.35,
												0.5,
												true,
												CycleMethod.NO_CYCLE,
												new Stop(0.0, Color.WHITE),
												new Stop(1.0, Color.CORNFLOWERBLUE)) ;

		ball = new Circle() ;					// a circle represents the ball
		ball.setRadius(ballSize) ;
		ball.setCenterX(windowWidth/2) ;
		ball.setCenterY(windowHeight/2) ;
		ball.setFill(g) ;

		player_1 = new Rectangle() ;			// one rectangle for player 1's board
		player_1.setWidth(15) ;
		player_1.setHeight(boardSize) ;
		player_1.setLayoutX(15) ;
		player_1.setLayoutY(windowHeight/3 + 20) ;
		player_1.setFill(Color.RED) ;

		player_2 = new Rectangle() ;			// another rectangle for player 2's board
		player_2.setWidth(15) ;
		player_2.setHeight(boardSize) ;
		player_2.setLayoutX(windowWidth-35) ;
		player_2.setLayoutY(windowHeight/3 + 20) ;
		player_2.setFill(Color.LIME) ;

		// File file = new File(PATH) ;		
		// media = new Media(file.toURI().toString()) ;
		// mediaPlayer = new MediaPlayer(media) ;
		// mediaPlayer.setStartTime(Duration.seconds(3.9)) ;
	}


	private  void endGame()
	{
		Runtime.getRuntime().exit(0) ;			// terminate the program
//		timeline.stop() ;
//		theBox.show("Game Over! Wanna play again?" ,"END", "Yes", "No") ;
	}
}