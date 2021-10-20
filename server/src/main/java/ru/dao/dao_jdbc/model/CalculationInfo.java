package ru.dao.dao_jdbc.model;

/**
 * Модель для хранения данных необходимых для рестарта вычислений.
 */
public class CalculationInfo {

    private int id;
    private String uid;
    private int number;
    private int treads;

    public CalculationInfo() {
    }

    public CalculationInfo(int id, String uid, int number, int treads) {
        this.id = id;
        this.uid = uid;
        this.number = number;
        this.treads = treads;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
}
