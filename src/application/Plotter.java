package application;

import static functions.Utilities.round;

import java.util.ArrayList;

import functions.Function;
import functions.Point;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;

/**
 * Controls a {@link Canvas} and plots things to it.
 * 
 * @author Jonathan Brose
 *
 */
public class Plotter {
	
	private Canvas canvas;
	
	private GraphicsContext g;
	
	private double scale = 0.7;
	
	private double dX = 0, dY = 0;
	
	private static final double UNITS_PER_X_SIDE = 10;
	
	private static final double MIN_SCALE = 0.05;


	public Plotter(Canvas canvas) {
		this.canvas = canvas;
		g = canvas.getGraphicsContext2D();
	}

	/**
	 * Plots the given function to the canvas in the given color. 
	 * 
	 * @param function
	 *            the function
	 * @param color
	 *            the color, in which the function should be plotted
	 */
	public void plotFunction(Function function, Color color) {
		if (function == null || function.isXAxis())
			return;

		double distanceFactor = scale * UNITS_PER_X_SIDE / (canvas.getWidth() / 2);
		double distance = (canvas.getWidth() / 2) / (UNITS_PER_X_SIDE * scale);
		double yShift = canvas.getHeight() / 2;
		boolean was_NaN = false;
		g.setStroke(color);
		g.setLineWidth(2);
		g.setLineCap(StrokeLineCap.ROUND);
		Double functionvalue = 0.0;
		g.beginPath();
		for (int i = 0; i <= canvas.getWidth(); i++) {
			double x = i - (canvas.getWidth() / 2);
			x = round(x * distanceFactor - dX, 6);
			double x2 = round((i + 1 - (canvas.getWidth() / 2)) * distanceFactor - dX, 6);
			functionvalue = function.calculateFunctionValue(x);
			if (i == 0 || was_NaN) {
				was_NaN = false;
				if (shouldBeSkipped(functionvalue, x, x2, function, distance)) {
					was_NaN = true;
					continue;
				} else {
					g.moveTo(i, (-functionvalue - dY) * distance + yShift);
					g.beginPath();
					g.lineTo(i, (-functionvalue - dY) * distance + yShift);
				}
			} else if (shouldBeSkipped(functionvalue, x, x2, function, distance)) {
				was_NaN = true;
				g.stroke();
				g.closePath();
				continue;
			} else
				g.lineTo(i, (-functionvalue - dY) * distance + yShift);

		}
		if (was_NaN) {
			g.beginPath();
			g.moveTo(canvas.getWidth() - 1, 1);
			g.lineTo(canvas.getWidth(), 1);
		}
		g.stroke();
		g.closePath();

	}

	/**
	 * Checks whether the next line should get stroked or skipped.
	 * 
	 * @param functionvalue
	 *            newest function value
	 * @param x
	 *            x-coordinate in the coordinate system
	 * @param function
	 *            function which gets plotted
	 * @param x2
	 *            x-coordinate which will come in the next iteration
	 *            
	 * @param distance
	 * 			the distance between one unit in pixels
	 * @return if stroking should get skipped true, otherwise false.
	 */
	private boolean shouldBeSkipped(Double functionvalue, double x, double x2, Function function,
			double distance) {
		if (!isValid(functionvalue))
			return true;

		if ((-functionvalue - dY) * distance < -canvas.getHeight() / 2
				|| (-functionvalue - dY) * distance > canvas.getHeight() / 2) {

		}
		if (function.calculateFunctionValue(x) > 0 && function.calculateFunctionValue(x2) < 0) {
			if (!isValid(function.calculateFunctionValue(round(x, 1))))
				return true;
			if (!isValid(function.calculateFunctionValue(round(x + round(x - x2, 1), 1))))
				return true;
			if (!isValid(function.calculateFunctionValue(round(x2, 1))))
				return true;
		}
		if (function.calculateFunctionValue(x) < 0 && function.calculateFunctionValue(x2) > 0) {
			if (!isValid(function.calculateFunctionValue(round(x, 1))))
				return true;
			if (!isValid(function.calculateFunctionValue(round(x + round(x - x2, 1), 1))))
				return true;
			if (!isValid(function.calculateFunctionValue(round(x2, 1))))
				return true;
		}
		return false;
	}

	/**
	 * Checks if d is a valid number (if it is infinite or Double.NaN)
	 * 
	 * @param d
	 *           value to get checked
	 * @return if the number is valid true, otherwise false.
	 */
	public boolean isValid(Double d) {
		return !(d.isNaN() || d.isInfinite());
	}

	/**
	 * Plots the in {@link CurveDiscussionController} calculated points onto the graph.
	 * 
	 * @param curveDiscussionController
	 *            the curveDisscusionController
	 * @param color
	 *            color, in which the point will be plotted
	 */
	public void plotSpecialPoints(CurveDiscussionController curveDiscussionController, Color color) {
		if (curveDiscussionController == null)
			return;
		if (!curveDiscussionController.pointsShown())
			return;
		ArrayList<Point> usedPoints = new ArrayList<Point>();
		plotPoints(curveDiscussionController.getExtremePoints(), usedPoints, color);
		plotPoints(curveDiscussionController.getZeros(), usedPoints, color);
		plotPoints(curveDiscussionController.getSaddlePoints(), usedPoints, color);
		plotPoints(curveDiscussionController.getTurningPoints(), usedPoints, color);

	}

	/**
	 * Plots the x and y axis with labeling
	 * 
	 * @see #plotAxes(double, Color, double)
	 * @see #plotAxesLabeling(double, Color, double, double, double, double)
	 */
	public void plotAxes() {
		
		double distance = (canvas.getWidth() / 2) / (UNITS_PER_X_SIDE * scale);
		double maxValueWidth = 25;
		double commaSeperations = 1;
		double fontSize = calculateFontSize();
		double lineWidth = 2;
		Color color = Color.BLACK;
		if (this.scale < 0.25) {
			commaSeperations = 5;
			if (this.scale < 0.08)
				commaSeperations = 15;
		}
		plotAxes(lineWidth, color, distance);
		plotAxesLabeling(lineWidth, color, distance, commaSeperations, fontSize, maxValueWidth);
	}

	/**
	 * Plots the x and y axis.
	 * 
	 * @param lineWidth
	 *            the line width of the axes
	 * @param color
	 *            the color of the axes
	 * @param distance
	 *            distance between one unit in pixels
	 */
	private void plotAxes(double lineWidth, Color color, double distance) {
		g.setLineWidth(lineWidth);
		g.setStroke(color);

		g.strokeLine(0, this.canvas.getHeight() / 2 - dY * distance, this.canvas.getWidth(),
				this.canvas.getHeight() / 2 - dY * distance);

		g.strokeLine(this.canvas.getWidth() / 2 + dX * distance, 0,
				this.canvas.getWidth() / 2 + dX * distance, this.canvas.getHeight());
	}

	/**
	 * plots the labeling for the x and y axes
	 * 
	 * @param lineWidth
	 *           lineWidth of the text
	 * @param color
	 *            the color of the labeling
	 * @param distance
	 *            a coordinate unit in pixels
	 * @param commaSeperations
	 *           how often the number should get split
	 * @param fontSize
	 *           the font size
	 * @param maxValueWidth
	 *           the max width a text can have
	 */
	private void plotAxesLabeling(double lineWidth, Color color, double distance, double commaSeperations,
			double fontSize, double maxValueWidth) {

		double c = 0;
		double width = canvas.getWidth();
		double halfWidth = width / 2;
		double height = canvas.getHeight();
		double halfHeight = height / 2;
		double stepWidth = distance / commaSeperations;
		double fontPosition = 0;
		double root = 0;

		g.setFill(color);
		g.setFont(new Font(fontSize));
		g.setLineWidth(lineWidth);

		// X-Achse -----------------------------------------------------------------
		root = halfWidth + dX * distance;
		fontPosition = halfHeight - shiftY(dY * distance - fontSize);
		c = 0;
		for (double x = root; x <= width; x += stepWidth) {
			g.fillText(calculateC(c), x, fontPosition, maxValueWidth);
			c += 1 / commaSeperations;
		}
		c = -1 / commaSeperations;
		for (double z = root - stepWidth; z >= 0; z -= stepWidth) {
			g.fillText(calculateC(c), z, fontPosition, maxValueWidth);
			c -= 1 / commaSeperations;

		}

		// Y-Achse -----------------------------------------------------------------
		root = halfHeight - dY * distance;
		fontPosition = halfWidth + shiftX(dX * distance + 2);
		c = 0;
		for (double y = root; y >= 0; y -= stepWidth) {
			g.fillText(calculateC(c), fontPosition, y, maxValueWidth);
			c += 1 / commaSeperations;

		}
		c = 1 / commaSeperations;
		for (double y = root + stepWidth; y <= height; y += stepWidth) {
			g.fillText(calculateC(c), fontPosition, y, maxValueWidth);
			c -= 1 / commaSeperations;

		}

	}

	/**
	 * Plots the grid onto the coordinate system
	 * 
	 * @see #plotGrid(double, double, Color)
	 */
	public void plotGrid() {
		if (scale < 0.25) {
			plotGrid(15, 0.4, Color.LIGHTGRAY);
			plotGrid(5, 0.5, Color.web("#969696"));
		} else {
			plotGrid(5, 0.5, Color.LIGHTGRAY);
		}
		plotGrid(1, 1, Color.web("#969696"));
	}

	/**
	 * Plots a grid
	 * 
	 * @param unitFractions
	 *            how many lines per unit the grid should have
	 * @param lineWidth
	 *            the line width
	 * @param color
	 *            the color
	 */
	private void plotGrid(double unitFractions, double lineWidth, Color color) {
		g.setStroke(color);
		g.setLineWidth(lineWidth * calculateLineWidthMultiplier());
		double halfWidth = canvas.getWidth() / 2;
		double distance = halfWidth / (UNITS_PER_X_SIDE * scale);
		double shiftX, shiftY;

		shiftX = dX - (int) (dX);
		shiftX *= distance;
		shiftY = dY - (int) (dY);
		shiftY *= -distance;
		distance /= unitFractions;
		for (double x = halfWidth + shiftX; x <= canvas.getWidth(); x += distance) {
			g.strokeLine(x, 0, x, canvas.getHeight());
		}
		for (double x = halfWidth - distance + shiftX; x >= 0; x -= distance) {
			g.strokeLine(x, 0, x, canvas.getHeight());
		}
		double halfHeight = canvas.getHeight() / 2;
		for (double y = halfHeight + shiftY; y <= canvas.getHeight(); y += distance) {
			g.strokeLine(0, y, canvas.getWidth(), y);
		}
		for (double y = halfHeight - distance + shiftY; y >= 0; y -= distance) {
			g.strokeLine(0, y, canvas.getWidth(), y);
		}
	}

	/**
	 * Zeichnet eine {@link ArrayList} mit {@link Point}en
	 * 
	 * @param points
	 *            the points which should get plotted.
	 * @param usedPoints
	 *            the points, that already where plotted.
	 * @param color
	 *           the color
	 */
	private void plotPoints(ArrayList<Point> points, ArrayList<Point> usedPoints, Color color) {
		double pointRadius = 3;

		if (points == null || points.size() == 0)
			return;

		double distance = (canvas.getWidth() / 2) / (UNITS_PER_X_SIDE * scale);
		double shiftY = canvas.getHeight() / 2;
		double shiftX = canvas.getWidth() / 2;
		double fontSize = calculateFontSize();
		pointRadius *= calculateFontSize() / 14;

		g.setFont(new Font(fontSize));

		for (Point point : points) {
			double yPosition = shiftY - point.getY() * distance - dY * distance - pointRadius * 2;
			int c = wasPointAlreadyUsed(usedPoints, point);
			if (c > 0) {
				yPosition -= fontSize * c;
			} else {
				if (point.getName().equals("(T)")) {
					yPosition += 3 * pointRadius + fontSize;
				}
				// -------Punkt-------
				g.setFill(Color.BLACK);
				g.fillOval(shiftX + point.getX() * distance + dX * distance - pointRadius,
						shiftY - point.getY() * distance - dY * distance - pointRadius, pointRadius * 2,
						pointRadius * 2);
			}
			// ---------Beschriftung----
			g.setFill(color);
			usedPoints.add(point);
			g.fillText(point.getName(),
					shiftX + point.getX() * distance + dX * distance + pointRadius, yPosition);

		}
	}

	/**
	 * Returns the number how often the point was used.
	 * @param usedPoints
	 * 		List with the used points
	 * @param point
	 * 		point to get checked if a similar one is in the list
	 * @return how often the point was used.
	 */
	private int wasPointAlreadyUsed(ArrayList<Point> usedPoints, Point point) {
		int c = 0;
		for (Point usedPoint : usedPoints) {
			if (usedPoint.isEqual(point))
				c++;
		}
		return c;
	}

	/**
	 * Method which limits the shift on the x axis.
	 * 
	 * @param shift
	 *            the original shift
	 * @return the limited shift
	 */
	private double shiftX(double shift) {
		if (shift <= -canvas.getWidth() / 2)
			return -canvas.getWidth() / 2;
		if (shift > canvas.getWidth() / 2 - calculateFontSize() * 2)
			return canvas.getWidth() / 2 - calculateFontSize() * 2;

		return shift;
	}

	/**
	 * Method which limits the shift on the y axis.
	 * 
	 * @param shift
	 *            the original shift
	 * @return the limited shift
	 */
	private double shiftY(double shift) {
		double d = -canvas.getHeight() / 2;
		if (shift < d)
			return d;
		d = canvas.getHeight() / 2 - calculateFontSize();
		if (shift > d)
			return d;

		return shift;
	}

	/**
	 * Converts a double to a String in dependence on the scale.
	 * 
	 * @param c
	 *            value
	 * @return value as String, if the scale is greater than 1.7 the numbers after the comma are completely cut.
	 */
	private String calculateC(double c) {
		c = round(c, 2);
		if (scale > 1.7)
			return "" + (int) c;
		return "" + c;
	}

	private double calculateFontSize() {

		if (scale < 0.5)
			return 17;
		if (scale > 1.2) {
			double s = 14 - 2 * scale;
			if (s >= 4)
				return s;
			return 4;
		}
		return 14;
	}

	/**
	 * Calculates a multiplier for the line width in dependence on the scale
	 * 
	 * @return multiplier
	 */
	private double calculateLineWidthMultiplier() {
		return (1 / (scale * 3));
	}

	public void deleteImage() {
		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	

	
	public void setScale(double scale) {
		if (scale >= MIN_SCALE) {
			this.scale = scale;
		} else
			setScale(MIN_SCALE);
	}


	public void addToDX(double addition) {
		this.dX += addition;
	}

	
	public void addToDY(double addition) {
		this.dY += addition;
	}

	/**
	 * Adds the addition to the scale. Note: the addition is scaled in dependence of scale.
	 * 
	 * @param addition
	 *           the addition
	 */
	public void addToScale(double addition) {
		addition /= 1000 / (this.scale);
		setScale(this.scale + addition);
	}

	public double getdX() {
		return dX;
	}

	public void setdX(double dX) {
		this.dX = dX;
	}

	public double getdY() {
		return dY;
	}

	public void setdY(double dY) {
		this.dY = dY;
	}

	public double getScale() {
		return scale;
	}

	
}
