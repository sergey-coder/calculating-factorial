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
import ru.calculate.CalculateFactorial;
import ru.dao.impl.CalculationDaoImpl;
import ru.domain.Calculation;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ExecuteStatusEvent.class)
class ExecuteStatusEventTest {

    @Autowired
    ExecuteStatusEvent executeStatusEvent;

    @MockBean
    private CalculationDaoImpl calculationDaoImpl;

    /**
     * Проверяем формирование ответа при отсутствии вычисления с указанным Uid.
     */
    @Test
    void startEventNotUid() {
        RequestEvent request = RequestEvent.newBuilder().build();

        ResponseEvent responseEvent = executeStatusEvent.startEvent(request);
        Assertions.assertEquals("вычисление с данным uid не запущено", responseEvent.getMessage());
    }

    /**
     * Проверяем формирование ответа при статусе вычисления FINISHED.
     */
    @Test
    void startEventFINISHED() {
        RequestEvent request = RequestEvent.newBuilder()
                .setUid("testUid")
                .build();

        Calculation calculation = new Calculation();
        calculation.setStatusCalculation(ResponseEvent.StatusCalculation.FINISHED);
        calculation.setNumber(777);
        calculation.setResultCalculation("Very, very big number");

        Mockito.when(calculationDaoImpl.checkCalculationUid(request.getUid())).thenReturn(true);
        Mockito.when(calculationDaoImpl.findByUid(request.getUid())).thenReturn(calculation);
        ResponseEvent responseEvent = executeStatusEvent.startEvent(request);

        Assertions.assertEquals(
                "Завершено. Значение факториала " + calculation.getNumber() + " равно " + calculation.getResultCalculation(),
                responseEvent.getMessage()
        );
    }

    /**
     * Проверяем формирование ответа при статусе вычисления STOPPED.
     */
    @Test
    void startEventSTOPPED() {
        RequestEvent request = RequestEvent.newBuilder()
                .setUid("testUid")
                .build();

        Calculation calculation = new Calculation();
        calculation.setStatusCalculation(ResponseEvent.StatusCalculation.STOPPED);

        Mockito.when(calculationDaoImpl.checkCalculationUid(request.getUid())).thenReturn(true);
        Mockito.when(calculationDaoImpl.findByUid(request.getUid())).thenReturn(calculation);
        ResponseEvent responseEvent = executeStatusEvent.startEvent(request);

        Assertions.assertEquals(
                "Остановлено",
                responseEvent.getMessage()
        );
    }

    /**
     * Проверяем формирование ответа при статусе вычисления EXECUTING.
     */
    @Test
    void startEventEXECUTING() {
        RequestEvent request = RequestEvent.newBuilder()
                .setUid("testUid")
                .build();

        CalculateFactorial calculateFactorial = Mockito.mock(CalculateFactorial.class);

        Calculation calculation = new Calculation();
        calculation.setStatusCalculation(ResponseEvent.StatusCalculation.EXECUTING);
        calculation.setTreads(333);
        calculation.setCalculateFactorial(calculateFactorial);


        Mockito.when(calculationDaoImpl.checkCalculationUid(request.getUid())).thenReturn(true);
        Mockito.when(calculationDaoImpl.findByUid(request.getUid())).thenReturn(calculation);
        Mockito.when(calculateFactorial.getActiveThreadCount()).thenReturn(152);
        ResponseEvent responseEvent = executeStatusEvent.startEvent(request);

        Assertions.assertEquals(
                "Выполняется. Завершено потоков 181 из 333",
                responseEvent.getMessage()
        );
    }

    /**
     * Проверяем формирование ответа при статусе вычисления UNRECOGNIZED.
     */
    @Test
    void startEventDefault() {
        RequestEvent request = RequestEvent.newBuilder()
                .setUid("testUid")
                .build();

        Calculation calculation = new Calculation();
        calculation.setStatusCalculation(ResponseEvent.StatusCalculation.UNRECOGNIZED);

        Mockito.when(calculationDaoImpl.checkCalculationUid(request.getUid())).thenReturn(true);
        Mockito.when(calculationDaoImpl.findByUid(request.getUid())).thenReturn(calculation);
        ResponseEvent responseEvent = executeStatusEvent.startEvent(request);

        Assertions.assertEquals(
                "Статус вычисления опредилить не удалось",
                responseEvent.getMessage()
        );
    }

}