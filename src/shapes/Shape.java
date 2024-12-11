package shapes;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Shape implements Moveable, Comparable<Shape>, Cloneable{
	private boolean selected;
	private Color edgeColor;
	private Color fillColor;
	private int zOrder;
	
	public Shape() 
	{
		this.edgeColor = Color.BLACK;
		this.fillColor = Color.WHITE;
	}
	public Shape(Color edgeColor, Color fillColor) 
	{
		this.edgeColor = edgeColor;
		this.fillColor = fillColor;
	}
	
	// Abstract methods
	public abstract boolean contains(int x, int y);
	public abstract void draw(Graphics g);
	
	// Getters and Setters
	public boolean isSelected() 
	{
		return selected;
	}
	public void setSelected(boolean selected) 
	{
		this.selected = selected;
	}
	public Color getEdgeColor() 
	{
		return edgeColor;
	}
	public void setEdgeColor(Color edgeColor) {
        this.edgeColor = edgeColor;
    }
    
    public Color getFillColor() {
        return fillColor;
    }
    
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }
    
    public int getZOrder() {
        return zOrder;
    }
    
    public void setZOrder(int zOrder) {
        this.zOrder = zOrder;
    }
    
    @Override
    public int compareTo(Shape other) {
        return Integer.compare(this.zOrder, other.zOrder);
    }
    
    @Override
    public Shape clone() throws CloneNotSupportedException {
        Shape cloned = (Shape) super.clone();
        cloned.edgeColor = this.edgeColor;
        cloned.fillColor = this.fillColor;
        return cloned;
    }
	
}
