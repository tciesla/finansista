package pl.tciesla.finansista.view;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import pl.tciesla.finansista.model.AssetCategory;

public class AssetsOverviewController {
	
	private static final String CENTER_ALIGNMENT_STYLE = "-fx-alignment: CENTER;";

	private Stage stage;
	
	@FXML
	private Label assetsValueLabel;
	@FXML
	private TableView<Asset> assetsTable;
	@FXML
	private TableColumn<Asset, String> assetNameColumn;
	@FXML
	private TableColumn<Asset, String> assetValueColumn;
	@FXML
	private TableColumn<Asset, String> assetCategoryColumn;
	@FXML
	private TableColumn<Asset, String> assetShareColumn;
	
	@FXML
	private TableView<AssetCategoryView> categoriesTable;
	@FXML
	private TableColumn<AssetCategoryView, String> categoryNameColumn;
	@FXML
	private TableColumn<AssetCategoryView, String> categoryValueColumn;
	@FXML
	private TableColumn<AssetCategoryView, String> categoryShareColumn;
	@FXML
	private PieChart categoriesValuePieChart;
	
	@FXML
	private void initialize() {
		
		initializeAssetsTable();
		initializeCategoriesTable();
		initializeCategoriesPieChart();
	}

	private void initializeAssetsTable() {
		// asset name column
		assetNameColumn.setCellValueFactory(c -> c.getValue().name());
		assetNameColumn.setStyle(CENTER_ALIGNMENT_STYLE);
		
		// asset value column
		assetValueColumn.setCellValueFactory(c -> c.getValue().value().asString());
		assetValueColumn.setStyle(CENTER_ALIGNMENT_STYLE);
		
		// asset category column
		assetCategoryColumn.setCellValueFactory(c -> c.getValue().category().asString());
		assetCategoryColumn.setStyle(CENTER_ALIGNMENT_STYLE);
		
		// asset share column
		assetShareColumn.setCellValueFactory(c -> c.getValue().share().asString());
		assetShareColumn.setStyle(CENTER_ALIGNMENT_STYLE);
		
		// add listener that recalculates assets value and shares
		assetsTable.getItems().addListener((ListChangeListener.Change<? extends Asset> c) -> {
			calculateTotalAssetsValueAndShares();
		});
		
		// fills assets table with data from XML file
		assetsTable.getItems().addAll(AssetDaoXml.getInstance().fetchAll());
	}
	
	private void initializeCategoriesTable() {
		// category name column
		categoryNameColumn.setCellValueFactory(c -> c.getValue().name());
		categoryNameColumn.setStyle(CENTER_ALIGNMENT_STYLE);
		
		// category value column
		categoryValueColumn.setCellValueFactory(c -> c.getValue().value().asString());
		categoryValueColumn.setStyle(CENTER_ALIGNMENT_STYLE);
		
		// category share column
		categoryShareColumn.setCellValueFactory(c -> c.getValue().share().asString());
		categoryShareColumn.setStyle(CENTER_ALIGNMENT_STYLE);
		
		Map<String, BigDecimal> categoryValues = new HashMap<>();
		for (AssetCategory assetCategory : AssetCategory.values()) {
			categoryValues.put(assetCategory.toString(), BigDecimal.ZERO);
		}
		for (Asset asset : AssetDaoXml.getInstance().fetchAll()) {
			String key = asset.getCategory().toString();
			BigDecimal oldValue = categoryValues.get(key);
			BigDecimal newValue = oldValue.add(asset.getValue());
			categoryValues.put(key, newValue);
		}
		
		BigDecimal assetsValue = assetsTable.getItems().stream().map(Asset::getValue)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		Map<String, BigDecimal> categoryShares = new HashMap<>();
		for (AssetCategory assetCategory : AssetCategory.values()) {
			BigDecimal categoryValue = categoryValues.get(assetCategory.toString());
			BigDecimal share = categoryValue.divide(assetsValue, 4, RoundingMode.HALF_UP);
			BigDecimal percentShare = share.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
			categoryShares.put(assetCategory.toString(), percentShare);
		}
		
		for (AssetCategory assetCategory : AssetCategory.values()) {
			AssetCategoryView assetCategoryView = new AssetCategoryView();
			assetCategoryView.setName(assetCategory.toString());
			assetCategoryView.setValue(categoryValues.get(assetCategory.toString()));
			assetCategoryView.setShare(categoryShares.get(assetCategory.toString()));
			categoriesTable.getItems().add(assetCategoryView);
		}
		categoriesTable.getItems().sort((c1, c2) -> c2.getValue().compareTo(c1.getValue()));
		
	}
	
	private void initializeCategoriesPieChart() {
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		for (AssetCategoryView assetCategoryView : categoriesTable.getItems()) {
			pieChartData.add(new PieChart.Data(assetCategoryView.name().get(), assetCategoryView.getValue().doubleValue()));
		}
		categoriesValuePieChart.setData(pieChartData);
	}
	
	private void calculateTotalAssetsValueAndShares() {
		BigDecimal totalValue = assetsTable.getItems().stream().map(Asset::getValue)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		assetsValueLabel.setText(totalValue.setScale(2, RoundingMode.HALF_EVEN).toString());
		assetsTable.getItems().stream().forEach(asset -> {
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
				assetsTable.getItems().add(controller.getAsset());
				AssetDaoXml.getInstance().persist(controller.getAsset());
//				calculateTotalAssetsValueAndShares();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleEditButtonClicked() {
		if (assetsTable.getSelectionModel().getSelectedItem() == null) return;
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
			controller.setAsset(assetsTable.getSelectionModel().getSelectedItem());

			Scene scene = new Scene(editDialog);
			dialogStage.setScene(scene);
			dialogStage.showAndWait();

			if (controller.isOkClicked()) {
				assetsTable.getItems().clear();
				AssetDaoXml.getInstance().update(controller.getAsset());
				assetsTable.getItems().addAll(AssetDaoXml.getInstance().fetchAll());
//				calculateTotalAssetsValueAndShares();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private void handleDeleteButtonClicked() {
		int selectedAsset = assetsTable.getSelectionModel().getSelectedIndex();
		if (selectedAsset != -1) { 
			assetsTable.getItems().remove(selectedAsset);
			AssetDaoXml.getInstance().delete(selectedAsset);
		}
//		calculateTotalAssetsValueAndShares();
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
