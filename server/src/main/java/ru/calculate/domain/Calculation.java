package ru.calculate.domain;

import ru.ResponseEvent;
import ru.calculate.CalculateFactorial;

/**
 * Модель для хранения данных о вычислениях.
 */
public class Calculation {

    private String uid;
    private ResponseEvent.StatusCalculation statusCalculation;
    private String resultCalculation = "данных о результатах вычисления не имеется";
    private int number;
    private int treads;
    private CalculateFactorial calculateFactorial;

    public Calculation() {
    }

    public Calculation(
            String uid,
            ResponseEvent.StatusCalculation statusCalculation,
            String resultCalculation,
            int number,
            int treads,
            CalculateFactorial calculateFactorial) {
        this.uid = uid;
        this.statusCalculation = statusCalculation;
        this.resultCalculation = resultCalculation;
        this.number = number;
        this.treads = treads;
        this.calculateFactorial = calculateFactorial;
    }

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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getTreads() {
        return treads;
    }

    public void setTreads(Integer treads) {
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
        return "Calculation{" +
                "uid='" + uid + '\'' +
                ", statusCalculation=" + statusCalculation +
                ", resultCalculation='" + resultCalculation + '\'' +
                ", number=" + number +
                ", treads=" + treads +
                ", calculateFactorial=" + calculateFactorial +
                '}';
    }
}

