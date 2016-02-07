package pl.tciesla.finansista.view;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.tciesla.finansista.FinansistaApplication;
import pl.tciesla.finansista.dao.AssetDaoMemory;
import pl.tciesla.finansista.model.Asset;

public class AssetOverviewController {
	
	private Stage primaryStage;
	
	@FXML
	private TableView<Asset> assetTable;
	@FXML
	private TableColumn<Asset, String> assetNameColumn;
	@FXML
	private TableColumn<Asset, String> assetValueColumn;
	@FXML
	private TableColumn<Asset, String> assetCategoryColumn;
	@FXML
	private Label assetsTotalValueLabel;
	
	@FXML
	private void initialize() {
		assetNameColumn.setCellValueFactory(cell -> cell.getValue().name());
		assetValueColumn.setCellValueFactory(cell -> cell.getValue().value().asString());
		assetCategoryColumn.setCellValueFactory(cell -> cell.getValue().category().asString());
		assetTable.getItems().addAll(AssetDaoMemory.getInstance().fetchAll());
		calculateTotalAssetsValue();
	}
	
	private void calculateTotalAssetsValue() {
		BigDecimal totalValue = assetTable.getItems().stream().map(Asset::getValue)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		assetsTotalValueLabel.setText(totalValue.setScale(2, RoundingMode.HALF_EVEN).toString());
	}
	
	@FXML
	private void handleNewButtonClicked() {
		
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(FinansistaApplication.class.getResource("view/AssetEditDialog.fxml"));
			AnchorPane editDialog = loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("New asset");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			
			AssetEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setAsset(new Asset());
			
			Scene scene = new Scene(editDialog);
			dialogStage.setScene(scene);
			dialogStage.showAndWait();
			
			if (controller.isOkClicked()) {
				assetTable.getItems().add(controller.getAsset());
				calculateTotalAssetsValue();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleEditButtonClicked() {
		if (assetTable.getSelectionModel().getSelectedItem() == null) return;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(FinansistaApplication.class.getResource("view/AssetEditDialog.fxml"));
			AnchorPane editDialog = loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("New asset");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);

			AssetEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setAsset(assetTable.getSelectionModel().getSelectedItem());

			Scene scene = new Scene(editDialog);
			dialogStage.setScene(scene);
			dialogStage.showAndWait();

			if (controller.isOkClicked()) {
				int selectedIndex = assetTable.getSelectionModel().getSelectedIndex();
				assetTable.getItems().set(selectedIndex, controller.getAsset());
				calculateTotalAssetsValue();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private void handleDeleteButtonClicked() {
		Asset selectedAsset = assetTable.getSelectionModel().getSelectedItem();
		if (selectedAsset != null) assetTable.getItems().remove(selectedAsset);
		calculateTotalAssetsValue();
	}

	public Stage getStage() {
		return primaryStage;
	}

	public void setStage(Stage stage) {
		this.primaryStage = stage;
	}

}
