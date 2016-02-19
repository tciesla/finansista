package pl.tciesla.finansista.dao;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import pl.tciesla.finansista.model.Asset;
import pl.tciesla.finansista.model.AssetListWrapper;

public class AssetDaoXml implements AssetDao {
	
	private static AssetDaoXml instance = null;
	
	File assetsFile = new File("assets.xml");
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;
	
	public static synchronized AssetDaoXml getInstance() {
		if (instance == null) createInstance();
		return instance;
	}

	private static synchronized void createInstance() {
		if (instance == null) instance = new AssetDaoXml();
	}
	
	protected AssetDaoXml() {
		try {
			JAXBContext context = JAXBContext.newInstance(AssetListWrapper.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			unmarshaller = context.createUnmarshaller();
			createEmptyFileIfNotExists();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createEmptyFileIfNotExists() throws JAXBException {
		if (!assetsFile.exists()) {
			marshaller.marshal(new AssetListWrapper(), assetsFile);
		}
	}

	@Override
	public List<Asset> fetchAll() {
		try {
			AssetListWrapper wrapper = (AssetListWrapper) unmarshaller.unmarshal(assetsFile);
			List<Asset> assets = wrapper.getAssets();
			Collections.sort(assets, (a1, a2) -> {
				return a2.getValue().compareTo(a1.getValue());
			});
			return assets;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void persist(Asset asset) {
		try {
			AssetListWrapper wrapper = (AssetListWrapper) unmarshaller.unmarshal(assetsFile);
			wrapper.getAssets().add(asset);
			marshaller.marshal(wrapper, assetsFile);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void update(Asset asset) {
		try {
			AssetListWrapper wrapper = (AssetListWrapper) unmarshaller.unmarshal(assetsFile);
			List<Asset> assets = wrapper.getAssets();
			for (int i = 0; i < assets.size(); i++) {
				if (assets.get(i).equals(asset)) {
					assets.set(i, asset);
				}
			}
			
			wrapper.setAssets(assets);
			marshaller.marshal(wrapper, assetsFile);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Asset asset) {
		try {
			AssetListWrapper wrapper = (AssetListWrapper) unmarshaller.unmarshal(assetsFile);
			wrapper.getAssets().remove(asset);
			marshaller.marshal(wrapper, assetsFile);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
