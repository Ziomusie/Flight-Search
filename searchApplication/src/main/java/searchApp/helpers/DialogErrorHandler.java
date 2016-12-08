package searchApp.helpers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

/**
 * Created by Mateusz-PC on 22.11.2016.
 */
public class DialogErrorHandler {
	private JFXDialog errorDialog;

	private StackPane stackPane;

	private JFXButton cancelButton;

	public DialogErrorHandler(JFXDialog errorDialog, StackPane stackPane, JFXButton cancelButton) {
		this.errorDialog = errorDialog;
		this.stackPane = stackPane;
		this.cancelButton = cancelButton;

		errorDialog.setOverlayClose(false);
		cancelButton.setOnMouseClicked((e)->{
			Platform.exit();
		});
}


	public void showErrorDialog() {
		errorDialog.show(stackPane);
	}


}
