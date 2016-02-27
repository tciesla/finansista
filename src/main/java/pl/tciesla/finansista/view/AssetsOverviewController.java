package pl.tciesla.finansista.view;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.tciesla.finansista.FinansistaApplication;
import pl.tciesla.finansista.dao.AssetDaoXml;
import pl.tciesla.finansista.model.Asset;

public class AssetsOverviewController {
	
	private Stage stage;
	
	private ObservableList<Asset> assets = FXCollections.observableArrayList();
	
	ObservableList<PieChart.Data> pieChartData =
            FXCollections.observableArrayList(
            new PieChart.Data("Grapefruit", 13),
            new PieChart.Data("Oranges", 25),
            new PieChart.Data("Plums", 10),
            new PieChart.Data("Pears", 22),
            new PieChart.Data("Apples", 30));
	
	@FXML
	private TableView<Asset> assetTable;
	@FXML
	private TableColumn<Asset, String> assetNameColumn;
	@FXML
	private TableColumn<Asset, String> assetValueColumn;
	@FXML
	private TableColumn<Asset, String> assetCategoryColumn;
	@FXML
	private TableColumn<Asset, String> assetShareColumn;
	@FXML
	private Label assetsTotalValueLabel;
	@FXML
	private PieChart categoryPieChart;
	
	@FXML
	private void initialize() {
		assetNameColumn.setCellValueFactory(cell -> cell.getValue().name());
		assetNameColumn.setStyle("-fx-alignment: CENTER;");
		assetValueColumn.setCellValueFactory(cell -> cell.getValue().value().asString());
		assetValueColumn.setStyle("-fx-alignment: CENTER;");
		assetCategoryColumn.setCellValueFactory(cell -> cell.getValue().category().asString());
		assetCategoryColumn.setStyle("-fx-alignment: CENTER;");
		assetShareColumn.setCellValueFactory(cell -> cell.getValue().share().asString());
		assetShareColumn.setStyle("-fx-alignment: CENTER;");
		assets.addAll(AssetDaoXml.getInstance().fetchAll());
		assetTable.getItems().addAll(assets);
		calculateTotalAssetsValueAndShares();
		categoryPieChart.setData(pieChartData);
	}
	
	private void calculateTotalAssetsValueAndShares() {
		BigDecimal totalValue = assetTable.getItems().stream().map(Asset::getValue)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		assetsTotalValueLabel.setText(totalValue.setScale(2, RoundingMode.HALF_EVEN).toString());
		assetTable.getItems().stream().forEach(asset -> {
			BigDecimal value = asset.getValue();
			BigDecimal share = value.divide(totalValue, 4, RoundingMode.HALF_UP);
			asset.setShare(share.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP));
		});
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
			dialogStage.initOwner(stage);
			
			AssetEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setAsset(new Asset());
			
			Scene scene = new Scene(editDialog);
			dialogStage.setScene(scene);
			dialogStage.showAndWait();
			
			if (controller.isOkClicked()) {
				assetTable.getItems().add(controller.getAsset());
				AssetDaoXml.getInstance().persist(controller.getAsset());
				calculateTotalAssetsValueAndShares();
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
			dialogStage.initOwner(stage);

			AssetEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setAsset(assetTable.getSelectionModel().getSelectedItem());

			Scene scene = new Scene(editDialog);
			dialogStage.setScene(scene);
			dialogStage.showAndWait();

			if (controller.isOkClicked()) {
				assetTable.getItems().clear();
				AssetDaoXml.getInstance().update(controller.getAsset());
				assetTable.getItems().addAll(AssetDaoXml.getInstance().fetchAll());
				calculateTotalAssetsValueAndShares();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private void handleDeleteButtonClicked() {
		int selectedAsset = assetTable.getSelectionModel().getSelectedIndex();
		if (selectedAsset != -1) { 
			assetTable.getItems().remove(selectedAsset);
			AssetDaoXml.getInstance().delete(selectedAsset);
		}
		calculateTotalAssetsValueAndShares();
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
