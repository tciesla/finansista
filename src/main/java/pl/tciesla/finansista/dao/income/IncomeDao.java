package pl.tciesla.finansista.dao.income;

import pl.tciesla.finansista.dao.BaseDao;
import pl.tciesla.finansista.model.income.Income;

import java.util.List;

public interface IncomeDao extends BaseDao<Income, Integer> {

    @Override
    void persist(Income income);

    @Override
    void update(Income income);

    @Override
    void delete(Integer id);

    @Override
    List<Income> fetchAll();
}
