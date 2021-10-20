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
import ru.calculate.domain.Calculation;
import ru.calculate.impl.CalculationDaoImpl;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ExecuteStopEvent.class)
class ExecuteStopEventTest {

    @Autowired
    ExecuteStopEvent executeStopEvent;

    @MockBean
    private CalculationDaoImpl calculationDaoImpl;

    /**
     * Проверяем формирование ответа при отсутствии вычисления с указанным Uid.
     */
    @Test
    void startEventNotUid() {
        RequestEvent request = RequestEvent.newBuilder().build();

        ResponseEvent responseEvent = executeStopEvent.startEvent(request);
        Assertions.assertEquals("вычисление с данным uid не запущено", responseEvent.getMessage());
    }

    /**
     * Проверяем формирование ответа при статусе вычисления FINISHED.
     */
    @Test
    void startEventStatusFINISHED() {
        RequestEvent request = RequestEvent.newBuilder()
                .setUid("testUid")
                .build();

        Calculation calculation = new Calculation();
        calculation.setStatusCalculation(ResponseEvent.StatusCalculation.FINISHED);

        Mockito.when(calculationDaoImpl.checkCalculationUid(request.getUid())).thenReturn(true);
        Mockito.when(calculationDaoImpl.findByUid(request.getUid())).thenReturn(calculation);

        ResponseEvent responseEvent = executeStopEvent.startEvent(request);
        Assertions.assertEquals("вычесления не могут остановлены", responseEvent.getMessage());
    }

    /**
     * Проверяем формирование ответа при статусе вычисления EXECUTING.
     */
    @Test
    void startEventStatusEXECUTING() {
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

        ResponseEvent responseEvent = executeStopEvent.startEvent(request);
        Assertions.assertEquals("вычесления остановлены", responseEvent.getMessage());
    }

}