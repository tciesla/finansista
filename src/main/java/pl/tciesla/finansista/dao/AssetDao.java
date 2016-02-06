package pl.tciesla.finansista.dao;

import java.math.BigDecimal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.tciesla.finansista.model.Asset;
import pl.tciesla.finansista.model.Category;

public class AssetDao {

	private static AssetDao instance = null;
	
	private ObservableList<Asset> assets = FXCollections.observableArrayList();

	protected AssetDao() {
		// TODO: remove sample assets
		assets.add(new Asset("Cash in wallet", new BigDecimal("1000.00"), Category.CASH));
		assets.add(new Asset("US Gov. Bonds", new BigDecimal("25000.00"), Category.BONDS));
		assets.add(new Asset("2500 Microsoft shares", new BigDecimal("42300.25"), Category.STOCK));
	}

	public static synchronized AssetDao getInstance() {
		if (instance == null) createInstance();
		return instance;
	}

	private static synchronized void createInstance() {
		if (instance == null) instance = new AssetDao();
	}

	public ObservableList<Asset> fetchAll() {
		return assets;
	}
	
}
