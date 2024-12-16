package mvc.dialogs;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import shapes.Line;
import shapes.Point;

public class DlgLine extends DlgShape {
	private JTextField txtStartX, txtStartY, txtEndX, txtEndY;
	private Line line;

	public DlgLine(Frame parent) {
		super(parent, "Modify Line");
		setSize(300, 200);
		setLocationRelativeTo(parent);
	}

	@Override
	protected void initializeComponents() {
		super.initializeComponents();
		contentPanel.setLayout(new GridLayout(5, 2, 5, 5));

		contentPanel.add(new JLabel("Start Point X:"));
		txtStartX = new JTextField(10);
		contentPanel.add(txtStartX);

		contentPanel.add(new JLabel("Start Point Y:"));
		txtStartY = new JTextField(10);
		contentPanel.add(txtStartY);

		contentPanel.add(new JLabel("End Point X:"));
		txtEndX = new JTextField(10);
		contentPanel.add(txtEndX);

		contentPanel.add(new JLabel("End Point Y:"));
		txtEndY = new JTextField(10);
		contentPanel.add(txtEndY);

		JButton btnEdgeColor = new JButton("Edge Color");
		btnEdgeColor.addActionListener(e -> {
			Color color = JColorChooser.showDialog(this, "Choose Edge Color", getEdgeColor());
			if (color != null)
				setEdgeColor(color);
		});
		contentPanel.add(btnEdgeColor);
	}

	public void setLine(Line line) {
		this.line = line;
		txtStartX.setText(String.valueOf(line.getStartPoint().getX()));
		txtStartY.setText(String.valueOf(line.getStartPoint().getY()));
		txtEndX.setText(String.valueOf(line.getEndPoint().getX()));
		txtEndY.setText(String.valueOf(line.getEndPoint().getY()));
		setEdgeColor(line.getEdgeColor());
	}

	public Line getLine() {
		return line;
	}

	@Override
	protected boolean validateInput() {
		try {
			int startX = Integer.parseInt(txtStartX.getText().trim());
			int startY = Integer.parseInt(txtStartY.getText().trim());
			int endX = Integer.parseInt(txtEndX.getText().trim());
			int endY = Integer.parseInt(txtEndY.getText().trim());

			line.setStartPoint(new Point(startX, startY));
			line.setEndPoint(new Point(endX, endY));
			line.setEdgeColor(getEdgeColor());

			return true;
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Please enter valid numbers for coordinates", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
}