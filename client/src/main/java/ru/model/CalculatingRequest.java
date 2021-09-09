package ru.model;

/**
 * Модель для формирования запроса от пользователя.
 */
public class CalculatingRequest {

    private Integer number;

    private Integer treads;

    private String uid;

    private Boolean statusCalculating;

    private String urlRequest;

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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrlRequest() {
        return urlRequest;
    }

    public void setUrlRequest(String urlRequest) {
        this.urlRequest = urlRequest;
    }

    @Override
    public String toString() {
        return "CalculatingRequest{" +
                "number=" + number +
                ", treads=" + treads +
                '}';
    }

}
