package mvc.dialogs;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class DlgHexagon extends DlgShape {
	private JTextField txtRadius;

	public DlgHexagon(Frame parent) {
		super(parent, "Hexagon Properties");
		setSize(300, 150);
		setLocationRelativeTo(parent);
	}

	@Override
	protected void initializeComponents() {
		super.initializeComponents();
		contentPanel.setLayout(new GridLayout(3, 2, 5, 5));

		contentPanel.add(new JLabel("Radius:"));
		txtRadius = new JTextField(10);
		contentPanel.add(txtRadius);

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

	@Override
	protected boolean validateInput() {
		try {
			int radius = Integer.parseInt(txtRadius.getText());
			if (radius <= 0) {
				JOptionPane.showMessageDialog(this, "Radius must be greater than 0", "Invalid Input",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
			return true;
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Please enter a valid number for radius", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public int getRadius() {
		return Integer.parseInt(txtRadius.getText());
	}
}