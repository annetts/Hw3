package ee.ut.cs.ds.client.characters;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class GameFrog extends AGameCharacter {

	private String[] imgFilenames = { "frog-cool.png" };

	public GameFrog() {
		type = "frog";
		loadImages();
	}

	// TODO image loading must be somewhere else.... not every time game updates
	// new images are loeaded.
	private void loadImages() {
		imgFrames = new Image[imgFilenames.length];

		for (int i = 0; i < imgFilenames.length; i++) {
			URL imageUrl = getClass().getClassLoader().getResource(imgFilenames[i]);
			if (imageUrl == null) {
				System.err.println("Failed to load image: " + imageUrl);
			} else {
				try {
					imgFrames[i] = ImageIO.read(imageUrl);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
