package mvc;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.ButtonGroup;
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

		// Create toolbar with shape buttons
		JPanel toolbar = new JPanel();
		ButtonGroup btnGroup = new ButtonGroup();

		tglSelect = new JToggleButton("Select");
		tglPoint = new JToggleButton("Point");
		tglLine = new JToggleButton("Line");
		tglRectangle = new JToggleButton("Rectangle");
		tglCircle = new JToggleButton("Circle");
		tglDonut = new JToggleButton("Donut");
		tglHexagon = new JToggleButton("Hexagon");

		btnGroup.add(tglSelect);
		btnGroup.add(tglPoint);
		btnGroup.add(tglLine);
		btnGroup.add(tglRectangle);
		btnGroup.add(tglCircle);
		btnGroup.add(tglDonut);
		btnGroup.add(tglHexagon);

		toolbar.add(tglSelect);
		toolbar.add(tglPoint);
		toolbar.add(tglLine);
		toolbar.add(tglRectangle);
		toolbar.add(tglCircle);
		toolbar.add(tglDonut);
		toolbar.add(tglHexagon);

		// Add components to frame
		Container contentPane = getContentPane();
		contentPane.add(toolbar, BorderLayout.NORTH);
		contentPane.add(view, BorderLayout.CENTER);

		// Add button action listeners
		tglSelect.addActionListener(e -> controller.setCurrentState(DrawingState.SELECT));
		tglPoint.addActionListener(e -> controller.setCurrentState(DrawingState.POINT));
		tglLine.addActionListener(e -> controller.setCurrentState(DrawingState.LINE));
		tglRectangle.addActionListener(e -> controller.setCurrentState(DrawingState.RECTANGLE));
		tglCircle.addActionListener(e -> controller.setCurrentState(DrawingState.CIRCLE));
		tglDonut.addActionListener(e -> controller.setCurrentState(DrawingState.DONUT));
		tglHexagon.addActionListener(e -> controller.setCurrentState(DrawingState.HEXAGON));
	}

	public static void main(String[] args) {
		Frame frame = new Frame();
		frame.setVisible(true);
	}
}