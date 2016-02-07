package pl.tciesla.finansista.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@XmlRootElement
@XmlType(propOrder = {"name", "category", "value"})
public class Asset {

	private final StringProperty name;
	private final ObjectProperty<BigDecimal> value;
	private final ObjectProperty<Category> category;
	
	public Asset() {
		this("", BigDecimal.ZERO, Category.CASH);
	}

	public Asset(String name, BigDecimal value, Category category) {
		this.name = new SimpleStringProperty(name);
		this.value = new SimpleObjectProperty<>(value);
		this.category = new SimpleObjectProperty<>(category);
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
	
	public Category getCategory() {
		return category.get();
	}
	
	public void setCategory(Category category) {
		this.category.set(category);
	}
	
	public ObjectProperty<Category> category() {
		return category;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name.get() == null) ? 0 : name.get().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Asset other = (Asset) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.get().equals(other.name.get()))
			return false;
		return true;
	}
	
}
