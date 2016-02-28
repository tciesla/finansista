package pl.tciesla.finansista.model;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "assets")
@XmlAccessorType(XmlAccessType.FIELD)
public class AssetsWrapper {

	@XmlElement(name = "asset")
	private List<Asset> assets;
	
	public AssetsWrapper() {
		assets = new LinkedList<>();
	}

	public AssetsWrapper(List<Asset> assets) {
		this.assets = assets;
	}

	public List<Asset> getAssets() {
		return assets;
	}

}
