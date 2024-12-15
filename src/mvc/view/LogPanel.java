package mvc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import command.log.LogObserver;
import command.log.Logger;

public class LogPanel extends JPanel implements LogObserver {
	private JTextArea logArea;
	private Logger logger;

	public LogPanel() {
		logger = Logger.getInstance();
		setLayout(new BorderLayout());

		// Create log text area
		logArea = new JTextArea();
		logArea.setEditable(false); // Make it read-only

		// Add scroll support
		JScrollPane scrollPane = new JScrollPane(logArea);
		scrollPane.setPreferredSize(new Dimension(200, 0)); // Fixed width, variable height

		// Add title
		JLabel title = new JLabel("Command Log", SwingConstants.CENTER);
		title.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		add(title, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);

		setBorder(BorderFactory.createLineBorder(Color.GRAY));
	}

	public void updateLog() {
		StringBuilder sb = new StringBuilder();
		for (String logEntry : logger.getLog()) {
			sb.append(logEntry).append("\n");
		}
		logArea.setText(sb.toString());
		logArea.setCaretPosition(logArea.getDocument().getLength()); // Scroll to bottom
	}

	@Override
	public void onlogUpdated() {
		updateLog();

	}
}