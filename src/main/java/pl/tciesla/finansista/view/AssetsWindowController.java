package pl.tciesla.finansista.view;

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
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.tciesla.finansista.dao.AssetDaoXml;
import pl.tciesla.finansista.model.Asset;
import pl.tciesla.finansista.model.AssetCategory;
import pl.tciesla.finansista.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static java.math.RoundingMode.HALF_UP;

public class AssetsWindowController {

    private static final int NUMBER_SCALE = 4;
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
    @SuppressWarnings("unused")
	private void initialize() {
		initializeAssetsTableColumns();
		initializeCategoriesTableColumns();
		addAssetsTableListeners();
		loadAssetsIntoAssetsTable();
	}

	private void initializeAssetsTableColumns() {
        initializeAssetNameColumn();
        initializeAssetValueColumn();
        initializeAssetCategoryColumn();
        initializeAssetShareColumn();
	}

    private void initializeAssetNameColumn() {
        assetNameColumn.setCellValueFactory(c -> c.getValue().name());
        assetNameColumn.setStyle(CENTER_ALIGNMENT_STYLE);
    }

    private void initializeAssetValueColumn() {
        assetValueColumn.setCellValueFactory(c -> new SimpleStringProperty(
                StringUtils.toCurrency(c.getValue().getValue())));
        assetValueColumn.setStyle(CENTER_ALIGNMENT_STYLE);
    }

    private void initializeAssetCategoryColumn() {
        assetCategoryColumn.setCellValueFactory(c -> c.getValue().category().asString());
        assetCategoryColumn.setStyle(CENTER_ALIGNMENT_STYLE);
    }

    private void initializeAssetShareColumn() {
        assetShareColumn.setCellValueFactory(c -> new SimpleStringProperty(
                StringUtils.toPercent(c.getValue().getShare())));
        assetShareColumn.setStyle(CENTER_ALIGNMENT_STYLE);
    }

    private void initializeCategoriesTableColumns() {
        initializeCategoryNameColumn();
        initializeCategoryValueColumn();
        initializeCategoryShareColumn();
	}

    private void initializeCategoryNameColumn() {
        categoryNameColumn.setCellValueFactory(c -> c.getValue().name());
        categoryNameColumn.setStyle(CENTER_ALIGNMENT_STYLE);
    }

    private void initializeCategoryValueColumn() {
        categoryValueColumn.setCellValueFactory(c -> new SimpleStringProperty(
                StringUtils.toCurrency(c.getValue().getValue())));
        categoryValueColumn.setStyle(CENTER_ALIGNMENT_STYLE);
    }

    private void initializeCategoryShareColumn() {
        categoryShareColumn.setCellValueFactory(c -> new SimpleStringProperty(
                StringUtils.toPercent(c.getValue().getShare())));
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

        BigDecimal assetsValue = assetsTable.getItems().stream()
                .map(Asset::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		// updates assetsValue label
		assetsValueLabel.setText(StringUtils.toCurrency(assetsValue));
		
		// updates shares in assets table
		assetsTable.getItems().stream().forEach(asset -> {
			BigDecimal value = asset.getValue();
            BigDecimal share = value.divide(assetsValue, NUMBER_SCALE, HALF_UP);
			asset.setShare(share);
		});
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
			BigDecimal share = categoryValue.divide(assetsValue, NUMBER_SCALE, HALF_UP);
			categoryShares.put(assetCategory, share);
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
		ObservableList<PieChart.Data> categoriesChartData = FXCollections.observableArrayList();
		categoriesTable.getItems().stream().forEach(categoryView -> {
            String name = categoryView.getName();
            double value = categoryView.getValue().doubleValue();
            categoriesChartData.add(new PieChart.Data(name, value));
        });
		categoriesValuePieChart.setData(categoriesChartData);
	}
	
	private void loadAssetsIntoAssetsTable() {
        List<Asset> assets = AssetDaoXml.getInstance().fetchAll();
        assetsTable.getItems().addAll(assets);
	}

    @FXML
    @SuppressWarnings("unused")
    private void handleCloseApplication() {
        System.exit(0);
    }
	
	@FXML
    @SuppressWarnings("unused")
	private void handleNewAssetOperation() {
		
		try {
            FXMLLoader loader = createLoader();
			Pane newAssetDialogPane = loader.load();
            Stage dialogStage = createDialogStage("asset.add.dialog.title");
			AssetDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setAsset(new Asset());

            showAssetDialogAndWait(newAssetDialogPane, dialogStage);
            executeAddAssetIfOkClicked(controller);

        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    private void executeAddAssetIfOkClicked(AssetDialogController controller) {
        if (controller.isOkClicked()) {
            Asset asset = controller.getAsset();
            AssetDaoXml.getInstance().persist(asset);
            refreshAssetsInTable();
        }
    }

    @FXML
    @SuppressWarnings("unused")
	private void handleEditAssetOperation() {
		if (assetsTable.getSelectionModel().getSelectedItem() == null) return;
		try {
            FXMLLoader loader = createLoader();
            Pane editAssetDialogPane = loader.load();
            Stage dialogStage = createDialogStage("asset.edit.dialog.title");
			AssetDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
            Asset selectedAsset = assetsTable.getSelectionModel().getSelectedItem();
            controller.setAsset(selectedAsset);

            showAssetDialogAndWait(editAssetDialogPane, dialogStage);
            executeEditAssetIfOkClicked(controller);

        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    private void executeEditAssetIfOkClicked(AssetDialogController controller) {
        if (controller.isOkClicked()) {
            Asset modifiedAsset = controller.getAsset();
            AssetDaoXml.getInstance().update(modifiedAsset);
            refreshAssetsInTable();
        }
    }

    private FXMLLoader createLoader() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/AssetDialog.fxml"));
        loader.setResources(ResourceBundle.getBundle("Bundle"));
        return loader;
    }

    private Stage createDialogStage(String dialogTitleKey) {
        Stage dialogStage = new Stage();
        ResourceBundle bundle = ResourceBundle.getBundle("Bundle");
        dialogStage.setTitle(bundle.getString(dialogTitleKey));
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(stage);
        return dialogStage;
    }

    private void showAssetDialogAndWait(Pane newAssetDialogPane, Stage dialogStage) {
        Scene scene = new Scene(newAssetDialogPane);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

	@FXML
    @SuppressWarnings("unused")
	private void handleDeleteAssetOperation() {
		int selectedAsset = assetsTable.getSelectionModel().getSelectedIndex();
		if (selectedAsset != -1) { 
			AssetDaoXml.getInstance().delete(selectedAsset);
			refreshAssetsInTable();
		}
	}

	private void refreshAssetsInTable() {
		assetsTable.getItems().clear();
        List<Asset> assets = AssetDaoXml.getInstance().fetchAll();
        assetsTable.getItems().addAll(assets);
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
