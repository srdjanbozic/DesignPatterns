
package mvc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import command.log.LogObserver;
import command.log.Logger;
import mvc.model.DrawingModel;
import strategy.LogFileManager;
import strategy.SaveManager;

public class LogPanel extends JPanel implements LogObserver {
	private JTextArea logArea;
	private Logger logger;
	private SaveManager saveManager;

	public LogPanel(DrawingModel model) {
		logger = Logger.getInstance();
		saveManager = new LogFileManager(model);
		setLayout(new BorderLayout());

		// Create log text area
		logArea = new JTextArea();
		logArea.setEditable(false);

		// Add scroll support
		JScrollPane scrollPane = new JScrollPane(logArea);
		scrollPane.setPreferredSize(new Dimension(200, 0));

		// Create buttons panel
		JPanel buttonPanel = new JPanel();
		JButton saveButton = new JButton("Save Log");
		JButton loadButton = new JButton("Load Log");

		saveButton.addActionListener(e -> saveLog());
		loadButton.addActionListener(e -> loadLog());

		buttonPanel.add(saveButton);
		buttonPanel.add(loadButton);

		// Add title
		JLabel title = new JLabel("Command Log", SwingConstants.CENTER);
		title.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// Add components to panel
		add(title, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH); // Add this line to show buttons

		setBorder(BorderFactory.createLineBorder(Color.GRAY));
	}

	private void saveLog() {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			String filePath = fileChooser.getSelectedFile().getAbsolutePath();
			saveManager.save(filePath);
		}
	}

	private void loadLog() {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String filePath = fileChooser.getSelectedFile().getAbsolutePath();
			saveManager.load(filePath);
			updateLog();
		}
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
	public void onLogUpdated() {
		updateLog();

	}
}