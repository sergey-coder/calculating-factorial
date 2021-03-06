package client.model;

/**
 * Перечень типов событий обрабатываемых client.
 */
public enum TypeEvent {
    /**
     * Событие запрашивает текущий статус вычисления на сервере.
     */
    GET_STATUS,

    /**
     * Событие останавливает вычисления на сервере.
     */
    STOP,

    /**
     * Событие возобнавляет вычисления на сервере.
     */
    RECOMMENCE,

    /**
     * Событие запрашивает текущий результат вычисления на сервере.
     */
    RESULT,

    /**
     * Событие инициирует старт вычислений на сервере.
     */
    START
}
