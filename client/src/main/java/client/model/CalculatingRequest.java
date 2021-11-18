package client.model;

/**
 * Модель для формирования запроса от пользователя.
 */
public class CalculatingRequest {

    /**
     * Число для вычисления его факториала.
     */
    private Integer number;

    /**
     * Количество потоков для производства вычисления.
     */
    private Integer thread;

    /**
     * Идентификатор вычисления.
     */
    private String uid;

    /**
     * Тип события, для определения типа операции производимой над вычислением или его результатом.
     */
    private TypeEvent typeEvent;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getThread() {
        return thread;
    }

    public void setThread(Integer thread) {
        this.thread = thread;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public TypeEvent getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(TypeEvent typeEvent) {
        this.typeEvent = typeEvent;
    }

    @Override
    public String toString() {
        return "CalculatingRequest{" +
                "number=" + number +
                ", treads=" + thread +
                '}';
    }

}
