package mvc.view;

import java.awt.Graphics;

import javax.swing.JPanel;

import mvc.model.DrawingModel;
import shapes.Shape;

public class DrawingView extends JPanel {
	private DrawingModel model;

	public DrawingView(DrawingModel model) {
		this.model = model;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Draw all shapes from the model
		for (Shape shape : model.getShapes()) {
			shape.draw(g);
		}
	}

	public DrawingModel getModel() {
		return model;
	}
}