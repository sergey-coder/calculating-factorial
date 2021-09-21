package ru.dao;

public interface CalculationDao <T> {

    void addNewCalculation(T entity);

    T findByUid(String uid);

    boolean checkCalculationUid(String uid);

    void updateCalculation(T entity);
}
