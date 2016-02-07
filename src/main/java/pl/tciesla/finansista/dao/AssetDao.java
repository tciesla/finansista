package pl.tciesla.finansista.dao;

import java.util.List;

import pl.tciesla.finansista.model.Asset;

public interface AssetDao {
	
	List<Asset> fetchAll();
	void persist(Asset asset);
	void update(Asset asset);
	void delete(Asset asset);
}
