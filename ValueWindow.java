import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

public class ValueWindow extends JFrame {
	private BufferedImage originalImage;
	private GUI.ImagePanel picturePanel;
	private JSlider jSliderBrightness, jSliderSaturation, jSliderContrast, jSliderHue;
	private double brightness;

	public ValueWindow(GUI.ImagePanel imageHolder) {
		picturePanel = imageHolder;
		originalImage = imageHolder.getImage();
		JFrame valueWindow = new JFrame("color values");
		valueWindow.setSize(320, 320);
		valueWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();

		jSliderBrightness = makeTitledSilder("Brightness: 0.0", 0, 200, 0);
		panel.add(jSliderBrightness);
		// Add your sliders here

		valueWindow.add(panel);
		valueWindow.setVisible(true);
	}

	private JSlider makeTitledSilder(String string, int minVal, int maxVal, int val) {

		JSlider slider = new JSlider(JSlider.HORIZONTAL, minVal, maxVal, val);
		slider.setSize(640, 50);
		TitledBorder tb = new TitledBorder(BorderFactory.createEtchedBorder(), string, TitledBorder.LEFT,
				TitledBorder.ABOVE_BOTTOM);
		slider.setBorder(tb);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				JSlider slider = (JSlider) ce.getSource();

				// Here is where the slider values are read and stored
				if (slider == jSliderBrightness) {
					brightness = slider.getValue() - 128;
					TitledBorder border = (TitledBorder) slider.getBorder();
					border.setTitle("Brightness: " + brightness);
				}

				if (originalImage == null) return;
				updatePicture();
			}
		});

		return slider;
	}

	private void updatePicture() {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = originalImage.getRGB(x, y);
				int r = (pixel >> 16) & 0xff;
				int g = (pixel >> 8) & 0xff;
				int b = pixel & 0xff;

				// Apply brightness
				r += brightness;
				g += brightness;
				b += brightness;

				// Apply Contrast tipp: (can be done with brightness together)

				// Apply saturation, tipp: needs grayscale values!

				// Tipp for hue: since this is sin and cos, and they usualy do a rotation
				// around the unit circle, RGB values need to be normalized.

				// Aditional tipp: Java deals degrees internally as radians values!
				// You have to convert them before using sin & cos: Math.toRadians(degrees)

				// Where would you best calculate the sin & cos?

				// Don't forget: when adding to color values or multiplying with
				// factors beyond 1x colors may overflow! Take care of that.

				newImage.setRGB(x, y, (0xFF << 24) | (r << 16) | (g << 8) | b);
			}
		}
		picturePanel.setImage(newImage); // This could be removed with good events/delegates
	}
}
