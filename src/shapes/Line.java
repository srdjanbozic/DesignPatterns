package shapes;

import java.awt.Color;
import java.awt.Graphics;

public class Line extends Shape {
    private Point startPoint;
    private Point endPoint;
    
    public Line() {
    }
    
    public Line(Point startPoint, Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }
    
    public Line(Point startPoint, Point endPoint, Color color) {
        super(color, color); // Line only needs edge color
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }
    
    @Override
    public void moveBy(int byX, int byY) {
        startPoint.moveBy(byX, byY);
        endPoint.moveBy(byX, byY);
    }
    
    @Override
    public boolean contains(int x, int y) {
        // Calculate distance from point to line
        double distance = getDistanceFromPoint(x, y);
        return distance <= 3;
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(getEdgeColor());
        g.drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
        
        if (isSelected()) {
            g.setColor(Color.BLUE);
            g.drawRect(startPoint.getX() - 3, startPoint.getY() - 3, 6, 6);
            g.drawRect(endPoint.getX() - 3, endPoint.getY() - 3, 6, 6);
            // Draw selection at middle point
            Point middle = middlePoint();
            g.drawRect(middle.getX() - 3, middle.getY() - 3, 6, 6);
        }
    }
    
    public double length() {
        return startPoint.distance(endPoint.getX(), endPoint.getY());
    }
    
    public Point middlePoint() {
        int middleX = (startPoint.getX() + endPoint.getX()) / 2;
        int middleY = (startPoint.getY() + endPoint.getY()) / 2;
        return new Point(middleX, middleY);
    }
    
    private double getDistanceFromPoint(int x, int y) {
        double numerator = Math.abs((endPoint.getY() - startPoint.getY()) * x - 
                                  (endPoint.getX() - startPoint.getX()) * y + 
                                   endPoint.getX() * startPoint.getY() - 
                                   endPoint.getY() * startPoint.getX());
        
        double denominator = Math.sqrt(Math.pow(endPoint.getY() - startPoint.getY(), 2) + 
                                     Math.pow(endPoint.getX() - startPoint.getX(), 2));
        
        return numerator / denominator;
    }
    
    // Getters and setters
    public Point getStartPoint() {
        return startPoint;
    }
    
    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }
    
    public Point getEndPoint() {
        return endPoint;
    }
    
    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }
    
    @Override
    public String toString() {
        return "Line[" + startPoint + "-->" + endPoint + "]";
    }
}