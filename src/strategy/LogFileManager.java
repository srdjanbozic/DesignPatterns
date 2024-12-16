package strategy;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import command.AddShapeCommand;
import command.Command;
import command.log.Logger;
import mvc.model.DrawingModel;
import shapes.Circle;
import shapes.Donut;
import shapes.Line;
import shapes.Point;
import shapes.Rectangle;
import shapes.adapter.HexagonAdapter;

public class LogFileManager implements SaveManager {
	private DrawingModel model;

	public LogFileManager(DrawingModel model) {
		this.model = model;
	}

	@Override
	public void save(String filePath) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			List<String> log = Logger.getInstance().getLog();
			for (String logEntry : log) {
				writer.write(logEntry);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void load(String filePath) {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			List<String> loadedLog = new ArrayList<>();
			model.getShapes().clear();

			String line;
			while ((line = reader.readLine()) != null) {
				loadedLog.add(line);
				if (line.startsWith("Execute: Added")) {
					recreateShape(line);
				}
			}
			Logger.getInstance().setLog(loadedLog);
			// Make sure view is repainted
			if (model instanceof DrawingModel) {
				DrawingModel drawingModel = (DrawingModel) model;
				drawingModel.notifyObservers(); // If you have observer pattern implemented
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Color parseColor(String colorStr) {
		try {
			String[] rgb = colorStr.substring(colorStr.indexOf("(") + 1, colorStr.indexOf(")")).split(",");
			return new Color(Integer.parseInt(rgb[0].trim()), Integer.parseInt(rgb[1].trim()),
					Integer.parseInt(rgb[2].trim()));
		} catch (Exception e) {
			return Color.BLACK; // Default color if parsing fails
		}
	}

	private void recreatePoint(String pointInfo) {
		try {
			// Format: "Point(x,y,edgeColor=rgb(r,g,b))"
			String[] parts = pointInfo.split(",edgeColor=");
			String coords = parts[0].substring(parts[0].indexOf("(") + 1);
			String[] xy = coords.split(",");

			Point point = new Point(Integer.parseInt(xy[0].trim()), Integer.parseInt(xy[1].trim()));
			point.setEdgeColor(parseColor(parts[1]));

			Command command = new AddShapeCommand(model, point);
			command.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void recreateLine(String lineInfo) {
		try {
			// Format: "Line[Point(x1,y1)-->Point(x2,y2),edgeColor=rgb(r,g,b)]"
			String[] mainParts = lineInfo.split(",edgeColor=");
			String[] points = mainParts[0].split("-->");

			// Parse start point
			String startPointStr = points[0].substring(points[0].indexOf("(") + 1, points[0].indexOf(")"));
			String[] startXY = startPointStr.split(",");
			Point startPoint = new Point(Integer.parseInt(startXY[0].trim()), Integer.parseInt(startXY[1].trim()));

			// Parse end point
			String endPointStr = points[1].substring(points[1].indexOf("(") + 1, points[1].indexOf(")"));
			String[] endXY = endPointStr.split(",");
			Point endPoint = new Point(Integer.parseInt(endXY[0].trim()), Integer.parseInt(endXY[1].trim()));

			Line line = new Line(startPoint, endPoint);
			line.setEdgeColor(parseColor(mainParts[1].replace("]", "")));

			Command command = new AddShapeCommand(model, line);
			command.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void recreateRectangle(String rectangleInfo) {
		try {
			// Format:
			// "Rectangle[Point(x,y),width=w,height=h,edgeColor=rgb(r,g,b),fillColor=rgb(r,g,b)]"
			String[] parts = rectangleInfo.substring(rectangleInfo.indexOf("[") + 1, rectangleInfo.length() - 1)
					.split(",");

			// Parse point
			String pointStr = parts[0];
			String[] xy = pointStr.substring(pointStr.indexOf("(") + 1, pointStr.indexOf(")")).split(",");
			Point upperLeft = new Point(Integer.parseInt(xy[0].trim()), Integer.parseInt(xy[1].trim()));

			// Parse width and height
			int width = Integer.parseInt(parts[1].split("=")[1]);
			int height = Integer.parseInt(parts[2].split("=")[1]);

			Rectangle rectangle = new Rectangle(upperLeft, height, width);
			rectangle.setEdgeColor(parseColor(parts[3].split("=")[1]));
			rectangle.setFillColor(parseColor(parts[4].split("=")[1]));

			Command command = new AddShapeCommand(model, rectangle);
			command.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void recreateCircle(String circleInfo) {
		try {
			// Format:
			// "Circle[center=Point(x,y),radius=r,edgeColor=rgb(r,g,b),fillColor=rgb(r,g,b)]"
			String[] parts = circleInfo.substring(circleInfo.indexOf("[") + 1, circleInfo.length() - 1).split(",");

			// Parse center point
			String centerStr = parts[0].substring(parts[0].indexOf("(") + 1, parts[0].indexOf(")"));
			String[] xy = centerStr.split(",");
			Point center = new Point(Integer.parseInt(xy[0].trim()), Integer.parseInt(xy[1].trim()));

			// Parse radius
			int radius = Integer.parseInt(parts[1].split("=")[1]);

			Circle circle = new Circle(center, radius);
			circle.setEdgeColor(parseColor(parts[2].split("=")[1]));
			circle.setFillColor(parseColor(parts[3].split("=")[1]));

			Command command = new AddShapeCommand(model, circle);
			command.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void recreateDonut(String donutInfo) {
		try {
			// Format:
			// "Donut[center=Point(x,y),radius=r,innerRadius=ir,edgeColor=rgb(r,g,b),fillColor=rgb(r,g,b)]"
			String[] parts = donutInfo.substring(donutInfo.indexOf("[") + 1, donutInfo.length() - 1).split(",");

			// Parse center point
			String centerStr = parts[0].substring(parts[0].indexOf("(") + 1, parts[0].indexOf(")"));
			String[] xy = centerStr.split(",");
			Point center = new Point(Integer.parseInt(xy[0].trim()), Integer.parseInt(xy[1].trim()));

			// Parse radii
			int radius = Integer.parseInt(parts[1].split("=")[1]);
			int innerRadius = Integer.parseInt(parts[2].split("=")[1]);

			Donut donut = new Donut(center, radius, innerRadius);
			donut.setEdgeColor(parseColor(parts[3].split("=")[1]));
			donut.setFillColor(parseColor(parts[4].split("=")[1]));

			Command command = new AddShapeCommand(model, donut);
			command.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void recreateHexagon(String hexagonInfo) {
		try {
			// Format:
			// "Hexagon[center=(x,y),radius=r,edgeColor=rgb(r,g,b),fillColor=rgb(r,g,b)]"
			String[] parts = hexagonInfo.substring(hexagonInfo.indexOf("[") + 1, hexagonInfo.length() - 1).split(",");

			// Parse center coordinates
			String centerStr = parts[0].substring(parts[0].indexOf("(") + 1, parts[0].indexOf(")"));
			String[] xy = centerStr.split(",");
			Point center = new Point(Integer.parseInt(xy[0].trim()), Integer.parseInt(xy[1].trim()));

			// Parse radius
			int radius = Integer.parseInt(parts[1].split("=")[1]);

			HexagonAdapter hexagon = new HexagonAdapter(center, radius);
			hexagon.setEdgeColor(parseColor(parts[2].split("=")[1]));
			hexagon.setFillColor(parseColor(parts[3].split("=")[1]));

			Command command = new AddShapeCommand(model, hexagon);
			command.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}