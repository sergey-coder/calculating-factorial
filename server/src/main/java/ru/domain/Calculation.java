package ru.domain;

import ru.ResponseEvent;
import ru.calculate.CalculateFactorial;

/**
 * Модель для хранения данных о вычислениях.
 */
public class Calculation {

    private String uid;
    private ResponseEvent.StatusCalculation statusCalculation;
    private String resultCalculation = "результат вычислений еще не определен";
    private int number;
    private int treads;
    private CalculateFactorial calculateFactorial;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ResponseEvent.StatusCalculation getStatusCalculation() {
        return statusCalculation;
    }

    public void setStatusCalculation(ResponseEvent.StatusCalculation statusCalculation) {
        this.statusCalculation = statusCalculation;
    }

    public String getResultCalculation() {
        return resultCalculation;
    }

    public void setResultCalculation(String resultCalculation) {
        this.resultCalculation = resultCalculation;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTreads() {
        return treads;
    }

    public void setTreads(int treads) {
        this.treads = treads;
    }

    public CalculateFactorial getCalculateFactorial() {
        return calculateFactorial;
    }

    public void setCalculateFactorial(CalculateFactorial calculateFactorial) {
        this.calculateFactorial = calculateFactorial;
    }

    @Override
    public String toString() {
        return  uid +
                "," + number +
                "," + treads;
    }

}

