package mvc;

import javax.swing.JButton;

import mvc.model.DrawingModel;
import mvc.model.DrawingModelObserver;

public class ButtonStateObserver implements DrawingModelObserver {
	private JButton btnDelete;
	private JButton btnModify;
	private DrawingModel model;

	public ButtonStateObserver(JButton btnDelete, JButton btnModify, DrawingModel model) {
		this.btnDelete = btnDelete;
		this.btnModify = btnModify;
		this.model = model;
	}

	@Override
	public void update() {
		btnDelete.setEnabled(!model.getSelectedShapes().isEmpty());
		btnModify.setEnabled(model.getSelectedShapes().size() == 1);

	}

}
