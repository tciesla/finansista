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
@XmlType(propOrder = {"name", "value", "date"})
public class Income {

    private static final String INITIAL_NAME = "unknown";

    private StringProperty name;
	private ObjectProperty<BigDecimal> value;
	private ObjectProperty<LocalDate> date;

	public Income() {
        this(INITIAL_NAME, BigDecimal.ZERO);
    }

	public Income(String name, BigDecimal value) {
		this(name, value, LocalDate.now());
	}

	public Income(String name, BigDecimal value, LocalDate date) {
		this.name = new SimpleStringProperty(name);
		this.value = new SimpleObjectProperty<>(value);
		this.date = new SimpleObjectProperty<>(date);
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

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public ObjectProperty<LocalDate> date() {
        return date;
    }

}
