package ru.model;

/**
 * Модель для формирования ответа конечному пользователю.
 */
public class CalculatingRespons {

    private String uid;

    private String message;

    private String resultCaculating = "результат вычислений еще не определен";

    public CalculatingRespons() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResultCaculating() {
        return resultCaculating;
    }

    public void setResultCaculating(String resultCaculating) {
        this.resultCaculating = resultCaculating;
    }
}
