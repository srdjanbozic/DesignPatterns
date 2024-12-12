package mvc.model;

import java.util.ArrayList;
import java.util.List;

import shapes.Shape;

public class DrawingModel {
	private List<Shape> shapes = new ArrayList<>();
	private List<Shape> selectedShapes = new ArrayList<>();

	// Methods for shape management
	public void add(Shape shape) {
		shapes.add(shape);
	}

	public void remove(Shape shape) {
		shapes.remove(shape);
		selectedShapes.remove(shape);
	}

	public void addToSelection(Shape shape) {
		if (!selectedShapes.contains(shape)) {
			selectedShapes.add(shape);
			shape.setSelected(true);
		}
	}

	public void removeFromSelection(Shape shape) {
		selectedShapes.remove(shape);
		shape.setSelected(false);
	}

	public void clearSelection() {
		for (Shape shape : selectedShapes) {
			shape.setSelected(false);
		}
		selectedShapes.clear();
	}

	// Getters
	public List<Shape> getShapes() {
		return shapes;
	}

	public List<Shape> getSelectedShapes() {
		return selectedShapes;
	}
}