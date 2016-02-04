package pl.tciesla.finansista.model;

import java.math.BigDecimal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Asset {

	private final StringProperty name;
	private final ObjectProperty<BigDecimal> value;

	public Asset(String name, BigDecimal value) {
		this.name = new SimpleStringProperty(name);
		this.value = new SimpleObjectProperty<>(value);
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public StringProperty name() {
		return name;
	}

	public BigDecimal getValue() {
		return value.get();
	}

	public void setValue(BigDecimal value) {
		this.value.set(value);
	}

	public ObjectProperty<BigDecimal> value() {
		return value;
	}
}
