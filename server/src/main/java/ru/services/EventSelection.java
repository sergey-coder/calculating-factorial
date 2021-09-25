package ru.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.RequestEvent;
import ru.services.impl.*;

/**
 * Отвечает за выбор класса реализующего логику для каждого типа Event.
 */
@Service
public class EventSelection {

    private final ExecuteStartEvent executeStartEvent;
    private final ExecuteStopEvent executeStopEvent;
    private final ExecuteStatusEvent executeStatusEvent;
    private final ExecuteRecommenceEvent executeRecommenceEvent;
    private final ExecuteResultEvent executeResultEvent;

    public EventSelection(ExecuteStartEvent executeStartEvent,
                          ExecuteStopEvent executeStopEvent,
                          ExecuteStatusEvent executeStatusEvent,
                          ExecuteRecommenceEvent executeRecommenceEvent,
                          ExecuteResultEvent executeResultEvent) {
        this.executeStartEvent = executeStartEvent;
        this.executeStopEvent = executeStopEvent;
        this.executeStatusEvent = executeStatusEvent;
        this.executeRecommenceEvent = executeRecommenceEvent;
        this.executeResultEvent = executeResultEvent;
    }

    /**
     * По типу пришедшего Event, определяет реализацию интерфейса ExecuteEvent.
     *
     * @param typeEvent тип Event.
     * @return bean класса реализующего интерфейс ExecuteEvent
     */
    public ExecuteEvent selectEventProcess(RequestEvent.TypeEvent typeEvent) {
        return switch (typeEvent) {
            case START -> executeStartEvent;
            case STOP -> executeStopEvent;
            case GET_STATUS -> executeStatusEvent;
            case RECOMMENCE -> executeRecommenceEvent;
            case RESULT -> executeResultEvent;
            default -> throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "не известный тип запроса");
        };
    }
}
