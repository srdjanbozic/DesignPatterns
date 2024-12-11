package shapes;

import java.awt.Color;
import java.awt.Graphics;

public class Point extends Shape{
	private int x;
	private int y;
	
	public Point() {}
	public Point(int x, int y) 
	{
		this.x = x;
		this.y = y;
	}
	public Point(int x, int y, Color edgeColor) 
	{
		super(edgeColor, edgeColor);
		this.x = x;
		this.y = y;
	}
	@Override
	public void moveBy(int byX, int byY) 
	{
		this.x += byX;
		this.y += byY;
	}
	@Override
	public boolean contains(int x, int y) 
	{
		return this.distance(x, y) <= 3;
	}
	@Override
    public void draw(Graphics g) {
        g.setColor(getEdgeColor());
        g.drawLine(this.x-2, this.y, this.x+2, y);
        g.drawLine(x, this.y-2, x, this.y+2);
        
        if (isSelected()) {
            g.setColor(Color.BLUE);
            g.drawRect(this.x-3, this.y-3, 6, 6);
        }
    }
    
    public double distance(int x2, int y2) {
        double dx = this.x - x2;
        double dy = this.y - y2;
        return Math.sqrt(dx*dx + dy*dy);
    }
    
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    @Override
    public String toString() {
        return "Point(" + x + "," + y + ")";
    }
}

