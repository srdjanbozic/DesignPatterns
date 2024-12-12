package mvc.dialogs;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class DlgDonut extends DlgShape {
	private JTextField txtOuterRadius;
	private JTextField txtInnerRadius;

	public DlgDonut(Frame parent) {
		super(parent, "Donut Properties");
		setSize(300, 200);
		setLocationRelativeTo(parent);
	}

	@Override
	protected void initializeComponents() {
		super.initializeComponents();
		contentPanel.setLayout(new GridLayout(4, 2, 5, 5));

		contentPanel.add(new JLabel("Outer Radius:"));
		txtOuterRadius = new JTextField(10);
		contentPanel.add(txtOuterRadius);

		contentPanel.add(new JLabel("Inner Radius:"));
		txtInnerRadius = new JTextField(10);
		contentPanel.add(txtInnerRadius);

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
			int outerRadius = Integer.parseInt(txtOuterRadius.getText());
			int innerRadius = Integer.parseInt(txtInnerRadius.getText());

			if (outerRadius <= 0 || innerRadius <= 0) {
				JOptionPane.showMessageDialog(this, "Both radii must be greater than 0", "Invalid Input",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if (innerRadius >= outerRadius) {
				JOptionPane.showMessageDialog(this, "Inner radius must be smaller than outer radius", "Invalid Input",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}

			return true;
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Please enter valid numbers for both radii", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public int getOuterRadius() {
		return Integer.parseInt(txtOuterRadius.getText());
	}

	public int getInnerRadius() {
		return Integer.parseInt(txtInnerRadius.getText());
	}
}