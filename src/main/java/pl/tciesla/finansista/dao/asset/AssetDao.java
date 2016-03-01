package pl.tciesla.finansista.dao.asset;

import java.util.List;

import pl.tciesla.finansista.dao.BaseDao;
import pl.tciesla.finansista.model.asset.Asset;

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
