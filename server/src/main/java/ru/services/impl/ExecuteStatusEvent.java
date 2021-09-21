package ru.services.impl;

import org.springframework.stereotype.Service;
import ru.RequestEvent;
import ru.ResponseEvent;
import ru.dao.impl.CalculationDaoImpl;
import ru.domain.Calculation;
import ru.services.ExecuteEvent;

/**
 * Реализует выполнение event Status.
 */
@Service
public class ExecuteStatusEvent implements ExecuteEvent {

    private final CalculationDaoImpl calculationDaoImpl;

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
        String uid = request.getUid();

        if (!checkUid(uid)) {
            return ResponseEvent.newBuilder()
                    .setUid(uid)
                    .setMessage("вычисление с данным uid не запущено")
                    .build();
        }

        Calculation calculation = calculationDaoImpl.findByUid(uid);
        String message;

        switch (calculation.getStatusCalculation()) {
            case EXECUTING -> {
                message = "Выполняется. Завершено потоков "
                        + (calculation.getTreads() - calculation.getCalculateFactorial().getActiveThreadCount())
                        + " из " + calculation.getTreads();
            }
            case FINISHED -> {
                message = "Завершено. Значение факториала " + calculation.getNumber() + " равно " + calculation.getResultCalculation();
            }

            case STOPPED -> {
                message = "Остановлено";
            }
            default -> {
                message = "Статус вычисления опредилить не удалось";
            }
        }

        return ResponseEvent.newBuilder()
                .setUid(uid)
                .setMessage(message)
                .setResultCalculating(calculation.getResultCalculation())
                .build();
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
