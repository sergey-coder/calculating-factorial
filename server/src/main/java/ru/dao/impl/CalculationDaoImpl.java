package ru.dao.impl;

import org.springframework.stereotype.Component;
import ru.dao.CalculationDao;
import ru.domain.Calculation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Совершает операции CRUD над колецией с сохраненной информацией о вычеслениях.
 */
@Component
public class CalculationDaoImpl implements CalculationDao<Calculation> {

    private final List<Calculation> listCalculation;

    {
        listCalculation = new ArrayList<>();
    }

    /**
     * Добавляет в колекцию запись о вычислении.
     *
     * @param calculation новая запись о вычислении.
     */
    public void addNewCalculation(Calculation calculation) {
        listCalculation.add(calculation);
    }

    /**
     * Находит в колекции запись о вычислении по ее uid.
     *
     * @param uid идентификатор вычисления.
     * @return запись о вычислении.
     */
    public Calculation findByUid(String uid) {
        return listCalculation.stream().filter(calculation -> calculation.getUid().equals(uid)).findFirst().get();
    }

    /**
     * Проверяет наличие записи о вычислении с данным uid.
     *
     * @param uid идентификатор вычисления.
     * @return true если запись имеется.
     */
    public boolean checkCalculationUid(String uid) {
        Optional<Calculation> calculationEntity = listCalculation.stream().filter(calculation -> calculation.getUid().equals(uid)).findFirst();
        return calculationEntity.isPresent();
    }

    /**
     * Обновляет информацию о вычислении.
     *
     * @param calculation запись о вычислении с новой информацией.
     */
    public void updateCalculation(Calculation calculation) {
        Calculation entityForUpdate = findByUid(calculation.getUid());
        if (calculation.getResultCalculation() != null) {
            entityForUpdate.setResultCalculation(calculation.getResultCalculation());
        }

        if (calculation.getStatusCalculation() != null) {
            entityForUpdate.setStatusCalculation(calculation.getStatusCalculation());
        }
    }

}
