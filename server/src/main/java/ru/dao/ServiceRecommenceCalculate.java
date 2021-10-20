package ru.dao;

import ru.calculate.domain.Calculation;

/**
 * Интерфейс для классов реализующих механизм сохранения данных о вычислении.
 */
public interface ServiceRecommenceCalculate {

    /**
     * Получает сохраненные данные о вычислении.
     *
     * @param uid идентификатор вычисления.
     * @return данные о вычислении по модели Calculation.
     */
    Calculation getCalculation(String uid);

    /**
     * Проверяет наличие сохраненного вычисления.
     *
     * @param uid идентификатор вычисления.
     * @return true если вычисление имеется.
     */
    boolean checkCalculation(String uid);

    /**
     * Сохраняет данные о вычислении.
     *
     * @param calculation данные о вычислении по модели Calculation.
     */
    void saveDataCalculating(Calculation calculation);

    /**
     * Удаляет данные о вычислении.
     *
     * @param uid идентификатор вычисления.
     */
    void deleteDataCalculating(String uid);
}
