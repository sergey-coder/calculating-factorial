package ru.services;


import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 * Клас производит вычесление факториала
 */
@Component
public class Calculate {

    /**
     * Флаг на остановку вычеслений
     * пока флаг == false вычесление идет
     */
    private   Boolean stopCalculate = false;

    /**
     * аналог переменной i  в цикле for
     */
    private int iteratorCalculat = 1;

    /**
     * переменная для хранения промежуточного результата
     * вынесено за пределы метода для возможности получения
     * промежуточного результата
     */
    private BigInteger result = BigInteger.ONE;

    public void calculatingFactorial(int startedNumber){
        while (!stopCalculate || iteratorCalculat <= startedNumber){
            result = result.multiply(BigInteger.valueOf(iteratorCalculat));
            iteratorCalculat++;
        }
        stopCalculate = true;
    }

    /**
     * Выдает текущий статус вычеслений
     */
    public Boolean getStopCalculate() {
        return stopCalculate;
    }

    /**
     * Принудительная остановка вычеслений
     */
    public void setStopCalculate(Boolean stopCalculate) {
        this.stopCalculate = stopCalculate;
    }

    /**
     * Выдает текущий результат вычеслений
     */
    public BigInteger getResult() {
        return result;
    }
}
