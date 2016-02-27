package pl.tciesla.finansista.view;

import java.math.BigDecimal;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class AssetCategoryView {
	
	private SimpleStringProperty name = new SimpleStringProperty();
	private SimpleObjectProperty<BigDecimal> value = new SimpleObjectProperty<>();
	private SimpleObjectProperty<BigDecimal> share = new SimpleObjectProperty<>();
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	public String getName() {
		return name.get();
	}
	
	public SimpleStringProperty name() {
		return name;
	}
	
	public void setValue(BigDecimal value) {
		this.value.set(value);
	}
	
	public BigDecimal getValue() {
		return value.get();
	}
	
	public SimpleObjectProperty<BigDecimal> value() {
		return value;
	}
	
	public void setShare(BigDecimal share) {
		this.share.set(share);
	}
	
	public SimpleObjectProperty<BigDecimal> share() {
		return share;
	}

}
