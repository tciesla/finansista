package pl.tciesla.finansista.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pl.tciesla.finansista.model.Asset;
import pl.tciesla.finansista.model.AssetCategory;

public class AssetDaoMemory implements AssetDao {

	private static AssetDaoMemory instance = null;
	
	private List<Asset> assets = new ArrayList<>();

	protected AssetDaoMemory() {
		// Sample assets
		assets.add(new Asset("Cash in wallet", new BigDecimal("1000.00"), AssetCategory.CASH));
		assets.add(new Asset("Cash in secure place", new BigDecimal("5000.00"), AssetCategory.CASH));
		assets.add(new Asset("2500 Microsoft shares", new BigDecimal("2000.00"), AssetCategory.STOCK));
		assets.add(new Asset("1500 Facebook shares", new BigDecimal("32000.50"), AssetCategory.STOCK));
		assets.add(new Asset("Pure gold 100g", new BigDecimal("10000.55"), AssetCategory.GOODS));
		assets.add(new Asset("My personal computer", new BigDecimal("4000.00"), AssetCategory.GOODS));
		assets.add(new Asset("Steves personal load", new BigDecimal("2500.00"), AssetCategory.RECEIVABLE));
		assets.add(new Asset("Deposit in Bank of America", new BigDecimal("25000.00"), AssetCategory.DEPOSIT));
		assets.add(new Asset("Deposit in Bank of China", new BigDecimal("4500.00"), AssetCategory.DEPOSIT));
	}

	public static synchronized AssetDaoMemory getInstance() {
		if (instance == null) createInstance();
		return instance;
	}

	private static synchronized void createInstance() {
		if (instance == null) instance = new AssetDaoMemory();
	}

	@Override
	public List<Asset> fetchAll() {
		return assets;
	}

	@Override
	public void persist(Asset asset) {
		assets.add(asset);
	}
	
	@Override
	public void update(Asset asset) {
		for (int i = 0; i < assets.size(); i++) {
			if (assets.get(i).equals(asset)) {
				assets.set(i, asset);
			}
		}
	}

	@Override
	public void delete(Asset asset) {
		assets.remove(asset);
	}
	
}
