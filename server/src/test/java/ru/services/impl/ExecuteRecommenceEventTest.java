package ru.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.RequestEvent;
import ru.ResponseEvent;
import ru.dao.ServiceRecommenceCalculate;
import ru.dao.dao_file.ReadFile;
import ru.dao.dao_file.ServiceReadWriteFile;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ExecuteRecommenceEvent.class)
class ExecuteRecommenceEventTest {

    @Autowired
    private ExecuteRecommenceEvent executeRecommenceEvent;

    @MockBean(ServiceReadWriteFile.class)
    ServiceRecommenceCalculate serviceRecommenceCalculate;

    @MockBean
    private ExecuteStartEvent executeStartEvent;

    @MockBean
    private ReadFile readFile;

    /**
     * Проверяем формирование ответа в ситуации когда Uid не найден.
     */
    @Test
    void startEventNotFoundUid() {
        RequestEvent request = RequestEvent.newBuilder().setUid("testUid").build();
        Mockito.when(readFile.checkCalculation("testUid")).thenReturn(false);
        ResponseEvent responseEvent = executeRecommenceEvent.startEvent(request);
        Assertions.assertEquals("вычесления не могут возобновлены", responseEvent.getMessage());
        Assertions.assertEquals("testUid", responseEvent.getUid());
    }

    /**
     * Проверяем формирование ответа в ситуации когда Uid найден.
     */
    @Test
    void startEventValidUid() {
        RequestEvent request = RequestEvent.newBuilder().setUid("testUid").build();
        Mockito.when(serviceRecommenceCalculate.checkCalculation("testUid")).thenReturn(true);

        ResponseEvent responseEvent = executeRecommenceEvent.startEvent(request);
        Assertions.assertEquals("вычесления возобновлены", responseEvent.getMessage());
        Assertions.assertEquals("testUid", responseEvent.getUid());
    }

    /**
     * Проверяем вызов методов класса readWriteLock и readFile.
     */
    @Test
    void startEventVerifyMethods() {
        RequestEvent request = RequestEvent.newBuilder().setUid("testUid").build();
        Mockito.when(serviceRecommenceCalculate.checkCalculation("testUid")).thenReturn(true);

        executeRecommenceEvent.startEvent(request);
        Mockito.verify(serviceRecommenceCalculate, Mockito.times(1)).checkCalculation(request.getUid());
        Mockito.verify(serviceRecommenceCalculate, Mockito.times(1)).getCalculation(request.getUid());
    }

}