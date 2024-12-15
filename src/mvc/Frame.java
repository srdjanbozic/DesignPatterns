package mvc;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import mvc.controller.DrawingController;
import mvc.model.DrawingModel;
import mvc.view.DrawingView;

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

		toolbarPanel.add(shapeButtons, BorderLayout.WEST);

		// Create and add undo/redo panel
		JPanel undoRedoPanel = new JPanel();
		btnUndo = new JButton("Undo");
		btnRedo = new JButton("Redo");

		btnUndo.addActionListener(e -> controller.undo());
		btnRedo.addActionListener(e -> controller.redo());

		undoRedoPanel.add(btnUndo);
		undoRedoPanel.add(btnRedo);
		toolbarPanel.add(undoRedoPanel, BorderLayout.EAST);

		// Add panels to frame
		Container contentPane = getContentPane();
		contentPane.add(toolbarPanel, BorderLayout.NORTH);
		contentPane.add(view, BorderLayout.CENTER);
	}

	// Add main method
	public static void main(String[] args) {
		Frame frame = new Frame();
		frame.setVisible(true);
	}
}