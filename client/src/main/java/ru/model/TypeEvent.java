package ru.model;

public enum TypeEvent {
    /**
     * Событие запускает вычисления на сервере.
     */
    START(1),

    /**
     * Событие останавливает вычисления на сервере.
     */
    STOP(2),

    /**
     * Событие запрашивает текущий статус вычисления на сервере.
     */
    GET_STATUS(3);

    private final int code;

    TypeEvent(int code) {
        this.code = code;
    }

    public static TypeEvent findByCode(int code) {
        for (TypeEvent item : TypeEvent.values()) {
            if (code == item.code) return item;
        }
        throw new IllegalArgumentException("Unknown TypeStatusLic: " + code);
    }

    public int getCode() {
        return this.code;
    }

}
