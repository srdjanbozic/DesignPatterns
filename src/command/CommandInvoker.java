package command;

import java.util.Stack;

import command.log.Logger;

public class CommandInvoker {
	private Stack<Command> undoStack = new Stack<>();
	private Stack<Command> redoStack = new Stack<>();
	private Logger logger = Logger.getInstance();

	public void executeCommand(Command command) {
		command.execute();
		undoStack.push(command);
		redoStack.clear(); // Clear redo stack when new command is executed
		logger.log("Execute: " + command.toString());
	}

	public void undo() {
		if (!undoStack.isEmpty()) {
			Command command = undoStack.pop();
			command.unexecute();
			redoStack.push(command);
			logger.log("Undo: " + command.toString());
		}
	}

	public void redo() {
		if (!redoStack.isEmpty()) {
			Command command = redoStack.pop();
			command.execute();
			undoStack.push(command);
			logger.log("Redo: " + command.toString());
		}
	}

	public boolean isUndoAvailable() {
		return !undoStack.isEmpty();
	}

	public boolean isRedoAvailable() {
		return !redoStack.isEmpty();
	}
}