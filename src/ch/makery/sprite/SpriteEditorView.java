package ch.makery.sprite;

import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SpriteEditorView {
	
	ColorPicker colorPicker  = new ColorPicker(Color.RED);
	ScrollPane holder; //= new StackPane();
	Button set,fill, pen, eraser,show,hide;
	MenuItem open, saveas, first, second, third;
	TextField inputwidth, inputheight;
	Scene scene;
	Pane root;
	Text tx = new Text();
	Text tx1 = new Text();
	StackPane sp;
	ChoiceBox cb = new ChoiceBox();
	
	public SpriteEditorView(Stage primaryStage){

		
		holder = new ScrollPane();
		root = new Pane();
		
		scene = new Scene(root);
		
		
		
	    
	    
		
		sp = new StackPane();
		
		holder.setLayoutX(250);
		holder.setLayoutY(50);
		holder.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		holder.setVbarPolicy(ScrollBarPolicy.AS_NEEDED); // set scrollpane
				
		colorPicker.setLayoutX(20);
		colorPicker.setLayoutY(200);
		
		set = new Button("Set");
		fill = new Button("Fill");
		pen = new Button("Pen");
		eraser = new Button("Eraser");
		show = new Button("Show");
		hide = new Button("Hide");
		
		Label labelwidth = new Label("Width:");
		Label labelheight = new Label("Height:");
		inputwidth = new TextField("Width");
		inputheight = new TextField("Height");
		
		
		inputwidth.setLayoutX(20);
		inputwidth.setLayoutY(80);
		inputwidth.setMaxWidth(80);
		inputheight.setLayoutX(20);
		inputheight.setLayoutY(150);
		inputheight.setMaxWidth(80);
		show.setLayoutX(20);
		show.setLayoutY(330);
		hide.setLayoutX(80);
		hide.setLayoutY(330);
		
		StackPane sp1 = new StackPane();
		StackPane sp2 = new StackPane();
		
		sp1.getChildren().add(labelwidth);
		sp1.setLayoutX(20);
		sp1.setLayoutY(50);
		sp2.getChildren().add(labelheight);
		sp2.setLayoutX(20);
		sp2.setLayoutY(120);
		set.setLayoutX(20);
		set.setLayoutY(250);
		pen.setLayoutX(70);
		pen.setLayoutY(250);
		fill.setLayoutX(90);
		fill.setLayoutY(290);
		eraser.setLayoutX(20);
		eraser.setLayoutY(290);

		
		
		sp.setStyle("-fx-background-color:white"); // make stack pane background color white for better viewing
		holder.setPrefSize(700,800);
		
		Menu menu = new Menu("File");
		
		open = new MenuItem("Open");
		saveas = new MenuItem("Save As");
		
		menu.getItems().add(open);
		menu.getItems().add(saveas);
		
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().add(menu);
		
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty()); // make menubar width fit the primarystage width
		
		root.getChildren().add(sp1);
		root.getChildren().add(sp2);
		root.getChildren().add(holder);
		root.getChildren().add(colorPicker);
		root.getChildren().add(menuBar);
		root.getChildren().add(set);
		root.getChildren().add(fill);
		root.getChildren().add(pen);
		root.getChildren().add(inputwidth);
		root.getChildren().add(inputheight);
		root.getChildren().add(tx);
		root.getChildren().add(eraser);
		root.getChildren().add(tx1);
		root.getChildren().add(show);
		root.getChildren().add(hide);
		
		
		

		
	}
	
	
}
