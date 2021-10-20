package ru.services.impl;

import org.springframework.stereotype.Service;
import ru.RequestEvent;
import ru.ResponseEvent;
import ru.calculate.domain.Calculation;
import ru.dao.ServiceRecommenceCalculate;
import ru.services.ExecuteEvent;


/**
 * Реализует выполнение event Recommence.
 */
@Service
public class ExecuteRecommenceEvent implements ExecuteEvent {

    private final ExecuteStartEvent executeStartEvent;
    private final ServiceRecommenceCalculate serviceRecommenceCalculate;

    /**
     * uid вычисления.
     */
    private String uid;

    public ExecuteRecommenceEvent(ExecuteStartEvent executeStartEvent, ServiceRecommenceCalculate serviceRecommenceCalculate) {
        this.executeStartEvent = executeStartEvent;
        this.serviceRecommenceCalculate = serviceRecommenceCalculate;
    }

    /**
     * Проверяет наличие незавершенных вычислений по текущему запросу,
     * при наличии, через инстанс ExecuteStartEvent, начинает вычисления заново.
     *
     * @param request запрос от client по модели RequestEvent.
     * @return ответ по модели ResponseEvent.
     */
    @Override
    public ResponseEvent startEvent(RequestEvent request) {
        uid = request.getUid();

        if (serviceRecommenceCalculate.checkCalculation(uid)) {
            Calculation calculation = serviceRecommenceCalculate.getCalculation(uid);
            executeStartEvent.startCalculation(calculation);
            return createResponseEvent("вычесления возобновлены");
        }
        return createResponseEvent("вычесления не могут возобновлены");
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
}
