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

public class DlgPoint extends DlgShape {
	private JTextField txtX, txtY;
	private Point point;

	public DlgPoint(Frame parent) {
		super(parent, "Modify Point");
		setSize(300, 150);
		setLocationRelativeTo(parent);
	}

	@Override
	protected void initializeComponents() {
		super.initializeComponents();
		contentPanel.setLayout(new GridLayout(3, 2, 5, 5));

		contentPanel.add(new JLabel("X:"));
		txtX = new JTextField(10);
		contentPanel.add(txtX);

		contentPanel.add(new JLabel("Y:"));
		txtY = new JTextField(10);
		contentPanel.add(txtY);

		JButton btnEdgeColor = new JButton("Edge Color");
		btnEdgeColor.addActionListener(e -> {
			Color color = JColorChooser.showDialog(this, "Choose Edge Color", getEdgeColor());
			if (color != null)
				setEdgeColor(color);
		});
		contentPanel.add(btnEdgeColor);
	}

	public void setPoint(Point point) {
		this.point = point;
		txtX.setText(String.valueOf(point.getX()));
		txtY.setText(String.valueOf(point.getY()));
		setEdgeColor(point.getEdgeColor());
	}

	public Point getPoint() {
		return point;
	}

	@Override
	protected boolean validateInput() {
		try {
			int x = Integer.parseInt(txtX.getText().trim());
			int y = Integer.parseInt(txtY.getText().trim());

			point.setX(x);
			point.setY(y);
			point.setEdgeColor(getEdgeColor());

			return true;
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Please enter valid numbers for coordinates", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
}