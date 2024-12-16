package shapes.adapter;

import java.awt.Color;
import java.awt.Graphics;

import hexagon.Hexagon;
import shapes.Point;
import shapes.Shape;

public class HexagonAdapter extends Shape {
	private Hexagon hexagon;

	public HexagonAdapter() {
	}

	public HexagonAdapter(Point center, int radius) {
		this.hexagon = new Hexagon(center.getX(), center.getY(), radius);
	}

	public HexagonAdapter(Point center, int radius, Color edgeColor, Color fillColor) {
		super(edgeColor, fillColor);
		this.hexagon = new Hexagon(center.getX(), center.getY(), radius);
		this.hexagon.setBorderColor(edgeColor);
		this.hexagon.setAreaColor(fillColor);
	}

	@Override
	public void moveBy(int byX, int byY) {
		this.hexagon.setX(this.hexagon.getX() + byX);
		this.hexagon.setY(this.hexagon.getY() + byY);
	}

	@Override
	public boolean contains(int x, int y) {
		return this.hexagon.doesContain(x, y);
	}

	@Override
	public void draw(Graphics g) {
		this.hexagon.paint(g);
	}

	@Override
	public void setSelected(boolean selected) {
		this.hexagon.setSelected(selected);
		super.setSelected(selected);
	}

	@Override
	public void setEdgeColor(Color color) {
		this.hexagon.setBorderColor(color);
		super.setEdgeColor(color);
	}

	@Override
	public void setFillColor(Color color) {
		this.hexagon.setAreaColor(color);
		super.setFillColor(color);
	}

	public Hexagon getHexagon() {
		return this.hexagon;
	}

	public void setHexagon(Hexagon hexagon) {
		this.hexagon = hexagon;
	}

	@Override
	public String toString() {
		return String.format("Hexagon[center=(%d,%d),radius=%d,edgeColor=rgb(%d,%d,%d),fillColor=rgb(%d,%d,%d)]",
				hexagon.getX(), hexagon.getY(), hexagon.getR(), getEdgeColor().getRed(), getEdgeColor().getGreen(),
				getEdgeColor().getBlue(), getFillColor().getRed(), getFillColor().getGreen(), getFillColor().getBlue());
	}
}