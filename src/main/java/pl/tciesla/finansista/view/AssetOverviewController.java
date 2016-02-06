package pl.tciesla.finansista.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.tciesla.finansista.FinansistaApplication;
import pl.tciesla.finansista.dao.AssetDao;
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
	private void initialize() {
		assetNameColumn.setCellValueFactory(cell -> cell.getValue().name());
		assetValueColumn.setCellValueFactory(cell -> cell.getValue().value().asString());
		assetCategoryColumn.setCellValueFactory(cell -> cell.getValue().category().asString());
		assetTable.getItems().addAll(AssetDao.getInstance().fetchAll());
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
	}

	public Stage getStage() {
		return primaryStage;
	}

	public void setStage(Stage stage) {
		this.primaryStage = stage;
	}

}
