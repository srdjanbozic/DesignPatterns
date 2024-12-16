package mvc;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import command.log.Logger;
import mvc.controller.DrawingController;
import mvc.model.DrawingModel;
import mvc.view.DrawingView;
import mvc.view.LogPanel;

public class Frame extends JFrame {
	private DrawingModel model;
	private DrawingView view;
	private DrawingController controller;

	private JToggleButton tglSelect;
	private JToggleButton tglPoint;
	private JToggleButton tglLine;
	private JToggleButton tglRectangle;
	private JToggleButton tglCircle;
	private JToggleButton tglDonut;
	private JToggleButton tglHexagon;

	private JButton btnUndo;
	private JButton btnRedo;
	private JButton btnDelete;
	private JButton btnModify;
	private LogPanel logPanel;

	public Frame() {
		setTitle("Design Patterns Drawing App");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);

		initializeComponents();
	}

	private void initializeComponents() {
		// Initialize MVC
		model = new DrawingModel();
		view = new DrawingView(model);
		controller = new DrawingController(model, view);

		// Create main toolbar panel
		JPanel toolbarPanel = new JPanel(new BorderLayout());

		// Create shape buttons panel
		JPanel shapeButtons = new JPanel();

		// Initialize toggle buttons
		tglSelect = new JToggleButton("Select");
		tglPoint = new JToggleButton("Point");
		tglLine = new JToggleButton("Line");
		tglRectangle = new JToggleButton("Rectangle");
		tglCircle = new JToggleButton("Circle");
		tglDonut = new JToggleButton("Donut");
		tglHexagon = new JToggleButton("Hexagon");

		// Add buttons to shape panel
		shapeButtons.add(tglSelect);
		shapeButtons.add(tglPoint);
		shapeButtons.add(tglLine);
		shapeButtons.add(tglRectangle);
		shapeButtons.add(tglCircle);
		shapeButtons.add(tglDonut);
		shapeButtons.add(tglHexagon);

		// Add buttons to button group
		ButtonGroup btnGroup = new ButtonGroup();
		btnGroup.add(tglSelect);
		btnGroup.add(tglPoint);
		btnGroup.add(tglLine);
		btnGroup.add(tglRectangle);
		btnGroup.add(tglCircle);
		btnGroup.add(tglDonut);
		btnGroup.add(tglHexagon);

		// Add action listeners
		tglSelect.addActionListener(e -> controller.setCurrentState(DrawingState.SELECT));
		tglPoint.addActionListener(e -> controller.setCurrentState(DrawingState.POINT));
		tglLine.addActionListener(e -> controller.setCurrentState(DrawingState.LINE));
		tglRectangle.addActionListener(e -> controller.setCurrentState(DrawingState.RECTANGLE));
		tglCircle.addActionListener(e -> controller.setCurrentState(DrawingState.CIRCLE));
		tglDonut.addActionListener(e -> controller.setCurrentState(DrawingState.DONUT));
		tglHexagon.addActionListener(e -> controller.setCurrentState(DrawingState.HEXAGON));

		toolbarPanel.add(shapeButtons, BorderLayout.WEST);

		// Create and add undo/redo panel
		JPanel undoRedoPanel = new JPanel();
		btnUndo = new JButton("Undo");
		btnRedo = new JButton("Redo");
		btnDelete = new JButton("Delete");
		btnModify = new JButton("Modify");
		btnDelete.setEnabled(false);
		btnModify.setEnabled(false);

		btnUndo.addActionListener(e -> controller.undo());
		btnRedo.addActionListener(e -> controller.redo());
		btnDelete.addActionListener(e -> controller.deleteSelected());
		btnModify.addActionListener(e -> controller.modifySelected());

		undoRedoPanel.add(btnUndo);
		undoRedoPanel.add(btnRedo);
		toolbarPanel.add(undoRedoPanel, BorderLayout.EAST);
		ButtonStateObserver buttonObserver = new ButtonStateObserver(btnDelete, btnModify, model);
		model.addObserver(buttonObserver);
		toolbarPanel.add(btnDelete);
		toolbarPanel.add(btnModify);

		// Add panels to frame
		Container contentPane = getContentPane();
		contentPane.add(toolbarPanel, BorderLayout.NORTH);
		contentPane.add(view, BorderLayout.CENTER);
		logPanel = new LogPanel(model);
		contentPane.add(logPanel, BorderLayout.EAST);
		// In Frame's initializeComponents(), after creating logPanel:
		Logger.getInstance().addObserver(logPanel);
	}

	// Add main method
	public static void main(String[] args) {
		Frame frame = new Frame();
		frame.setVisible(true);
	}
}