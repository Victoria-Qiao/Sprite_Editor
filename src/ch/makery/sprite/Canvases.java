package ch.makery.sprite;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

//import javafx.scene.canvas.Canvas;

public class Canvases {
	
	private Canvas firstCanvas, secondCanvas,frontCanvas; // firstCanvas for user viewing, secondCanvas for recording drawing info
	private int imgWidth, imgHeight, gap;             // frontCanvas for showing grids, imgWidth for recording width of img, imgHeight for recording height of img
	private int Width, Height;						// gap for recording the factor which multiplied by imgWidth or imgHeight for better viewing
													// Width and Height are viewing width and height for user after multiplied by gap
	
	public Canvases(int width, int height, SpriteEditorView view) {
		
	
		Width = width * 15;
		Height = height * 15;
		gap = 15;
		
			
		firstCanvas = new Canvas(Width,Height);  // set up the three canvases.
		secondCanvas = new Canvas(width,height);
		frontCanvas = new Canvas(Width,Height);
		
		firstCanvas.setLayoutX(90);
		firstCanvas.setLayoutY(30);
		frontCanvas.setLayoutX(90);
		frontCanvas.setLayoutY(30);
		
		GraphicsContext gc = frontCanvas.getGraphicsContext2D();
		
		GraphicsContext gc1 = firstCanvas.getGraphicsContext2D();
		
		imgWidth = width;
		imgHeight = height;
		
		for(int i = 0; i <=Width; i+=gap)   // draw grids on frontCanvas.
		{
			gc.setStroke(Color.BLACK);
			gc.setLineWidth(0.5);
			gc.strokeLine(i, 0, i, Height);
			
		}
		
		for(int j = 0; j <=Height; j+=gap)
		{
			gc.setStroke(Color.BLACK);
			gc.setLineWidth(0.5);
			gc.strokeLine(0, j, Width, j);
		}
		
		view.tx1.setText("Size: " + width + " * " + height ); // show the size of the img we want using with * height format
		
		view.tx1.setLayoutX(20);
		view.tx1.setLayoutY(400);
		

		
	}
	
	public void changeSize(int width, int height, SpriteEditorView view) {
		GraphicsContext gc1 = firstCanvas.getGraphicsContext2D();
		GraphicsContext gc2 = secondCanvas.getGraphicsContext2D();
		GraphicsContext gc = frontCanvas.getGraphicsContext2D();
		
		gc1.clearRect(0,0,firstCanvas.getWidth(),firstCanvas.getHeight());  // clear the three canvases.
		gc2.clearRect(0,0,secondCanvas.getWidth(),secondCanvas.getHeight());
		gc.clearRect(0,0,frontCanvas.getWidth(),frontCanvas.getHeight());

		
		Width = width * 15;
		Height = height * 15;
		gap = 15;
		
		
		firstCanvas.setWidth(Width);   // resize these canvases
		firstCanvas.setHeight(Height);
		secondCanvas.setWidth(width);
		secondCanvas.setHeight(height);
		frontCanvas.setWidth(Width);
		frontCanvas.setHeight(Height);
		
		for(int i = 0; i <=Width; i+=gap) // draw grids on the frontCanvas
		{
			gc.setStroke(Color.BLACK);
			gc.setLineWidth(0.5);
			gc.strokeLine(i, 0, i, Height);
		}
		
		for(int j = 0; j <=Height; j+=gap)
		{
			gc.setStroke(Color.BLACK);
			gc.setLineWidth(0.5);
			gc.strokeLine(0, j, Width, j);
		}
		
		imgWidth = width;
		imgHeight = height;
		
		view.tx1.setText("Size: " + width + " * " + height ); // show the size of the img we want to operate
		
		view.tx1.setLayoutX(20);
		view.tx1.setLayoutY(400);
		
		
	}
	
	
	public void makeDrawable(ColorPicker colorPicker,SpriteEditorView view, Canvas canvas) {
		
		GraphicsContext gc1 = firstCanvas.getGraphicsContext2D();
		GraphicsContext gc2 = secondCanvas.getGraphicsContext2D();
		PixelWriter pw = gc2.getPixelWriter();
		PixelWriter pw1 = gc1.getPixelWriter();
		
		
		canvas.setOnMouseDragged(
			new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					double x = event.getX();
					double y = event.getY();
					
					int k = (int)x/gap;  // get the pixel position of the img we want to operate
					int j = (int)y/gap;
					
					int px = k*gap;    // get the responding pixel position of firstCanvas for viewing 
					int py = j*gap;
										
					for(int h = 0; h < gap; h++) {     // because in user view, one rectangle represents a pixel, 
						for(int w = 0; w < gap; w++) {  // in user viewing we need to color the whole rectangle 
							pw1.setColor(px, py, colorPicker.getValue()); // which represents the corresponding pixel
							px++;
						}
						py++;
						px = k*gap;
					}
					
					pw.setColor(k,j, colorPicker.getValue()); // color the secondCanvas.
					
//					gc1.setFill(Color.GREY);
//					gc1.strokeRect(k*gap, j*gap, gap, gap);
					
					view.tx.setText("Current cursor postition: " + "(" + k + "," + j +")"); //everytime drag the cursor, show the cursor position (in pixel position)
					
					view.tx.setLayoutX(20);
					view.tx.setLayoutY(420);
					
				}
			}
		);
		
		canvas.setOnMousePressed(
				new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						double x = event.getX();
						double y = event.getY();
						
						int k = (int)x/gap;
						int j = (int)y/gap;
						
						int px = k*gap;
						int py = j*gap;
												
						for(int h = 0; h < gap; h++) {
							for(int w = 0; w < gap; w++) {
								pw1.setColor(px, py, colorPicker.getValue());
								px++;
							}
							px = k*gap;
							py++;
						}
						
						pw.setColor(k,j, colorPicker.getValue());

//						gc1.setFill(Color.GREY);
//						gc1.strokeRect(k*gap, j*gap, gap, gap);
						
						view.tx.setText("Current cursor postition: " + "(" + k + "," + j +")");
						
						view.tx.setLayoutX(20);
						view.tx.setLayoutY(420);
					
					}
				}
		);
		
		canvas.setOnMouseMoved(
				new EventHandler<MouseEvent>() { // everytime move the cursor, show the position of the pixel it represents
					
					public void handle(MouseEvent me) {
						double x = me.getX();
						double y = me.getY();
						
						int k = (int)x/getGap();
						int j = (int)y/getGap();
						view.tx.setText("Current cursor postition: " + "(" + k + "," + j +")");
						
						view.tx.setLayoutX(20);
						view.tx.setLayoutY(420);

						
					}
				}		
		);
		
	}
	
	
	
	public Canvas getFirstCanvas() {
		return firstCanvas;
	}
	
	public Canvas getSecondCanvas() {
		return secondCanvas;
	}
	
	public int getImgWidth() {
		return imgWidth;
	}
	
	public int getImgHeight() {
		return imgHeight;
	}
	
	public int getWidth() {
		return Width;
	}
	
	public int getHeight() {
		return Height;
	}
	
	public int getGap() {
		return gap;
	}
	public Canvas getFrontCanvas() {
		return frontCanvas;
	}

}
