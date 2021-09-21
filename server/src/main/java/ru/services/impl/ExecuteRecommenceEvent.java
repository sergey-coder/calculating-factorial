package ru.services.impl;

import org.springframework.stereotype.Service;
import ru.RequestEvent;
import ru.ResponseEvent;
import ru.services.ExecuteEvent;
import ru.util.WriteToFile;

/**
 * Реализует выполнение event Recommence.
 */
@Service
public class ExecuteRecommenceEvent implements ExecuteEvent {

    private final ExecuteStartEvent executeStartEvent;

    public ExecuteRecommenceEvent(ExecuteStartEvent executeStartEvent) {
        this.executeStartEvent = executeStartEvent;
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
        String uid = request.getUid();

        if (WriteToFile.checkCalculation(uid)) {
            executeStartEvent.startCalculation(WriteToFile.getCalculation(uid));
            return ResponseEvent.newBuilder()
                    .setUid(uid)
                    .setMessage("вычесления возобновлены")
                    .build();
        }
        return ResponseEvent.newBuilder()
                .setUid(uid)
                .setMessage("вычесления не могут возобновлены")
                .build();
    }
}
