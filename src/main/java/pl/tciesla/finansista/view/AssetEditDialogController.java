package pl.tciesla.finansista.view;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.tciesla.finansista.model.asset.Asset;
import pl.tciesla.finansista.model.asset.AssetCategory;

public class AssetEditDialogController {
	
	private Stage dialogStage;
	private boolean okClicked = false;
	private Asset asset;
	
	@FXML
	private TextField assetNameField;
	@FXML
	private TextField assetValueField;
	@FXML
	private ComboBox<AssetCategory> assetCategoryBox;
	
	@FXML
	private void initialize() {
		assetCategoryBox.getItems().setAll(AssetCategory.values());
	}
	
	@FXML
	private void handleOKButtonClicked() {
		okClicked = true;
		asset.setName(assetNameField.getText());
		asset.setValue(new BigDecimal(assetValueField.getText()));
		asset.setCategory(assetCategoryBox.getSelectionModel().getSelectedItem());
		dialogStage.close();
	}
	
	@FXML
	private void handleCancelButtonClicked() {
		dialogStage.close();
	}

	public Stage getDialogStage() {
		return dialogStage;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	public void setOkClicked(boolean okClicked) {
		this.okClicked = okClicked;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
		assetNameField.setText(asset.getName());
		assetValueField.setText(asset.getValue().setScale(2, RoundingMode.HALF_UP).toString());
		assetCategoryBox.getSelectionModel().select(asset.getCategory());
	}

}
