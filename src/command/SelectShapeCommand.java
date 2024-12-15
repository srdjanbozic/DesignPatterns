package command;

import java.util.ArrayList;
import java.util.List;

import mvc.model.DrawingModel;
import shapes.Shape;

public class SelectShapeCommand implements Command {
	private DrawingModel model;
	private Shape shape;
	private List<Shape> previousSelection;
	private boolean isShiftDown;

	public SelectShapeCommand(DrawingModel model, Shape shape, boolean isShiftDown) {
		this.model = model;
		this.shape = shape;
		this.isShiftDown = isShiftDown;
		this.previousSelection = new ArrayList<>(model.getSelectedShapes());
	}

	@Override
	public void execute() {
		if (!isShiftDown) {
			model.clearSelection();
		}
		model.addToSelection(shape);
	}

	@Override
	public void unexecute() {
		model.clearSelection();
		for (Shape s : previousSelection) {
			model.addToSelection(s);
		}
	}

	@Override
	public String toString() {
		return "Selected " + shape.toString();
	}
}