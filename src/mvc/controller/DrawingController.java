package mvc.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import mvc.DrawingState;
import mvc.dialogs.DlgCircle;
import mvc.dialogs.DlgDonut;
import mvc.dialogs.DlgHexagon;
import mvc.dialogs.DlgRectangle;
import mvc.model.DrawingModel;
import mvc.view.DrawingView;
import shapes.Circle;
import shapes.Donut;
import shapes.Line;
import shapes.Point;
import shapes.Rectangle;
import shapes.Shape;
import shapes.adapter.HexagonAdapter;

public class DrawingController {
	private DrawingModel model;
	private DrawingView view;
	private DrawingState currentState = DrawingState.SELECT;
	private Point startPoint;
	private Color currentEdgeColor = Color.BLACK;
	private Color currentFillColor = Color.WHITE;

	public DrawingController(DrawingModel model, DrawingView view) {
		this.model = model;
		this.view = view;
		initMouseListeners();
	}

	private void initMouseListeners() {
		view.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				handleMouseClick(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
	}

	public void setCurrentState(DrawingState state) {
		this.currentState = state;
		if (state == DrawingState.SELECT) {
			startPoint = null;
		}
	}

	protected void handleMouseClick(MouseEvent e) {
		Point clickPoint = new Point(e.getX(), e.getY());

		switch (currentState) {
		case SELECT:
			handleSelection(e, clickPoint);
			break;
		case POINT:
			handlePointDrawing(clickPoint);
			break;
		case LINE:
			handleLineDrawing(clickPoint);
			break;
		case RECTANGLE:
			handleRectangleDrawing(clickPoint);
			break;
		case CIRCLE:
			handleCircleDrawing(clickPoint);
			break;
		case DONUT:
			handleDonutDrawing(clickPoint);
			break;
		case HEXAGON:
			handleHexagonDrawing(clickPoint);
			break;
		}
		view.repaint();
	}

	private void handleSelection(MouseEvent e, Point clickPoint) {
		if (!e.isShiftDown()) {
			model.clearSelection();
		}

		for (Shape shape : model.getShapes()) {
			if (shape.contains(clickPoint.getX(), clickPoint.getY())) {
				model.addToSelection(shape);
				break;
			}
		}
	}

	private void handlePointDrawing(Point clickPoint) {
		clickPoint.setEdgeColor(currentEdgeColor);
		model.add(clickPoint);
	}

	private void handleLineDrawing(Point clickPoint) {
		if (startPoint == null) {
			startPoint = clickPoint;
		} else {
			Line line = new Line(startPoint, clickPoint);
			line.setEdgeColor(currentEdgeColor);
			model.add(line);
			startPoint = null;
		}
	}

	private void handleRectangleDrawing(Point clickPoint) {
		DlgRectangle dialog = new DlgRectangle((JFrame) view.getTopLevelAncestor());
		dialog.setVisible(true);

		if (dialog.isConfirmed()) {
			Rectangle rectangle = new Rectangle(clickPoint, dialog.getHeight(), dialog.getWidth());
			rectangle.setEdgeColor(dialog.getEdgeColor());
			rectangle.setFillColor(dialog.getFillColor());
			model.add(rectangle);
		}
	}

	private void handleCircleDrawing(Point clickPoint) {
		DlgCircle dialog = new DlgCircle((JFrame) view.getTopLevelAncestor());
		dialog.setVisible(true);

		if (dialog.isConfirmed()) {
			Circle circle = new Circle(clickPoint, dialog.getRadius());
			circle.setEdgeColor(dialog.getEdgeColor());
			circle.setFillColor(dialog.getFillColor());
			model.add(circle);
		}
	}

	private void handleDonutDrawing(Point clickPoint) {
		DlgDonut dialog = new DlgDonut((JFrame) view.getTopLevelAncestor());
		dialog.setVisible(true);

		if (dialog.isConfirmed()) {
			Donut donut = new Donut(clickPoint, dialog.getOuterRadius(), dialog.getInnerRadius());
			donut.setEdgeColor(dialog.getEdgeColor());
			donut.setFillColor(dialog.getFillColor());
			model.add(donut);
		}
	}

	private void handleHexagonDrawing(Point clickPoint) {
		DlgHexagon dialog = new DlgHexagon((JFrame) view.getTopLevelAncestor());
		dialog.setVisible(true);

		if (dialog.isConfirmed()) {
			HexagonAdapter hexagon = new HexagonAdapter(clickPoint, dialog.getRadius());
			hexagon.setEdgeColor(dialog.getEdgeColor());
			hexagon.setFillColor(dialog.getFillColor());
			model.add(hexagon);
		}
	}

	public Color getCurrentEdgeColor() {
		return currentEdgeColor;
	}

	public void setCurrentEdgeColor(Color currentEdgeColor) {
		this.currentEdgeColor = currentEdgeColor;
	}

	public Color getCurrentFillColor() {
		return currentFillColor;
	}

	public void setCurrentFillColor(Color currentFillColor) {
		this.currentFillColor = currentFillColor;
	}
}