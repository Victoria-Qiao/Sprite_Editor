package ch.makery.sprite;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
		
	@Override
	public void start(Stage primaryStage){
		//primaryStage = new Stage();
		primaryStage.setHeight(900); // set up the height and the width of primarystage
		primaryStage.setWidth(1000);
		SpriteEditorView view = new SpriteEditorView(primaryStage); // set up an object of class SpriteEditorView
		Canvases canvases = new Canvases(32,32,view);    // set up an object of class Canvases 
		primaryStage.setScene(view.scene); // put the scene of view to primary stage for showing
		SpriteEditorController controller = new SpriteEditorController(canvases,view);// put canvses and view to SpriteEditorController
		primaryStage.show(); // show the primary stage
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
