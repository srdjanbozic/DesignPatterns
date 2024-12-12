package mvc.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class DlgRectangle extends DlgShape {
	private JTextField txtWidth;
	private JTextField txtHeight;

	public DlgRectangle(Frame parent) {
		super(parent, "Rectangle Properties");
		setSize(300, 200);
		setLocationRelativeTo(parent);
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			System.out.println("Content panel dimensions: " + contentPanel.getPreferredSize());
			System.out.println("Button panel dimensions: " + buttonPanel.getPreferredSize());
		}
		super.setVisible(visible);
	}

	@Override
	protected void initializeComponents() {
		super.initializeComponents();
		contentPanel.setPreferredSize(new Dimension(300, 200));
		buttonPanel.setPreferredSize(new Dimension(300, 50));
		contentPanel.setLayout(new GridLayout(4, 2, 5, 5));

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

	@Override
	protected boolean validateInput() {
		try {
			if (txtWidth.getText().trim().isEmpty() || txtHeight.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Width and height fields cannot be empty", "Invalid Input",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}

			int width = Integer.parseInt(txtWidth.getText().trim());
			int height = Integer.parseInt(txtHeight.getText().trim());

			if (width <= 0 || height <= 0) {
				JOptionPane.showMessageDialog(this, "Width and height must be greater than 0", "Invalid Input",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
			return true;
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Please enter valid numbers for width and height", "Invalid Input",
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
