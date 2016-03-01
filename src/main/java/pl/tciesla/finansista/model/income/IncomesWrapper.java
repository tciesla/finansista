package pl.tciesla.finansista.model.income;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement(name = "incomes")
@XmlAccessorType(XmlAccessType.FIELD)
public class IncomesWrapper {

    @XmlElement(name = "income")
    private List<Income> incomes;

    public IncomesWrapper() {
        incomes = new LinkedList<>();
    }

    public IncomesWrapper(List<Income> incomes) {
        this.incomes = incomes;
    }

    public List<Income> getIncomes() {
        return incomes;
    }
}
