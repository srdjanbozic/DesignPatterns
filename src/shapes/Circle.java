package shapes;

import java.awt.Color;
import java.awt.Graphics;

public class Circle extends Shape {
	private Point center;
	private int radius;

	public Circle() {
	}

	public Circle(Point center, int radius) {
		this.center = center;
		this.radius = radius;
	}

	public Circle(Point center, int radius, Color edgeColor, Color fillColor) {
		super(edgeColor, fillColor);
		this.center = center;
		this.radius = radius;
	}

	@Override
	public void moveBy(int byX, int byY) {
		center.moveBy(byX, byY);
	}

	@Override
	public boolean contains(int x, int y) {
		return center.distance(x, y) <= radius;
	}

	@Override
	public void draw(Graphics g) {
		// Fill circle
		g.setColor(getFillColor());
		g.fillOval(center.getX() - radius, center.getY() - radius, radius * 2, radius * 2);

		// Draw edge
		g.setColor(getEdgeColor());
		g.drawOval(center.getX() - radius, center.getY() - radius, radius * 2, radius * 2);

		if (isSelected()) {
			g.setColor(Color.BLUE);
			// Draw selection handles at cardinal points
			g.drawRect(center.getX() - 3, center.getY() - 3, 6, 6); // Center
			g.drawRect(center.getX() - radius - 3, center.getY() - 3, 6, 6); // Left
			g.drawRect(center.getX() + radius - 3, center.getY() - 3, 6, 6); // Right
			g.drawRect(center.getX() - 3, center.getY() - radius - 3, 6, 6); // Top
			g.drawRect(center.getX() - 3, center.getY() + radius - 3, 6, 6); // Bottom
		}
	}

	public double area() {
		return radius * radius * Math.PI;
	}

	// Getters and setters
	public Point getCenter() {
		return center;
	}

	public void setCenter(Point center) {
		this.center = center;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		if (radius < 0) {
			throw new IllegalArgumentException("Radius cannot be negative");
		}
		this.radius = radius;
	}

	@Override
	public String toString() {
		return String.format("Circle[center=%s,radius=%d,edgeColor=rgb(%d,%d,%d),fillColor=rgb(%d,%d,%d)]", center,
				radius, getEdgeColor().getRed(), getEdgeColor().getGreen(), getEdgeColor().getBlue(),
				getFillColor().getRed(), getFillColor().getGreen(), getFillColor().getBlue());
	}
}