package mvc.dialogs;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import shapes.Point;
import shapes.Rectangle;

public class DlgRectangle extends DlgShape {
	private JTextField txtX, txtY, txtWidth, txtHeight;
	private Rectangle rectangle;

	public DlgRectangle(Frame parent) {
		super(parent, "Modify Rectangle");
		setSize(300, 250);
		setLocationRelativeTo(parent);
	}

	@Override
	protected void initializeComponents() {
		super.initializeComponents();
		contentPanel.setLayout(new GridLayout(6, 2, 5, 5));

		contentPanel.add(new JLabel("Upper Left Point X:"));
		txtX = new JTextField(10);
		contentPanel.add(txtX);

		contentPanel.add(new JLabel("Upper Left Point Y:"));
		txtY = new JTextField(10);
		contentPanel.add(txtY);

		contentPanel.add(new JLabel("Width:"));
		txtWidth = new JTextField(10);
		contentPanel.add(txtWidth);

		contentPanel.add(new JLabel("Height:"));
		txtHeight = new JTextField(10);
		contentPanel.add(txtHeight);

		JButton btnEdgeColor = new JButton("Edge Color");
		btnEdgeColor.addActionListener(e -> {
			Color color = JColorChooser.showDialog(this, "Choose Edge Color", getEdgeColor());
			if (color != null)
				setEdgeColor(color);
		});
		contentPanel.add(btnEdgeColor);

		JButton btnFillColor = new JButton("Fill Color");
		btnFillColor.addActionListener(e -> {
			Color color = JColorChooser.showDialog(this, "Choose Fill Color", getFillColor());
			if (color != null)
				setFillColor(color);
		});
		contentPanel.add(btnFillColor);
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
		txtX.setText(String.valueOf(rectangle.getUpperLeftPoint().getX()));
		txtY.setText(String.valueOf(rectangle.getUpperLeftPoint().getY()));
		txtWidth.setText(String.valueOf(rectangle.getWidth()));
		txtHeight.setText(String.valueOf(rectangle.getHeight()));
		setEdgeColor(rectangle.getEdgeColor());
		setFillColor(rectangle.getFillColor());
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	@Override
	protected boolean validateInput() {
		try {
			int x = Integer.parseInt(txtX.getText().trim());
			int y = Integer.parseInt(txtY.getText().trim());
			int width = Integer.parseInt(txtWidth.getText().trim());
			int height = Integer.parseInt(txtHeight.getText().trim());

			if (width <= 0 || height <= 0) {
				JOptionPane.showMessageDialog(this, "Width and height must be greater than 0", "Invalid Input",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}

			rectangle.setUpperLeftPoint(new Point(x, y));
			rectangle.setWidth(width);
			rectangle.setHeight(height);
			rectangle.setEdgeColor(getEdgeColor());
			rectangle.setFillColor(getFillColor());

			return true;
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Please enter valid numbers", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public int getWidth() {
		try {
			if (!isConfirmed() || txtWidth.getText().trim().isEmpty()) {
				return 0;
			}
			return Integer.parseInt(txtWidth.getText().trim());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public int getHeight() {
		try {
			if (!isConfirmed() || txtHeight.getText().trim().isEmpty()) {
				return 0;
			}
			return Integer.parseInt(txtHeight.getText().trim());
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}
