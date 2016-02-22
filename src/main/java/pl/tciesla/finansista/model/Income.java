package pl.tciesla.finansista.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@XmlRootElement
@XmlType(propOrder = { "name", "value", "date" })
public class Income {

	private StringProperty name;
	private ObjectProperty<BigDecimal> value;
	private ObjectProperty<LocalDate> date;

	public Income(String name, BigDecimal value) {
		this(name, value, LocalDate.now());
	}

	public Income(String name, BigDecimal value, LocalDate date) {
		this.name = new SimpleStringProperty(name);
		this.value = new SimpleObjectProperty<>(value);
		this.date = new SimpleObjectProperty<>(date);
	}

	public StringProperty name() {
		return name;
	}

	public String getName() {
		return name.get();
	}

	public ObjectProperty<BigDecimal> value() {
		return value;
	}

	public BigDecimal getValue() {
		return value.get();
	}

	public ObjectProperty<LocalDate> date() {
		return date;
	}

	public LocalDate getDate() {
		return date.get();
	}

	@Override
	public String toString() {
		return "Income [name=" + name.get() + ", value=" + value.get() + ", date=" + date.get() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.get().hashCode());
		result = prime * result + ((name == null) ? 0 : name.get().hashCode());
		result = prime * result + ((value == null) ? 0 : value.get().hashCode());
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
		Income other = (Income) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.get().equals(other.date.get()))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.get().equals(other.name.get()))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.get().equals(other.value.get()))
			return false;
		return true;
	}

}
