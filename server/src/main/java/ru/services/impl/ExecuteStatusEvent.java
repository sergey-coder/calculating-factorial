package ru.services.impl;

import org.springframework.stereotype.Service;
import ru.RequestEvent;
import ru.ResponseEvent;
import ru.calculate.domain.Calculation;
import ru.calculate.impl.CalculationDaoImpl;
import ru.services.ExecuteEvent;

/**
 * Реализует выполнение event Status.
 */
@Service
public class ExecuteStatusEvent implements ExecuteEvent {

    private final CalculationDaoImpl calculationDaoImpl;

    /**
     * uid вычисления.
     */
    private String uid;

    public ExecuteStatusEvent(CalculationDaoImpl calculationDaoImpl) {
        this.calculationDaoImpl = calculationDaoImpl;
    }

    /**
     * Находит по uid вычисления его данные, в зависимости от типа status формирует ответ,
     * при наличии результата вычислений передает его инициатору.
     *
     * @param request запрос от client по модели RequestEvent.
     * @return ответ по модели ResponseEvent.
     */
    @Override
    public ResponseEvent startEvent(RequestEvent request) {
        uid = request.getUid();

        if (!checkUid(uid)) {
            return createResponseEvent("вычисление с данным uid не запущено");
        }
        Calculation calculation = calculationDaoImpl.findByUid(uid);

        return createResponseEvent(createMessage(calculation));
    }


    /**
     * Формирует ответ для инициатора.
     *
     * @param message сообщение для инициатора.
     * @return ответ по модели ResponseEvent.
     */
    private ResponseEvent createResponseEvent(String message) {
        return ResponseEvent.newBuilder()
                .setUid(uid)
                .setMessage(message)
                .build();
    }

    /**
     * Формирует текст сообщения.
     *
     * @param calculation сохраненные данные о вычислении.
     * @return сообщение для инициатора запроса.
     */
    private String createMessage(Calculation calculation) {
        switch (calculation.getStatusCalculation()) {
            case EXECUTING -> {
                return "Выполняется. Завершено потоков "
                        + (calculation.getTreads() - calculation.getCalculateFactorial().getActiveThreadCount())
                        + " из " + calculation.getTreads();
            }
            case FINISHED -> {
                return "Завершено. Значение факториала " + calculation.getNumber() + " равно " + calculation.getResultCalculation();
            }

            case STOPPED -> {
                return "Остановлено";
            }
            default -> {
                return "Статус вычисления опредилить не удалось";
            }
        }
    }

    /**
     * Проверяет, имеется ли запись о вычислении с данным Uid.
     *
     * @param requestUid - Uid вычисления.
     */
    private boolean checkUid(String requestUid) {
        return calculationDaoImpl.checkCalculationUid(requestUid);
    }
}
