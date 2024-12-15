package command.log;

import java.util.ArrayList;
import java.util.List;

public class Logger {
	private static Logger instance; // Singleton pattern
	private List<String> commandLog;
	private List<LogObserver> observers;

	private Logger() {
		commandLog = new ArrayList<>();
		observers = new ArrayList<>();
	}

	public void addObserver(LogObserver observer) {
		observers.add(observer);
	}

	public static Logger getInstance() {
		if (instance == null) {
			instance = new Logger();
		}
		return instance;
	}

	public void log(String commandDescription) {
		commandLog.add(commandDescription);
		notifyObservers();
	}

	private void notifyObservers() {
		for (LogObserver observer : observers) {
			observer.onlogUpdated();
		}

	}

	public List<String> getLog() {
		return new ArrayList<>(commandLog); // Return copy of log
	}

	public void clearLog() {
		commandLog.clear();
	}
}