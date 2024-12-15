package command.log;

import java.util.ArrayList;
import java.util.List;

public class Logger {
	private static Logger instance; // Singleton pattern
	private List<String> commandLog;

	private Logger() {
		commandLog = new ArrayList<>();
	}

	public static Logger getInstance() {
		if (instance == null) {
			instance = new Logger();
		}
		return instance;
	}

	public void log(String commandDescription) {
		commandLog.add(commandDescription);
	}

	public List<String> getLog() {
		return new ArrayList<>(commandLog); // Return copy of log
	}

	public void clearLog() {
		commandLog.clear();
	}
}