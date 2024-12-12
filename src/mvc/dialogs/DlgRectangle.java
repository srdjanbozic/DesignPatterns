package mvc.dialogs;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class DlgRectangle extends DlgShape {
	private JTextField txtWidth, txtHeight;

	public DlgRectangle(Frame parent) {
		super(parent, "Rectangle Properties");
		setSize(300, 200);
		setLocationRelativeTo(parent);
	}

	@Override
	protected void initializeComponents() {
		super.initializeComponents();
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
			int width = Integer.parseInt(txtWidth.getText());
			int height = Integer.parseInt(txtHeight.getText());
			return width > 0 && height > 0;
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Please enter valid numbers for width and height", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public int getWidth() {
		return Integer.parseInt(txtWidth.getText());
	}

	public int getHeight() {
		return Integer.parseInt(txtHeight.getText());
	}
}