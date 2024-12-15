package command;

public interface Command {
	void execute();

	void unexecute();

	String toString(); // For logging purposes
}
