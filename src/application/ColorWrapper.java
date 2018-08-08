package application;

import javafx.scene.paint.Color;
	/**
	 * @author Jonathan Brose
	 */
public class ColorWrapper {
	
	Color color;
	
	public ColorWrapper(Color color) {
		this.color = color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public Color getColor() {
		return color;
	}
	@Override
	public String toString() {
		return color.toString();
	}
}
