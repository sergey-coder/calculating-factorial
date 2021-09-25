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
import ru.dao.impl.CalculationDaoImpl;
import ru.domain.Calculation;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ExecuteResultEvent.class)
class ExecuteResultEventTest {

    @Autowired
    private ExecuteResultEvent executeResultEvent;

    @MockBean
    private CalculationDaoImpl calculationDaoImpl;

    /**
     * Запускаем event без uid
     */
    @Test
    void startEventNotUid() {
        RequestEvent request = RequestEvent.newBuilder().build();
        ResponseEvent responseEvent = executeResultEvent.startEvent(request);

        Assertions.assertEquals("вычисление с данным uid не запущено", responseEvent.getMessage());
    }

    /**
     * Доступен ли класс CalculationDaoImpl.
     */
    @Test
    void startEventVerifyCalculationDaoImpl() {
        RequestEvent request = RequestEvent.newBuilder().setUid("testUid").build();
        executeResultEvent.startEvent(request);
        Mockito.verify(calculationDaoImpl, Mockito.times(1)).checkCalculationUid(request.getUid());

        Calculation calculation = new Calculation();
        calculation.setStatusCalculation(ResponseEvent.StatusCalculation.FINISHED);
        Mockito.when(calculationDaoImpl.findByUid(request.getUid())).thenReturn(calculation);
        Mockito.when(calculationDaoImpl.checkCalculationUid(request.getUid())).thenReturn(true);

        executeResultEvent.startEvent(request);
        Mockito.verify(calculationDaoImpl, Mockito.times(1)).findByUid(request.getUid());
    }

    /**
     * Проверяем формирование ответа с учетом  возможных статусов вычисления.
     */
    @Test
    void startEventValidParams() {
        RequestEvent request = RequestEvent.newBuilder().setUid("testUid").build();
        Mockito.when(calculationDaoImpl.checkCalculationUid(request.getUid())).thenReturn(true);

        Calculation calculation = new Calculation();
        calculation.setResultCalculation("777");
        calculation.setUid("testUid");
        Mockito.when(calculationDaoImpl.findByUid(request.getUid())).thenReturn(calculation);

        calculation.setStatusCalculation(ResponseEvent.StatusCalculation.FINISHED);
        ResponseEvent responseEventFINISHED = executeResultEvent.startEvent(request);
        Assertions.assertEquals("результат вычислений получен", responseEventFINISHED.getMessage());
        Assertions.assertEquals("777", responseEventFINISHED.getResultCalculating());
        Assertions.assertEquals("testUid", responseEventFINISHED.getUid());

        calculation.setStatusCalculation(ResponseEvent.StatusCalculation.EXECUTING);
        ResponseEvent responseEventEXECUTING = executeResultEvent.startEvent(request);
        Assertions.assertEquals("вычисления еще не завершены", responseEventEXECUTING.getMessage());
        Assertions.assertEquals("результат вычислений еще не определен", responseEventEXECUTING.getResultCalculating());
        Assertions.assertEquals("testUid", responseEventEXECUTING.getUid());

        calculation.setStatusCalculation(ResponseEvent.StatusCalculation.STOPPED);
        ResponseEvent responseEventSTOPPED = executeResultEvent.startEvent(request);
        Assertions.assertEquals("вычисления остановлены", responseEventSTOPPED.getMessage());
        Assertions.assertEquals("", responseEventSTOPPED.getResultCalculating());
        Assertions.assertEquals("testUid", responseEventSTOPPED.getUid());

        calculation.setStatusCalculation(ResponseEvent.StatusCalculation.UNRECOGNIZED);
        ResponseEvent responseEventUNRECOGNIZED = executeResultEvent.startEvent(request);
        Assertions.assertEquals("результат вычислений по данному event получить не удалось", responseEventUNRECOGNIZED.getMessage());
        Assertions.assertEquals("", responseEventUNRECOGNIZED.getResultCalculating());
        Assertions.assertEquals("testUid", responseEventUNRECOGNIZED.getUid());
    }
}