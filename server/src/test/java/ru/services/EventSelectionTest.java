package ru.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import ru.RequestEvent;
import ru.services.impl.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EventSelection.class)
class EventSelectionTest {

    @Autowired
    private EventSelection eventSelection;

    @MockBean
    private ExecuteStartEvent executeStartEvent;

    @MockBean
    private ExecuteStopEvent executeStopEvent;

    @MockBean
    private ExecuteStatusEvent executeStatusEvent;

    @MockBean
    private ExecuteRecommenceEvent executeRecommenceEvent;

    @MockBean
    private ExecuteResultEvent executeResultEvent;

    /**
     * Правильно ли по типу event определяется класс реализующий ExecuteEvent.
     */
    @Test
    void selectEventProcess() {
        ExecuteEvent executeEventRecommence = eventSelection.selectEventProcess(RequestEvent.TypeEvent.RECOMMENCE);
        Assertions.assertEquals(Boolean.TRUE, executeEventRecommence instanceof ExecuteRecommenceEvent);

        ExecuteEvent executeEventStatus = eventSelection.selectEventProcess(RequestEvent.TypeEvent.GET_STATUS);
        Assertions.assertEquals(Boolean.TRUE, executeEventStatus instanceof ExecuteStatusEvent);

        ExecuteEvent executeEventStart = eventSelection.selectEventProcess(RequestEvent.TypeEvent.START);
        Assertions.assertEquals(Boolean.TRUE, executeEventStart instanceof ExecuteStartEvent);

        ExecuteEvent executeEventStop = eventSelection.selectEventProcess(RequestEvent.TypeEvent.STOP);
        Assertions.assertEquals(Boolean.TRUE, executeEventStop instanceof ExecuteStopEvent);

        ExecuteEvent executeEventResult = eventSelection.selectEventProcess(RequestEvent.TypeEvent.RESULT);
        Assertions.assertEquals(Boolean.TRUE, executeEventResult instanceof ExecuteResultEvent);

        Assertions.assertThrows(HttpClientErrorException.class,()-> {eventSelection.selectEventProcess(RequestEvent.TypeEvent.UNRECOGNIZED);});
    }
}