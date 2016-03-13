package pl.tciesla.assets.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;

@XmlRootElement
@XmlType(propOrder = {"name", "category", "value"})
public class Asset {

	private static final String INITIAL_NAME = "";

	private Integer id;
	private final StringProperty name;
	private final ObjectProperty<BigDecimal> value;
	private final ObjectProperty<AssetCategory> category;
	private final ObjectProperty<BigDecimal> share;
	
	public Asset() {
		this(INITIAL_NAME, BigDecimal.ZERO, AssetCategory.CASH);
	}

	public Asset(String name, BigDecimal value, AssetCategory category) {
		this.name = new SimpleStringProperty(name);
		this.value = new SimpleObjectProperty<>(value);
		this.category = new SimpleObjectProperty<>(category);
		this.share = new SimpleObjectProperty<>(BigDecimal.ZERO);
	}

	@XmlTransient
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public AssetCategory getCategory() {
		return category.get();
	}
	
	public void setCategory(AssetCategory category) {
		this.category.set(category);
	}
	
	public ObjectProperty<AssetCategory> category() {
		return category;
	}

    @XmlTransient
    public BigDecimal getShare() {
        return share.get();
    }

	public void setShare(BigDecimal share) {
		this.share.set(share);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
