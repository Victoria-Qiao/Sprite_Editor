package ch.makery.sprite;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Stack;

import javax.imageio.ImageIO;

import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SpriteEditorController {
	
	private Stage stage;
	private Canvases canvases;
	private SpriteEditorView view;
	boolean ifClicked = false;  // this is used to detect whether hide button is clicked
	
	public void open(SpriteEditorView view) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
		File file = fileChooser.showOpenDialog(stage);  // add showopendialog and make a filter of files which is in png
		
		Image image = new Image(file.toURI().toString());  // set up an img object according to the path chosen
		PixelReader reader = image.getPixelReader();
		System.out.println(image.getHeight() + " " + image.getWidth());
		int imageWidth, imageHeight; 
		imageWidth = (int)image.getWidth();   // record the img height and width
		imageHeight = (int)image.getHeight();
		System.out.println(imageWidth + " " + imageHeight);
		
		if(file != null) {
			canvases.changeSize(imageWidth,imageHeight,view); // if chose a file, then change the size which fits to the img opened.
			GraphicsContext gc1 = canvases.getFirstCanvas().getGraphicsContext2D();
			GraphicsContext gc2 = canvases.getSecondCanvas().getGraphicsContext2D();
			PixelWriter pw = gc2.getPixelWriter();
			PixelWriter pw1 = gc1.getPixelWriter();
			
			
			
			for(int i = 0; i < image.getHeight();i++) // read the color info of img and put the color in firstCanvas and secondCanvas
			{
				for(int j = 0; j < image.getWidth(); j++)
				{
					gc1.setFill(reader.getColor(j, i));
					gc1.fillRect(j*canvases.getGap(), i*canvases.getGap(), canvases.getGap(), canvases.getGap());
					pw.setColor(j, i, reader.getColor(j, i));
					
				}
			}
			
			canvases.makeDrawable(view.colorPicker,view,canvases.getFrontCanvas()); // make the canvases drawable
		
		}
	}
	
	
	public void saveas(SpriteEditorView view) {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Image");
		fileChooser.setInitialFileName("Untitled.png");
		File file  = fileChooser.showSaveDialog(stage);
		File fileName = file.getAbsoluteFile();
		final SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		Image image = canvases.getSecondCanvas().snapshot(params, null); // take a snapshot of the secondCanvas, which is the final we edit

		
		if (file != null) {
           try {
               ImageIO.write(SwingFXUtils.fromFXImage(image, null), "PNG", fileName); // write the file as png format	
               
           	} catch (IOException ex) {
               System.out.println(ex.getMessage());
            }
        }
		
	}
	
	
	

	public SpriteEditorController(Canvases canvases, SpriteEditorView view) {
		this.canvases = canvases;
		this.view = view;
		this.view.holder.setContent(view.sp);
		this.view.sp.getChildren().add(canvases.getFirstCanvas());
		this.view.sp.getChildren().add(canvases.getFrontCanvas());
		canvases.getFrontCanvas().toFront();
		int gap = canvases.getGap();
		canvases.makeDrawable(view.colorPicker,view,canvases.getFrontCanvas());
		
		
		this.view.show.setOnAction(
			new EventHandler<ActionEvent>() {   // show the frontCanvas to show the grid, and set ifClicked false 
				public void handle(ActionEvent e) { // which means hide button has not been clicked.
					view.scene.setCursor(Cursor.DEFAULT);
					canvases.getFrontCanvas().setVisible(true);
					ifClicked = false;
				}
			}
				
		);
		
		this.view.hide.setOnAction(
				new EventHandler<ActionEvent>() {  // hide the front Canvas to show the grid, and set ifClicked true
													// which means hide button has been clicked.
					public void handle(ActionEvent e) {
						view.scene.setCursor(Cursor.DEFAULT);
						canvases.getFrontCanvas().setVisible(false);
						canvases.makeDrawable(view.colorPicker,view,canvases.getFirstCanvas());
						ifClicked = true;
					}
				}
					
		);
		
		
		
		this.view.open.setOnAction(
			new EventHandler<ActionEvent>() {
				public void handle(final ActionEvent e) {
					
					Alert alert = new Alert(AlertType.CONFIRMATION); // set up an alert dialog to ask if to save current content on canvas
					alert.setTitle("Confirmation Dialog");
					alert.setHeaderText("You'll leave the current canvas");
					alert.setContentText("Do you want to save it?");
						
					ButtonType yes = new ButtonType("Yes");
					ButtonType no = new ButtonType("No");
					ButtonType cancel = new ButtonType("Cancel",ButtonData.CANCEL_CLOSE);
						
					alert.getButtonTypes().setAll(yes,no,cancel);
					Optional<ButtonType> result = alert.showAndWait();
						
					if(result.get() == yes) {  // if yes, first save then open new img
						saveas(view);
						open(view);
					}
					else if(result.get() == cancel) { // if cancel, close the alert dialog
						e.consume();
					}	
					else {     // if no, open the new img directly without saving current content on canvas
						open(view);
					}
						
				}
			}	
		);
		
		this.view.saveas.setOnAction(
			new EventHandler<ActionEvent>(){
				public void handle(final ActionEvent e) {
					saveas(view);
					}
				}	
				
		);
		
		this.view.set.setOnMouseClicked( // this is for resizing the canvas
				new EventHandler<MouseEvent>(){
					public void handle(final MouseEvent e) {
						Alert alert = new Alert(AlertType.CONFIRMATION); // the alert dialog for asking if to save the current content
						alert.setTitle("Confirmation Dialog");
						alert.setHeaderText("You'll leave the current canvas");
						alert.setContentText("Do you want to save it?");
						
						ButtonType yes = new ButtonType("Yes");
						ButtonType no = new ButtonType("No");
						ButtonType cancel = new ButtonType("Cancel",ButtonData.CANCEL_CLOSE);
						
						alert.getButtonTypes().setAll(yes,no,cancel);
						Optional<ButtonType> result = alert.showAndWait();
						
						if(result.get() == yes) {         // if yes, first save then resize the canvas
							saveas(view);
							Integer iw = Integer.valueOf(view.inputwidth.getText());
							Integer ih = Integer.valueOf(view.inputheight.getText());
							canvases.changeSize(iw,ih,view);	
							canvases.makeDrawable(view.colorPicker,view,canvases.getFrontCanvas());
						}
						else if(result.get() == cancel) {  // if cancel, close the alert dialog
							e.consume();
						}	
						else {								// if no, resize the canvas without saving.
							Integer iw = Integer.valueOf(view.inputwidth.getText());
							Integer ih = Integer.valueOf(view.inputheight.getText());
							canvases.changeSize(iw,ih,view);
							canvases.makeDrawable(view.colorPicker,view,canvases.getFrontCanvas());
						}
						view.holder.setVmax(canvases.getHeight());
						view.holder.setHmax(canvases.getWidth());
						
					}
				}
				
				
		);
		
		
		this.view.pen.setOnMouseClicked(
			new EventHandler<MouseEvent>() {  // this is for drawing.
				
				public void handle(MouseEvent event) {
					view.scene.setCursor(Cursor.DEFAULT);
					Canvas canvas;
					if(ifClicked == true) {   // if hide clicked, make firstCanvas mouse event
						canvas = canvases.getFirstCanvas();
					}
					else {
						canvas = canvases.getFrontCanvas(); // if show clicked, make frontCanvas mouse event
					}
					canvases.makeDrawable(view.colorPicker,view,canvas);
				}
			}
		);
		

		
		
		
		
		this.view.fill.setOnMouseClicked(
				new EventHandler<MouseEvent>() {
					
					public void handle(MouseEvent event) {
							view.scene.setCursor(Cursor.HAND); // when fill is clicked, the cursor becomes a hand
							Canvas canvas;
							if(ifClicked == true) {
								canvas = canvases.getFirstCanvas();// to judge which canvas to operate on by checking ifClicked
							}
							else {
								canvas = canvases.getFrontCanvas();
							}
							
							canvas.setOnMouseDragged( // in fill, I only want mousepressed works, so I set mouse dragged do noting
								new EventHandler<MouseEvent>() {
									public void handle(MouseEvent me) {
										double x = me.getX();
										double y = me.getY();
									}
								}
									
							);
							
							
							canvas.setOnMousePressed(
	
									new EventHandler<MouseEvent>(){
									
										int[][] array = new int[canvases.getImgWidth()][canvases.getImgHeight()]; // create a two dimentional int array to record color status
										GraphicsContext gc1 = canvases.getFirstCanvas().getGraphicsContext2D();
										GraphicsContext gc2 = canvases.getSecondCanvas().getGraphicsContext2D();
											
										public void fill(int x, int y, int origin, Color color) {
											
											PixelWriter pw = gc2.getPixelWriter();
												
											if(array[x][y] != origin) return;
											Stack<Integer> stX = new Stack<Integer>(); // to record seed positions
											Stack<Integer> stY = new Stack<Integer>(); // using scanning line algorithm to fulfill fill function
												
											int x1;
											boolean spanAbove, spanBelow;
												
											stX.push(x);
											stY.push(y);
												
											while(!stX.isEmpty()) {
												x1 = stX.pop(); 
												y = stY.pop();
												while(x1 >= 0 && array[x1][y] == 1) x1--;
												x1++;
												spanAbove = spanBelow = false;
													
												while(x1 < canvases.getImgWidth() && array[x1][y] == origin) {
													array[x1][y] = 2;
													gc1.setFill(color);
													int gap = canvases.getGap();
													gc1.fillRect(x1*gap, y*gap, gap,gap);
													pw.setColor(x1, y, color);
													
													if(!spanAbove && y > 0 && array[x1][y-1] == origin) {
														stX.push(x1);
														stY.push(y-1);
														spanAbove = true;
													}
													else if(spanAbove && y > 0 && array[x1][y-1]!=origin) {
														spanAbove = false;
													}
														
													if(!spanBelow && y < (canvases.getImgHeight()-1) && array[x1][y+1] == origin) {
														stX.push(x1);
														stY.push(y+1);
														spanBelow = true;
													}
													else if(spanBelow && y < (canvases.getImgHeight()-1) && array[x1][y+1] != origin) {
														spanBelow = false;
													}
													x1++;
														
														
														
												}
													
													
										}
												
	
									}
		
											
											public void handle(MouseEvent me) {
												double x = me.getX();   // get the position of cursor pressed
												double y = me.getY();		
												int k = (int)x/canvases.getGap();  // get which pixel to operate on
												int j = (int)y/canvases.getGap();
												
												Image image = canvases.getSecondCanvas().snapshot(new SnapshotParameters(), null); 
												// take a snapshot of the secondCanvas for read the color status
												PixelReader pr = image.getPixelReader();
												Color color = pr.getColor(k,j); // record the color which the pixel the cursor in. 
												
												int h = 0;
												int w = 0;
												
												int height = canvases.getImgHeight();
												int width = canvases.getImgWidth();
												
												for(int i = 0; i <height;i++) {
													for(int l = 0; l <width;l++) {
														if(pr.getColor(l, i).equals(color)==true) {
															array[w][h] = 1;		// record the color status of the img. if the color the same as the position clicked, set 1; else, set 0.
														}
														else {
															array[w][h] = 0;
														}
														w++;
													}
													h++;
													w = 0;
												}
	
												int origin = array[k][j];  // get the value of clicked as origin.
												fill(k,j, origin, view.colorPicker.getValue()); 
												
											
											}
										}
									
										
								);
							}
							
							
						
							
						
					}
					
					
					
				
				
				
		);
		
		
		this.view.eraser.setOnMouseClicked(
				
			new EventHandler<MouseEvent>() {
				public void handle(MouseEvent me) {
					Canvas canvas;
					if(ifClicked == true) {  // if hide is clicked, set the mouse operation on firstCanvas; else, on frontCanvas
						canvas = canvases.getFirstCanvas();
					}
					else {
						canvas = canvases.getFrontCanvas();
					}
					
					view.scene.setCursor(Cursor.CLOSED_HAND); // when eraser is clicked, set the cursor a close hand.
					GraphicsContext gc1 = canvases.getFirstCanvas().getGraphicsContext2D();
					GraphicsContext gc2 = canvases.getSecondCanvas().getGraphicsContext2D();
					PixelWriter pw = gc2.getPixelWriter();
					
					canvas.setOnMouseDragged(
						new EventHandler<MouseEvent>() {
							public void handle(MouseEvent me) {
								double x = me.getX();   // get the position of the cursor dragged
								double y = me.getY();
								int gap = canvases.getGap();
								int k = (int)x/gap;     // calculate which pixel to operate on.
								int j = (int)y/gap;
								
								gc1.clearRect(k*gap, j*gap, gap, gap);
								pw.setColor(k, j, Color.TRANSPARENT); // set the color we want to operate on transparent to reach 
																		// the aim of erasing.
								view.tx.setText("Current cursor postition: " + "(" + k + "," + j +")"); // show the cursor position.
								
								view.tx.setLayoutX(20);
								view.tx.setLayoutY(420);
								
							}
						}
							
					);
					
					canvas.setOnMousePressed(
							new EventHandler<MouseEvent>() {
								public void handle(MouseEvent me) {
									double x = me.getX();
									double y = me.getY();
									int gap = canvases.getGap();
									int k = (int)x/gap;
									int j = (int)y/gap;
									
									gc1.clearRect(k*gap, j*gap, gap, gap);
									
									pw.setColor(k, j, Color.TRANSPARENT);
									
								}
							}
								
						);
					
					
					
					
				}
			}
					
				
				
		);
		
		
		
				
	}
	




	
	
	
	
}
