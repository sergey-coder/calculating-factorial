package ru.services.impl;

import org.springframework.stereotype.Service;
import ru.RequestEvent;
import ru.ResponseEvent;
import ru.domain.Calculation;
import ru.file.ReadFile;
import ru.services.ExecuteEvent;


/**
 * Реализует выполнение event Recommence.
 */
@Service
public class ExecuteRecommenceEvent implements ExecuteEvent {

    private final ExecuteStartEvent executeStartEvent;
    private final ReadFile readFile;

    public ExecuteRecommenceEvent(ExecuteStartEvent executeStartEvent, ReadFile readFile) {
        this.executeStartEvent = executeStartEvent;
        this.readFile = readFile;
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

        if (readFile.checkCalculation(uid)) {
            Calculation calculation = readFile.getCalculation(uid);
            executeStartEvent.startCalculation(calculation);

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
