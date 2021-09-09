package ru.model;

import java.util.Objects;

/**
 * Модель для формирования ответа конечному пользователю.
 */
public class CalculatingRespons {

    private String uid;

    private String message;

    private String resultCaculating;

    public CalculatingRespons() {}

    public CalculatingRespons(String uid, String message, String resultCaculating) {
        this.uid = uid;
        this.message = message;
        this.resultCaculating = resultCaculating;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalculatingRespons that = (CalculatingRespons) o;
        return Objects.equals(uid, that.uid) && Objects.equals(message, that.message) && Objects.equals(resultCaculating, that.resultCaculating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, message, resultCaculating);
    }
}
