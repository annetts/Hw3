package ee.ut.cs.ds.client.characters;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class GameFrog extends AGameCharacter {

	private String[] imgFilenames = {
	         "frog-angry.png",
	         "frog-confused.png",
	         "frog-cool.png"};

	public GameFrog() {
		loadImages();
	}

	private void loadImages() {
		imgFrames = new Image[imgFilenames.length];

		for (int i = 0; i < imgFilenames.length; i++) {
			URL imageUrl = getClass().getClassLoader().getResource(imgFilenames[i]);
			if (imageUrl == null) {
				System.err.println("Failed to load image: " + imageUrl);
			} else {
				try {
					imgFrames[i] = ImageIO.read(imageUrl);
					System.out.println("Loaded image " + imgFilenames[i]);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
