package mvc.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public abstract class DlgShape extends JDialog {
	private boolean confirmed;
	protected JButton btnOk, btnCancel;
	protected JPanel buttonPanel, contentPanel;
	private Color edgeColor = Color.BLACK;
	private Color fillColor = Color.WHITE;

	public DlgShape(Frame parent, String title) {
		super(parent, title, true);
		setResizable(false);
		initializeComponents();
	}

	protected void initializeComponents() {
		contentPanel = new JPanel();
		buttonPanel = new JPanel();

		btnOk = new JButton("OK");
		btnOk.addActionListener(e -> {
			if (validateInput()) {
				confirmed = true;
				setVisible(false);
			}
		});

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(e -> {
			confirmed = false;
			setVisible(false);
		});

		buttonPanel.add(btnOk);
		buttonPanel.add(btnCancel);

		getContentPane().add(contentPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	protected abstract boolean validateInput();

	public boolean isInputValid() {
		return validateInput();
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public Color getEdgeColor() {
		return edgeColor;
	}

	public Color getFillColor() {
		return fillColor;
	}

	protected void setEdgeColor(Color color) {
		this.edgeColor = color;
	}

	protected void setFillColor(Color color) {
		this.fillColor = color;
	}
}