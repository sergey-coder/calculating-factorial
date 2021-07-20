package model;

public class CalculatingRequest {
    private Integer id;
    private Integer number;
    private Integer treads;
    private Boolean processCalculat;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Boolean getProcessCalculat() {
        return processCalculat;
    }

    public void setProcessCalculat(Boolean processCalculat) {
        this.processCalculat = processCalculat;
    }
}
