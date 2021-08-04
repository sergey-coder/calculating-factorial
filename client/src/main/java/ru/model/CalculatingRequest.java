package ru.model;


public class CalculatingRequest {

    private Integer number;

    private Integer treads;

    private Boolean statusCalculating;

    public CalculatingRequest(Integer number, Integer treads) {
        this.number = number;
        this.treads = treads;
        this.statusCalculating = false;
    }

    public CalculatingRequest() {}

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

    public Boolean getStatusCalculating() {
        return statusCalculating;
    }

    public void setStatusCalculating(Boolean statusCalculating) {
        this.statusCalculating = statusCalculating;
    }

    @Override
    public String toString() {
        return "CalculatingRequest{" +
                "number=" + number +
                ", treads=" + treads +
                '}';
    }

}
