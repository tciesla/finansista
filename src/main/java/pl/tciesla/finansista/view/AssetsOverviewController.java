package pl.tciesla.finansista.view;

import static java.math.RoundingMode.HALF_UP;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
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
import pl.tciesla.finansista.FinancierApplication;
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
		initializeAssetsTableColumns();
		initializeCategoriesTableColumns();
		addAssetsTableListeners();
		loadAssetsIntoAssetsTable();
	}

	private void initializeAssetsTableColumns() {
		// asset name column
		assetNameColumn.setCellValueFactory(c -> c.getValue().name());
		assetNameColumn.setStyle(CENTER_ALIGNMENT_STYLE);
		
		// asset value column
        assetValueColumn.setCellValueFactory(c -> new SimpleStringProperty(toCurrency(c.getValue().getValue())));
		assetValueColumn.setStyle(CENTER_ALIGNMENT_STYLE);
		
		// asset category column
		assetCategoryColumn.setCellValueFactory(c -> c.getValue().category().asString());
		assetCategoryColumn.setStyle(CENTER_ALIGNMENT_STYLE);
		
		// asset share column
		assetShareColumn.setCellValueFactory(c -> c.getValue().share().asString());
		assetShareColumn.setStyle(CENTER_ALIGNMENT_STYLE);
	}
	
	private void initializeCategoriesTableColumns() {
		// category name column
		categoryNameColumn.setCellValueFactory(c -> c.getValue().name());
		categoryNameColumn.setStyle(CENTER_ALIGNMENT_STYLE);
		
		// category value column
        categoryValueColumn.setCellValueFactory(c -> new SimpleStringProperty(toCurrency(c.getValue().getValue())));
		categoryValueColumn.setStyle(CENTER_ALIGNMENT_STYLE);
		
		// category share column
		categoryShareColumn.setCellValueFactory(c -> c.getValue().share().asString());
		categoryShareColumn.setStyle(CENTER_ALIGNMENT_STYLE);
	}
	
	private void addAssetsTableListeners() {
		assetsTable.getItems().addListener((ListChangeListener.Change<? extends Asset> c) -> {
			createOrUpdateAssetsValueAndShares();
			createOrUpdateCategoriesTable();
			createOrUpdateCategoriesPieChart();
		});
	}
	
	private void createOrUpdateAssetsValueAndShares() {
		
		// updates assetsValue label
		BigDecimal assetsValue = assetsTable.getItems().stream()
				.map(Asset::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
		assetsValueLabel.setText(toCurrency(assetsValue.setScale(2, HALF_UP)));
		
		// updates shares in assets table
		assetsTable.getItems().stream().forEach(asset -> {
			BigDecimal value = asset.getValue();
			BigDecimal share = value.divide(assetsValue, 4, HALF_UP);
			BigDecimal percent = share.multiply(BigDecimal.valueOf(100));
			asset.setShare(percent.setScale(2, HALF_UP));
		});
	}

    private String toCurrency(BigDecimal value) {
        NumberFormat currencyInstance = NumberFormat.getCurrencyInstance();
        currencyInstance.setMinimumFractionDigits(2);
        currencyInstance.setMaximumFractionDigits(2);
        return currencyInstance.format(value);
    }

    private void createOrUpdateCategoriesTable() {
		Map<AssetCategory, BigDecimal> categoryValues = createCategoryValuesMap();
		Map<AssetCategory, BigDecimal> categoryShares = createCategorySharesMap(categoryValues);
		List<AssetCategoryView> assetCategoryViews = createAssetCategoryViews(categoryValues, categoryShares);
		categoriesTable.getItems().clear();
		categoriesTable.getItems().addAll(assetCategoryViews);
	}

	private Map<AssetCategory, BigDecimal> createCategoryValuesMap() {
		
		Map<AssetCategory, BigDecimal> categoryValues = new HashMap<>();
		assetsTable.getItems().stream().forEach(asset -> {
			AssetCategory category = asset.getCategory();
			BigDecimal oldValue = categoryValues.get(category);
			if (oldValue == null) categoryValues.put(category, BigDecimal.ZERO);
			oldValue = categoryValues.get(category);
			BigDecimal newValue = oldValue.add(asset.getValue());
			categoryValues.put(category, newValue);
		});
		
		return categoryValues;
	}
	
	private Map<AssetCategory, BigDecimal> createCategorySharesMap(Map<AssetCategory, BigDecimal> categoryValues) {
		
		BigDecimal assetsValue = assetsTable.getItems().stream()
				.map(Asset::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		Map<AssetCategory, BigDecimal> categoryShares = new HashMap<>();
		categoryValues.keySet().stream().forEach(assetCategory -> {
			BigDecimal categoryValue = categoryValues.get(assetCategory);
			BigDecimal share = categoryValue.divide(assetsValue, 4, HALF_UP);
			BigDecimal percent = share.multiply(BigDecimal.valueOf(100));
			categoryShares.put(assetCategory, percent.setScale(2, HALF_UP));
		});
		
		return categoryShares;
	}
	
	private List<AssetCategoryView> createAssetCategoryViews(
			Map<AssetCategory, BigDecimal> categoryValues,
			Map<AssetCategory, BigDecimal> categoryShares) {
		
		List<AssetCategoryView> assetCategoryViews = new LinkedList<>();
		categoryValues.keySet().stream().forEach(assetCategory -> {
			AssetCategoryView assetCategoryView = new AssetCategoryView();
			assetCategoryView.setName(assetCategory.toString());
			assetCategoryView.setValue(categoryValues.get(assetCategory));
			assetCategoryView.setShare(categoryShares.get(assetCategory));
			assetCategoryViews.add(assetCategoryView);
		});
		
		// sort category view by value descending
		assetCategoryViews.sort((v1, v2) -> v2.getValue().compareTo(v1.getValue()));
		return assetCategoryViews;
	}
	
	private void createOrUpdateCategoriesPieChart() {
		
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		categoriesTable.getItems().stream().forEach(view ->
			pieChartData.add(new PieChart.Data(view.getName(), view.getValue().doubleValue()))
		);
		categoriesValuePieChart.setData(pieChartData);
		
//		categoriesValuePieChart.getData().stream().forEach(data -> {
//			String assetCategoryName = data.getName();
//			String color = AssetCategoryColorMapper.getColor(assetCategoryName);
//			data.getNode().setStyle("-fx-pie-color: " + color + ";");
//		});
		
	}
	
	private void loadAssetsIntoAssetsTable() {
		assetsTable.getItems().addAll(AssetDaoXml.getInstance().fetchAll());
	}

    @FXML
    private void handleCloseApplication() {
        System.exit(0);
    }
	
	@FXML
	private void handleNewAssetOperation() {
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/AssetEditDialog.fxml"));
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
				AssetDaoXml.getInstance().persist(controller.getAsset());
				refreshAssetsInTable();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleEditAssetOperation() {
		if (assetsTable.getSelectionModel().getSelectedItem() == null) return;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(FinancierApplication.class.getResource("/AssetEditDialog.fxml"));
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
				AssetDaoXml.getInstance().update(controller.getAsset());
				refreshAssetsInTable();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private void handleDeleteAssetOperation() {
		int selectedAsset = assetsTable.getSelectionModel().getSelectedIndex();
		if (selectedAsset != -1) { 
			AssetDaoXml.getInstance().delete(selectedAsset);
			refreshAssetsInTable();
		}
	}

	private void refreshAssetsInTable() {
		assetsTable.getItems().clear();
		assetsTable.getItems().addAll(AssetDaoXml.getInstance().fetchAll());
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
