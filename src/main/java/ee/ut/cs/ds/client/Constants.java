package ee.ut.cs.ds.client;

public class Constants {

	
	public static final String TITLE = " Frogs Catching Flies";
	public static final int CANVAS_WIDTH = 640;
	public static final int UI_CANVAS_WIDTH = 320;
	public static final int CANVAS_HEIGHT = 480;
	public static final int FRAME_RATE = 60;
	public static int grid_height = 10;
	public static int grid_width = 10;
	
	public static int getPixelRatioX() {
		return CANVAS_WIDTH / grid_width;
	}
	
	public static int getPixelRatioY() {
		return CANVAS_HEIGHT / grid_height;
	}
}
