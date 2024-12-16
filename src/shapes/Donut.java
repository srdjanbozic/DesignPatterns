package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Donut extends Circle {
	private int innerRadius;

	public Donut() {
	}

	public Donut(Point center, int radius, int innerRadius) {
		super(center, radius);
		this.innerRadius = innerRadius;
	}

	public Donut(Point center, int radius, int innerRadius, Color edgeColor, Color fillColor) {
		super(center, radius, edgeColor, fillColor);
		this.innerRadius = innerRadius;
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		// Create outer circle shape
		Ellipse2D outer = new Ellipse2D.Double(getCenter().getX() - getRadius(), getCenter().getY() - getRadius(),
				getRadius() * 2, getRadius() * 2);

		// Create inner circle shape
		Ellipse2D inner = new Ellipse2D.Double(getCenter().getX() - innerRadius, getCenter().getY() - innerRadius,
				innerRadius * 2, innerRadius * 2);

		// Create area from the outer circle and subtract inner circle
		Area area = new Area(outer);
		area.subtract(new Area(inner));

		// Fill the donut
		g2d.setColor(getFillColor());
		g2d.fill(area);

		// Draw the edges
		g2d.setColor(getEdgeColor());
		g2d.draw(area);

		if (isSelected()) {
			g2d.setColor(Color.BLUE);
			// Outer circle handles
			g2d.drawRect(getCenter().getX() - 3, getCenter().getY() - 3, 6, 6); // Center
			g2d.drawRect(getCenter().getX() - getRadius() - 3, getCenter().getY() - 3, 6, 6); // Left
			g2d.drawRect(getCenter().getX() + getRadius() - 3, getCenter().getY() - 3, 6, 6); // Right
			g2d.drawRect(getCenter().getX() - 3, getCenter().getY() - getRadius() - 3, 6, 6); // Top
			g2d.drawRect(getCenter().getX() - 3, getCenter().getY() + getRadius() - 3, 6, 6); // Bottom

			// Inner circle handles
			g2d.drawRect(getCenter().getX() - innerRadius - 3, getCenter().getY() - 3, 6, 6); // Inner left
			g2d.drawRect(getCenter().getX() + innerRadius - 3, getCenter().getY() - 3, 6, 6); // Inner right
			g2d.drawRect(getCenter().getX() - 3, getCenter().getY() - innerRadius - 3, 6, 6); // Inner top
			g2d.drawRect(getCenter().getX() - 3, getCenter().getY() + innerRadius - 3, 6, 6); // Inner bottom
		}
	}

	@Override
	public boolean contains(int x, int y) {
		double distance = getCenter().distance(x, y);
		return distance <= getRadius() && distance >= innerRadius;
	}

	public double area() {
		return super.area() - (innerRadius * innerRadius * Math.PI);
	}

	// Getters and setters
	public int getInnerRadius() {
		return innerRadius;
	}

	public void setInnerRadius(int innerRadius) {
		if (innerRadius < 0 || innerRadius >= getRadius()) {
			throw new IllegalArgumentException("Inner radius must be non-negative and less than outer radius");
		}
		this.innerRadius = innerRadius;
	}

	@Override
	public String toString() {
		return String.format(
				"Donut[center=%s,radius=%d,innerRadius=%d,edgeColor=rgb(%d,%d,%d),fillColor=rgb(%d,%d,%d)]",
				getCenter(), getRadius(), innerRadius, getEdgeColor().getRed(), getEdgeColor().getGreen(),
				getEdgeColor().getBlue(), getFillColor().getRed(), getFillColor().getGreen(), getFillColor().getBlue());
	}
}