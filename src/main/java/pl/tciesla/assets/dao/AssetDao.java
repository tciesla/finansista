package pl.tciesla.assets.dao;

import java.util.List;

import pl.tciesla.assets.model.Asset;

public interface AssetDao extends BaseDao<Asset, Integer> {

	@Override
	void persist(Asset asset);

	@Override
	void update(Asset asset);

	@Override
	void delete(Integer id);

	@Override
	List<Asset> fetchAll();
}
