package ru.services.impl;

import org.springframework.stereotype.Service;
import ru.RequestEvent;
import ru.ResponseEvent;
import ru.calculate.domain.Calculation;
import ru.calculate.impl.CalculationDaoImpl;
import ru.services.ExecuteEvent;

/**
 * Реализует выполнение event Stop.
 */
@Service
public class ExecuteStopEvent implements ExecuteEvent {

    private final CalculationDaoImpl calculationDaoImpl;

    /**
     * uid вычисления.
     */
    private String uid;

    public ExecuteStopEvent(CalculationDaoImpl calculationDaoImpl) {
        this.calculationDaoImpl = calculationDaoImpl;
    }

    /**
     * Останавливает вычисления.
     * Запрашивает из сохраненных данных о вычислении ссылку на инстанс CalculateFactorial,
     * через данную ссылку обращается к методу stopCalculation(), который и останавливает вычисления.
     * Устанавливает статус вычисления STOPPED.
     *
     * @param request запрос от client по модели RequestEvent.
     * @return ответ по модели ResponseEvent.
     */
    @Override
    public ResponseEvent startEvent(RequestEvent request) {
        uid = request.getUid();

        if (!checkUid(uid)) {
            return createResponse("вычисление с данным uid не запущено");
        }

        Calculation calculation = calculationDaoImpl.findByUid(uid);

        if (calculation.getStatusCalculation() == ResponseEvent.StatusCalculation.EXECUTING) {
            calculation.setStatusCalculation(ResponseEvent.StatusCalculation.STOPPED);
            calculationDaoImpl.updateCalculation(calculation);
            calculation.getCalculateFactorial().stopCalculation();
            return createResponse("вычесления остановлены");
        }
        return createResponse("вычесления не могут остановлены");
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
     * @param message текст сообщения для передаче инициатору.
     * @return ответ по модели ResponseEvent.
     */
    private ResponseEvent createResponse(String message) {
        return ResponseEvent.newBuilder()
                .setUid(uid)
                .setMessage(message)
                .build();
    }

}
