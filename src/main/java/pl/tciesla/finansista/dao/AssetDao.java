package pl.tciesla.finansista.dao;

import java.util.List;

import pl.tciesla.finansista.model.Asset;

public interface AssetDao {
	List<Asset> fetchAll();
}
