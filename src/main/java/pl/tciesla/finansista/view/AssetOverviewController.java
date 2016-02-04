package pl.tciesla.finansista.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.tciesla.finansista.dao.AssetDao;
import pl.tciesla.finansista.model.Asset;

public class AssetOverviewController {
	
	@FXML
	private TableView<Asset> assetTable;
	@FXML
	private TableColumn<Asset, String> assetNameColumn;
	@FXML
	private TableColumn<Asset, String> assetValueColumn;
	
	@FXML
	private void initialize() {
		assetNameColumn.setCellValueFactory(cell -> cell.getValue().name());
		assetValueColumn.setCellValueFactory(cell -> cell.getValue().value().asString());
		assetTable.getItems().addAll(AssetDao.getInstance().fetchAll());
	}
	
	@FXML
	private void handleNewButtonClicked() {
		// TODO implement this method
	}
	
	@FXML
	private void handleEditButtonClicked() {
		// TODO implement this method
	}
	
	@FXML
	private void handleDeleteButtonClicked() {
		// TODO implement this method
	}

}
