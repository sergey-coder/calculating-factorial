package ru.calculate;

import ru.calculate.domain.Calculation;

public interface CalculationDao {

    void addNewCalculation(Calculation entity);

    Calculation findByUid(String uid);

    boolean checkCalculationUid(String uid);

    void updateCalculation(Calculation entity);
}

