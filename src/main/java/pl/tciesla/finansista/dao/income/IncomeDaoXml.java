package pl.tciesla.finansista.dao.income;

import pl.tciesla.finansista.model.income.Income;
import pl.tciesla.finansista.model.income.IncomesWrapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;
import java.util.stream.IntStream;

public class IncomeDaoXml implements IncomeDao {

    private static final IncomeDao instance = new IncomeDaoXml();

    // TODO use other filename source
    private File incomesFile = new File("incomes.xml");
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    private IncomeDaoXml() {
        try {
            JAXBContext context = JAXBContext.newInstance(IncomesWrapper.class);
            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            unmarshaller = context.createUnmarshaller();
            createEmptyFileIfNotExists();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private void createEmptyFileIfNotExists() throws JAXBException {
        if (!incomesFile.exists()) {
            marshaller.marshal(new IncomesWrapper(), incomesFile);
        }
    }

    public static IncomeDao getInstance() {
        return instance;
    }

    @Override
    public void persist(Income income) {
        List<Income> incomes = fetchAll();
        incomes.add(income);
        persistIncomes(incomes);
    }

    @Override
    public void update(Income income) {
        List<Income> incomes = fetchAll();
        incomes.set(income.getId(), income);
        persistIncomes(incomes);
    }

    @Override
    public void delete(Integer id) {
        List<Income> incomes = fetchAll();
        incomes.remove(id.intValue());
        persistIncomes(incomes);
    }

    @Override
    public List<Income> fetchAll() {
        try {
            IncomesWrapper incomesWrapper = (IncomesWrapper) unmarshaller.unmarshal(incomesFile);
            List<Income> incomes = incomesWrapper.getIncomes();
            incomes.sort((i1, i2) -> i2.getDate().compareTo(i1.getDate()));
            IntStream.range(0, incomes.size()).forEach(i -> incomes.get(i).setId(i));
            return incomes;
        } catch (JAXBException e) {
            // TODO use logger here
            e.printStackTrace();
        }
        return null;
    }

    private void persistIncomes(List<Income> incomes) {
        try {
            incomes.sort((i1, i2) -> i2.getDate().compareTo(i1.getDate()));
            marshaller.marshal(new IncomesWrapper(incomes), incomesFile);
        } catch (JAXBException e) {
            // TODO use logger here
            e.printStackTrace();
        }
    }
}
