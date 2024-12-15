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
		// Replace old shape with new shape in model
		int index = model.getShapes().indexOf(oldState);
		if (index != -1) {
			model.getShapes().set(index, newState);
		}
	}

	@Override
	public void unexecute() {
		// Restore old shape
		int index = model.getShapes().indexOf(newState);
		if (index != -1) {
			model.getShapes().set(index, oldState);
		}
	}

	@Override
	public String toString() {
		return "Updated " + oldState.toString();
	}
}