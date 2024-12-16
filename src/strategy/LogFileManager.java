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
			if (model instanceof DrawingModel) {
				DrawingModel drawingModel = (DrawingModel) model;
				drawingModel.notifyObservers();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Color parseColor(String colorStr) {
		try {
			// Pronađi početak i kraj RGB vrednosti
			int startIdx = colorStr.indexOf("rgb(");
			int endIdx = colorStr.indexOf(")", startIdx);

			if (startIdx == -1 || endIdx == -1) {
				throw new IllegalArgumentException("Invalid color format: " + colorStr);
			}

			// Izdvajanje dela između "rgb(" i ")"
			String rgbValues = colorStr.substring(startIdx + 4, endIdx);

			// Razdvajanje na tri vrednosti (R, G, B)
			String[] rgbParts = rgbValues.split(",");

			if (rgbParts.length != 3) {
				throw new IllegalArgumentException("Invalid RGB format: " + colorStr);
			}

			int r = Integer.parseInt(rgbParts[0].trim());
			int g = Integer.parseInt(rgbParts[1].trim());
			int b = Integer.parseInt(rgbParts[2].trim());

			return new Color(r, g, b);
		} catch (Exception e) {
			throw new IllegalArgumentException("Error parsing color: " + colorStr, e);
		}
	}

	private Point parsePoint(String pointStr) {
		try {
			// Izdvaja samo koordinate tačke, zanemarujući dodatne informacije (kao što su
			// boje)
			String[] parts = pointStr.split(",");

			if (parts.length < 2) {
				throw new IllegalArgumentException("Invalid point format: " + pointStr);
			}

			// Izdvaja samo x i y koordinate
			int x = Integer.parseInt(parts[0].trim());
			int y = Integer.parseInt(parts[1].trim());

			return new Point(x, y);
		} catch (Exception e) {
			throw new IllegalArgumentException("Error parsing point: " + pointStr, e);
		}
	}

	private void recreateShape(String logLine) {
		try {
			if (!logLine.startsWith("Execute: Added")) {
				return;
			}

			String shapeInfo = logLine.substring("Execute: Added ".length());

			if (shapeInfo.startsWith("Point")) {
				recreatePoint(shapeInfo);
			} else if (shapeInfo.startsWith("Line")) {
				recreateLine(shapeInfo);
			} else if (shapeInfo.startsWith("Rectangle")) {
				recreateRectangle(shapeInfo);
			} else if (shapeInfo.startsWith("Circle")) {
				recreateCircle(shapeInfo);
			} else if (shapeInfo.startsWith("Donut")) {
				recreateDonut(shapeInfo);
			} else if (shapeInfo.startsWith("Hexagon")) {
				recreateHexagon(shapeInfo);
			}
		} catch (Exception e) {
			System.out.println("Error processing line: " + logLine);
			e.printStackTrace();
		}
	}

	private void recreatePoint(String pointInfo) {
		try {
			String coords = pointInfo.substring(pointInfo.indexOf("(") + 1, pointInfo.indexOf(")"));
			String[] xy = coords.split(",");

			Point point = new Point(Integer.parseInt(xy[0].trim()), Integer.parseInt(xy[1].trim()));

			Command command = new AddShapeCommand(model, point);
			command.execute();
		} catch (Exception e) {
			System.out.println("Error parsing point: " + pointInfo);
			e.printStackTrace();
		}
	}

	private void recreateLine(String lineInfo) {
		try {
			// Remove the "Line[" prefix
			String content = lineInfo.substring(lineInfo.indexOf("[") + 1);

			// Split by "-->" to get start and end points
			String[] parts = content.split("-->");
			if (parts.length < 2) {
				throw new IllegalArgumentException("Invalid line format: missing '-->' separator");
			}

			// Parse the start and end points
			int start = Integer.parseInt(parts[0].trim());

			// For the end point, we need to remove any trailing content
			String endPart = parts[1];
			int commaIndex = endPart.indexOf(",");
			int end = Integer.parseInt(endPart.substring(0, commaIndex).trim());

			// Create points
			Point startPoint = new Point(start, start);
			Point endPoint = new Point(end, end);

			// Create and add the line with default black color
			Line line = new Line(startPoint, endPoint);
			// No need to set color as it's black by default
			Command command = new AddShapeCommand(model, line);
			command.execute();
		} catch (Exception e) {
			System.out.println("Error parsing line: " + lineInfo);
			e.printStackTrace();
		}
	}

	private void recreateRectangle(String rectangleInfo) {
		try {
			// Extract coordinates
			int centerStart = rectangleInfo.indexOf("(");
			int centerEnd = rectangleInfo.indexOf(",", centerStart);
			int y_end = rectangleInfo.indexOf(",", centerEnd + 1);

			int x = Integer.parseInt(rectangleInfo.substring(centerStart + 1, centerEnd).trim());
			int y = Integer.parseInt(rectangleInfo.substring(centerEnd + 1, y_end).trim());
			Point upperLeft = new Point(x, y);

			// Extract width and height
			int heightStart = rectangleInfo.indexOf("height=") + "height=".length();
			int heightEnd = rectangleInfo.indexOf(",", heightStart);
			if (heightEnd == -1) {
				heightEnd = rectangleInfo.indexOf("]", heightStart);
			}
			int height = Integer.parseInt(rectangleInfo.substring(heightStart, heightEnd).trim());

			int widthStart = rectangleInfo.indexOf("width=") + "width=".length();
			int widthEnd = rectangleInfo.indexOf(",", widthStart);
			if (widthEnd == -1) {
				widthEnd = rectangleInfo.indexOf("]", widthStart);
			}
			int width = Integer.parseInt(rectangleInfo.substring(widthStart, widthEnd).trim());

			// Extract fill color
			int fillColorStart = rectangleInfo.indexOf("fillColor=rgb(") + "fillColor=rgb(".length();
			int fillColorEnd = rectangleInfo.indexOf(")", fillColorStart);
			String[] fillRGB = rectangleInfo.substring(fillColorStart, fillColorEnd).split(",");

			// Clamp RGB values between 0 and 255
			int r = Math.min(255, Math.max(0, Integer.parseInt(fillRGB[0].trim())));
			int g = Math.min(255, Math.max(0, Integer.parseInt(fillRGB[1].trim())));
			int b = Math.min(255, Math.max(0, Integer.parseInt(fillRGB[2].trim())));

			Color fillColor = new Color(r, g, b);

			// Create rectangle with the saved fill color
			Rectangle rectangle = new Rectangle(upperLeft, height, width);
			rectangle.setFillColor(fillColor);

			Command command = new AddShapeCommand(model, rectangle);
			command.execute();
		} catch (Exception e) {
			System.out.println("Error parsing rectangle: " + rectangleInfo);
			e.printStackTrace();
		}
	}

	private void recreateCircle(String shapeInfo) {
		try {
			// Extract center point coordinates
			int centerStart = shapeInfo.indexOf("(");
			int centerEnd = shapeInfo.indexOf(",", centerStart);
			int y_end = shapeInfo.indexOf(",", centerEnd + 1);

			int x = Integer.parseInt(shapeInfo.substring(centerStart + 1, centerEnd).trim());
			int y = Integer.parseInt(shapeInfo.substring(centerEnd + 1, y_end).trim());
			Point center = new Point(x, y);

			// Extract radius
			int radiusStart = shapeInfo.indexOf("radius=") + "radius=".length();
			int radiusEnd = shapeInfo.indexOf(",", radiusStart);
			int radius = Integer.parseInt(shapeInfo.substring(radiusStart, radiusEnd).trim());

			// Extract edge color
			int edgeColorStart = shapeInfo.indexOf("edgeColor=rgb(", radiusEnd) + "edgeColor=rgb(".length();
			int edgeColorEnd = shapeInfo.indexOf(")", edgeColorStart);
			String[] edgeRGB = shapeInfo.substring(edgeColorStart, edgeColorEnd).split(",");
			Color edgeColor = new Color(Integer.parseInt(edgeRGB[0].trim()), Integer.parseInt(edgeRGB[1].trim()),
					Integer.parseInt(edgeRGB[2].trim()));

			// Extract fill color
			int fillColorStart = shapeInfo.indexOf("fillColor=rgb(") + "fillColor=rgb(".length();
			int fillColorEnd = shapeInfo.indexOf(")", fillColorStart);
			String[] fillRGB = shapeInfo.substring(fillColorStart, fillColorEnd).split(",");
			Color fillColor = new Color(Integer.parseInt(fillRGB[0].trim()), Integer.parseInt(fillRGB[1].trim()),
					Integer.parseInt(fillRGB[2].trim()));

			// Create and add the circle
			Circle circle = new Circle(center, radius, edgeColor, fillColor);
			Command command = new AddShapeCommand(model, circle);
			command.execute();
		} catch (Exception e) {
			System.out.println("Error parsing circle: " + shapeInfo);
			e.printStackTrace();
		}
	}

	private void recreateDonut(String donutInfo) {
		try {
			// Extract center point coordinates
			int centerStart = donutInfo.indexOf("(");
			int centerEnd = donutInfo.indexOf(",", centerStart);
			int y_end = donutInfo.indexOf(",", centerEnd + 1);

			int x = Integer.parseInt(donutInfo.substring(centerStart + 1, centerEnd).trim());
			int y = Integer.parseInt(donutInfo.substring(centerEnd + 1, y_end).trim());
			Point center = new Point(x, y);

			// Extract radius
			int radiusStart = donutInfo.indexOf("radius=") + "radius=".length();
			int radiusEnd = donutInfo.indexOf(",", radiusStart);
			int radius = Integer.parseInt(donutInfo.substring(radiusStart, radiusEnd).trim());

			// Extract inner radius
			int innerRadiusStart = donutInfo.indexOf("innerRadius=") + "innerRadius=".length();
			int innerRadiusEnd = donutInfo.indexOf(",", innerRadiusStart);
			int innerRadius = Integer.parseInt(donutInfo.substring(innerRadiusStart, innerRadiusEnd).trim());

			// Extract edge color
			int edgeColorStart = donutInfo.indexOf("edgeColor=rgb(", innerRadiusEnd) + "edgeColor=rgb(".length();
			int edgeColorEnd = donutInfo.indexOf(")", edgeColorStart);
			String[] edgeRGB = donutInfo.substring(edgeColorStart, edgeColorEnd).split(",");
			Color edgeColor = new Color(Integer.parseInt(edgeRGB[0].trim()), Integer.parseInt(edgeRGB[1].trim()),
					Integer.parseInt(edgeRGB[2].trim()));

			// Extract fill color
			int fillColorStart = donutInfo.indexOf("fillColor=rgb(") + "fillColor=rgb(".length();
			int fillColorEnd = donutInfo.indexOf(")", fillColorStart);
			String[] fillRGB = donutInfo.substring(fillColorStart, fillColorEnd).split(",");
			Color fillColor = new Color(Integer.parseInt(fillRGB[0].trim()), Integer.parseInt(fillRGB[1].trim()),
					Integer.parseInt(fillRGB[2].trim()));

			// Create and add the donut
			Donut donut = new Donut(center, radius, innerRadius);
			donut.setEdgeColor(edgeColor);
			donut.setFillColor(fillColor);
			Command command = new AddShapeCommand(model, donut);
			command.execute();
		} catch (Exception e) {
			System.out.println("Error parsing donut: " + donutInfo);
			e.printStackTrace();
		}
	}

	private void recreateHexagon(String hexagonInfo) {
		try {
			// Uklanjanje početnog i završnog dela stringa
			String info = hexagonInfo.substring(hexagonInfo.indexOf("[") + 1, hexagonInfo.lastIndexOf("]"));

			// Razdvajanje stringa prema zarezima, traženje četiri dela
			String[] parts = info.split(",(?=([^()]*\\([^)]*\\))*[^)]*$)");

			if (parts.length != 4) {
				throw new IllegalArgumentException("Invalid hexagon info format: " + hexagonInfo);
			}

			// Parsiranje centra
			String centerPart = parts[0].substring(parts[0].indexOf("(") + 1, parts[0].indexOf(")"));
			String[] centerCoords = centerPart.split(",");
			if (centerCoords.length != 2) {
				throw new IllegalArgumentException("Invalid center coordinates: " + hexagonInfo);
			}
			Point center = new Point(Integer.parseInt(centerCoords[0].trim()),
					Integer.parseInt(centerCoords[1].trim()));

			// Parsiranje radijusa
			int radius = Integer.parseInt(parts[1].split("=")[1].trim());

			// Parsiranje edgeColor
			String edgeColorStr = parts[2].split("=")[1].trim();
			Color edgeColor = parseColor(edgeColorStr);

			// Parsiranje fillColor
			String fillColorStr = parts[3].split("=")[1].trim();
			Color fillColor = parseColor(fillColorStr);

			// Kreiranje Hexagon objekta
			HexagonAdapter hexagon = new HexagonAdapter(center, radius);
			hexagon.setEdgeColor(edgeColor);
			hexagon.setFillColor(fillColor);

			// Kreiranje komande i izvršenje
			Command command = new AddShapeCommand(model, hexagon);
			command.execute();

		} catch (Exception e) {
			System.out.println("Error parsing hexagon: " + hexagonInfo);
			e.printStackTrace();
		}
	}
}