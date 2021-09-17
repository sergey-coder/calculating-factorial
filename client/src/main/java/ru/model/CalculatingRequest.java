package ru.model;

/**
 * Модель для формирования запроса от пользователя.
 */
public class CalculatingRequest {

    private Integer number;

    private Integer treads;

    private String uid;

    public CalculatingRequest(Integer number, Integer treads) {
        this.number = number;
        this.treads = treads;
    }

    public CalculatingRequest() {
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "CalculatingRequest{" +
                "number=" + number +
                ", treads=" + treads +
                '}';
    }

}
