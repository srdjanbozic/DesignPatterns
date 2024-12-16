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
			String pointsPart = lineInfo.substring(lineInfo.indexOf("[") + 1, lineInfo.lastIndexOf("]"));
			String[] points = pointsPart.split("-->");

			String start = points[0].substring(points[0].indexOf("(") + 1, points[0].indexOf(")"));
			String[] startXY = start.split(",");
			Point startPoint = new Point(Integer.parseInt(startXY[0].trim()), Integer.parseInt(startXY[1].trim()));

			String end = points[1].substring(points[1].indexOf("(") + 1, points[1].indexOf(")"));
			String[] endXY = end.split(",");
			Point endPoint = new Point(Integer.parseInt(endXY[0].trim()), Integer.parseInt(endXY[1].trim()));

			Line line = new Line(startPoint, endPoint);
			Command command = new AddShapeCommand(model, line);
			command.execute();
		} catch (Exception e) {
			System.out.println("Error parsing line: " + lineInfo);
			e.printStackTrace();
		}
	}

	private void recreateRectangle(String rectangleInfo) {
		try {
			// Format: "Rectangle[upperLeftPoint=(x,y), width=w, height=h]"
			String info = rectangleInfo.substring(rectangleInfo.indexOf("[") + 1, rectangleInfo.lastIndexOf("]"));
			String[] parts = info.split(", ");

			// Parse point
			String pointPart = parts[0];
			String pointCoords = pointPart.substring(pointPart.indexOf("(") + 1, pointPart.indexOf(")"));
			String[] xy = pointCoords.split(",");
			Point upperLeft = new Point(Integer.parseInt(xy[0].trim()), Integer.parseInt(xy[1].trim()));

			// Parse width and height
			int width = Integer.parseInt(parts[1].split("=")[1]);
			int height = Integer.parseInt(parts[2].split("=")[1]);

			Rectangle rectangle = new Rectangle(upperLeft, height, width);
			Command command = new AddShapeCommand(model, rectangle);
			command.execute();
		} catch (Exception e) {
			System.out.println("Error parsing rectangle: " + rectangleInfo);
			e.printStackTrace();
		}
	}

	private void recreateCircle(String shapeInfo) {
		// Očekivani format:
		// Circle[center=Point(212,152,edgeColor=rgb(0,0,0)),radius=12,edgeColor=rgb(204,0,102),fillColor=rgb(153,255,51)]

		// Prvo, parsiraj centar
		int centerStart = shapeInfo.indexOf("center=Point(");
		int centerEnd = shapeInfo.indexOf("),radius");
		String centerStr = shapeInfo.substring(centerStart + "center=Point(".length(), centerEnd);
		Point center = parsePoint(centerStr); // Pretpostavljamo da je metoda parsePoint ispravno implementirana

		// Parsiraj poluprečnik
		int radiusStart = shapeInfo.indexOf("radius=") + "radius=".length();
		int radiusEnd = shapeInfo.indexOf(",", radiusStart);
		int radius = Integer.parseInt(shapeInfo.substring(radiusStart, radiusEnd).trim());

		// Parsiraj boje
		String edgeColorStr = shapeInfo.substring(shapeInfo.indexOf("edgeColor=rgb("),
				shapeInfo.indexOf("),fillColor"));
		String fillColorStr = shapeInfo.substring(shapeInfo.indexOf("fillColor=rgb("));

		Color edgeColor = parseColor(edgeColorStr);
		Color fillColor = parseColor(fillColorStr);

		// Kreiraj objekat Circle sa svim parametrima
		Circle circle = new Circle(center, radius, edgeColor, fillColor);

		// Dodaj kružnicu u model (ako je model instanca DrawingModel)
		model.add(circle);
	}

	private void recreateDonut(String donutInfo) {
		try {
			// Format: "Donut[center=Point(465,253), radius=566, inner radius=56]"
			String info = donutInfo.substring(donutInfo.indexOf("[") + 1, donutInfo.lastIndexOf("]"));
			String[] parts = info.split(", ");

			// Parse center point
			String centerPart = parts[0];
			String centerCoords = centerPart.substring(centerPart.indexOf("(") + 1, centerPart.indexOf(")"));
			String[] xy = centerCoords.split(",");
			Point center = new Point(Integer.parseInt(xy[0].trim()), Integer.parseInt(xy[1].trim()));

			// Parse outer radius and inner radius
			int radius = Integer.parseInt(parts[1].split("=")[1]);
			int innerRadius = Integer.parseInt(parts[2].split("=")[1]);

			Donut donut = new Donut(center, radius, innerRadius);
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