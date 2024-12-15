package command;

import java.util.Stack;

public class CommandInvoker {
	private Stack<Command> undoStack = new Stack<>();
	private Stack<Command> redoStack = new Stack<>();

	public void executeCommand(Command command) {
		command.execute();
		undoStack.push(command);
		redoStack.clear(); // Clear redo stack when new command is executed
	}

	public void undo() {
		if (!undoStack.isEmpty()) {
			Command command = undoStack.pop();
			command.unexecute();
			redoStack.push(command);
		}
	}

	public void redo() {
		if (!redoStack.isEmpty()) {
			Command command = redoStack.pop();
			command.execute();
			undoStack.push(command);
		}
	}

	public boolean isUndoAvailable() {
		return !undoStack.isEmpty();
	}

	public boolean isRedoAvailable() {
		return !redoStack.isEmpty();
	}
}