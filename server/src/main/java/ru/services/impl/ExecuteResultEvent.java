package ru.services.impl;

import org.springframework.stereotype.Service;
import ru.RequestEvent;
import ru.ResponseEvent;
import ru.calculate.domain.Calculation;
import ru.calculate.impl.CalculationDaoImpl;
import ru.services.ExecuteEvent;

/**
 * Реализует выполнение event Result.
 */
@Service
public class ExecuteResultEvent implements ExecuteEvent {

    private final CalculationDaoImpl calculationDaoImpl;

    /**
     * uid вычисления.
     */
    private String uid;

    public ExecuteResultEvent(CalculationDaoImpl calculationDaoImpl) {
        this.calculationDaoImpl = calculationDaoImpl;
    }

    /**
     * По uid находит запись о вычислении, проверяет статус вычисления, формирует ответ.
     *
     * @param request запрос от client по модели RequestEvent.
     * @return ответ по модели ResponseEvent.
     */
    @Override
    public ResponseEvent startEvent(RequestEvent request) {
        uid = request.getUid();

        if (!checkUid(uid)) {
            return createResponse(
                    "вычисление с данным uid не запущено",
                    ""
            );
        }

        Calculation calculation = calculationDaoImpl.findByUid(uid);
        return switch (calculation.getStatusCalculation()) {
            case EXECUTING -> createResponse(
                    "вычисления еще не завершены",
                    "результат вычислений еще не определен"
            );
            case STOPPED -> createResponse(
                    "вычисления остановлены",
                    ""
            );
            case FINISHED -> createResponse(
                    "результат вычислений получен",
                    calculation.getResultCalculation()
            );
            default -> createResponse(
                    "результат вычислений по данному event получить не удалось",
                    ""
            );
        };
    }

    /**
     * Проверяет, имеется ли запись о вычислении с данным Uid.
     *
     * @param requestUid - Uid вычисления.
     */
    private boolean checkUid(String requestUid) {
        return calculationDaoImpl.checkCalculationUid(requestUid);
    }

    /**
     * Создает ответ для инициатора запроса о результатах event
     * по моделе ResponseEvent.
     *
     * @param message           текст сообщения для передаче инициатору.
     * @param resultCalculation результат вычислений.
     * @return ответ по модели ResponseEvent.
     */
    private ResponseEvent createResponse(String message, String resultCalculation) {
        return ResponseEvent.newBuilder()
                .setUid(uid)
                .setMessage(message)
                .setResultCalculating(resultCalculation)
                .build();
    }
}


