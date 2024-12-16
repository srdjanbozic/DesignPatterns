package command;

import mvc.model.DrawingModel;
import shapes.Shape;

public class UpdateShapeCommand implements Command {
	private Shape oldState;
	private Shape newState;
	private DrawingModel model;

	public UpdateShapeCommand(DrawingModel model, Shape oldState, Shape newState) {
		this.model = model;
		this.oldState = oldState;
		this.newState = newState;
	}

	@Override
	public void execute() {
		// Update the shape's properties
		oldState.setEdgeColor(newState.getEdgeColor());
		if (oldState instanceof FilledShape && newState instanceof FilledShape) {
			((FilledShape) oldState).setFillColor(((FilledShape) newState).getFillColor());
		}
		// Add other property updates based on shape type
	}

	@Override
	public void unexecute() {
		// Restore the old state
		// This would need specific implementation based on your shape properties
	}

	@Override
	public String toString() {
		return "Modified " + oldState.toString();
	}
}