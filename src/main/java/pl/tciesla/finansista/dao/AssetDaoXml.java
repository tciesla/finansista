package pl.tciesla.finansista.dao;

import java.io.File;
import java.util.List;
import java.util.stream.IntStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import pl.tciesla.finansista.model.Asset;
import pl.tciesla.finansista.model.AssetsWrapper;

public class AssetDaoXml implements AssetDao {
	
	private static AssetDaoXml instance = new AssetDaoXml();
	
	File assetsFile = new File("assets.xml");
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;
	
	public static AssetDaoXml getInstance() {
		return instance;
	}

	protected AssetDaoXml() {
		try {
			JAXBContext context = JAXBContext.newInstance(AssetsWrapper.class);
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
			marshaller.marshal(new AssetsWrapper(), assetsFile);
		}
	}
	
	@Override
	public void persist(Asset asset) {
		try {
			List<Asset> assets = fetchAll();
			assets.add(asset);
			marshallAssets(assets);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void marshallAssets(List<Asset> assets) throws JAXBException {
		assets.sort((a1, a2) -> a2.getValue().compareTo(a1.getValue()));
		IntStream.range(0, assets.size()).forEach(i -> assets.get(i).setId(i));
		marshaller.marshal(new AssetsWrapper(assets), assetsFile);
	}
	
	@Override
	public void update(Asset asset) {
		try {
			List<Asset> assets = fetchAll();
			assets.set(asset.getId(), asset);
			marshallAssets(assets);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void delete(Integer id) {
		try {
			List<Asset> assets = fetchAll();
			assets.remove(id.intValue());
			marshallAssets(assets);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public Asset fetch(Integer id) {
		List<Asset> assets = fetchAll();
		return assets.size() > id ? assets.get(id) : null;
	}

	@Override
	public List<Asset> fetchAll() {
		try {
			AssetsWrapper wrapper = (AssetsWrapper) unmarshaller.unmarshal(assetsFile);
			List<Asset> assets = wrapper.getAssets();
			assets.sort((a1, a2) -> a2.getValue().compareTo(a1.getValue()));
			IntStream.range(0, assets.size()).forEach(i -> assets.get(i).setId(i));
			return assets;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
