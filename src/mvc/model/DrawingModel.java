package mvc.model;

import java.util.ArrayList;
import java.util.List;

import shapes.Shape;

public class DrawingModel {
	private List<Shape> shapes = new ArrayList<>();
	private List<Shape> selectedShapes = new ArrayList<>();
	private List<DrawingModelObserver> observers = new ArrayList<>();
	// Methods for shape management

	// Add observer methods
	public void addObserver(DrawingModelObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(DrawingModelObserver observer) {
		observers.remove(observer);
	}

	public void notifyObservers() {
		for (DrawingModelObserver observer : observers) {
			observer.update();
		}
	}

	public void add(Shape shape) {
		shapes.add(shape);
		notifyObservers();

	}

	public void remove(Shape shape) {
		shapes.remove(shape);
		selectedShapes.remove(shape);
		notifyObservers();

	}

	public void addToSelection(Shape shape) {
		if (!selectedShapes.contains(shape)) {
			selectedShapes.add(shape);
			shape.setSelected(true);
			notifyObservers();
		}
	}

	public void removeFromSelection(Shape shape) {
		selectedShapes.remove(shape);
		shape.setSelected(false);
		notifyObservers();
	}

	public void clearSelection() {
		for (Shape shape : selectedShapes) {
			shape.setSelected(false);
		}
		selectedShapes.clear();
		notifyObservers();
	}

	// Getters
	public List<Shape> getShapes() {
		return shapes;
	}

	public List<Shape> getSelectedShapes() {
		return selectedShapes;

	}
}