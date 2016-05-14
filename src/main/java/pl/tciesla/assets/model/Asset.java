package pl.tciesla.assets.model;

import javafx.beans.property.*;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;

@XmlRootElement
@XmlType(propOrder = {"name", "category", "value"})
public class Asset {

	private static final String INITIAL_NAME = "";

	private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
	private final StringProperty name = new SimpleStringProperty(this, "name", INITIAL_NAME);
	private final ObjectProperty<AssetCategory> category = new SimpleObjectProperty<>(this, "category");
	private final ObjectProperty<BigDecimal> value = new SimpleObjectProperty<>(this, "value", BigDecimal.ZERO);
	private final ObjectProperty<BigDecimal> share = new SimpleObjectProperty<>(this, "share", BigDecimal.ZERO);

    public final IntegerProperty idProperty() {
        return id;
    }

	@XmlTransient
	public final Integer getId() {
		return id.get();
	}

	public final void setId(Integer id) {
		this.id.set(id);
	}

    public final StringProperty nameProperty() {
        return name;
    }

	public final String getName() {
		return name.get();
	}

	public final void setName(String name) {
		this.name.set(name);
	}

    public final ObjectProperty<AssetCategory> categoryProperty() {
        return category;
    }

    public final AssetCategory getCategory() {
        return category.get();
    }

    public final void setCategory(AssetCategory category) {
        this.category.set(category);
    }

    public final ObjectProperty<BigDecimal> valueProperty() {
        return value;
    }

	public final BigDecimal getValue() {
		return value.get();
	}

	public final void setValue(BigDecimal value) {
		this.value.set(value);
	}

    public final ObjectProperty<BigDecimal> shareProperty() {
        return share;
    }

    @XmlTransient
    public final BigDecimal getShare() {
        return share.get();
    }

	public final void setShare(BigDecimal share) {
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
