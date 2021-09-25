package ru.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.RequestEvent;
import ru.ResponseEvent;
import ru.dao.impl.CalculationDaoImpl;
import ru.domain.Calculation;
import ru.file.WriteFile;
import ru.util.UidGenerator;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ExecuteStartEvent.class)
class ExecuteStartEventTest {

    @Autowired
    private ExecuteStartEvent executeStartEvent;

    @MockBean
    private WriteFile readFile;

    @MockBean
    private CalculationDaoImpl calculationDaoImpl;

    @Captor
    ArgumentCaptor<Calculation> saveCalculation;


    /**
     * Проверяем генерацию ответа и генерацию uid.
     */
    @Test
    void startEventCreateUid() {
        RequestEvent request = RequestEvent.newBuilder()
                .setNumber(10)
                .setTreads(3)
                .build();

        ResponseEvent responseEvent = executeStartEvent.startEvent(request);

        Assertions.assertEquals("вычисления успешно начаты", responseEvent.getMessage());
        Assertions.assertEquals(5, responseEvent.getUid().length());
    }

    /**
     * Проверяем провильность формирования calculation,
     * формируется ли CalculateFactorial инстанс,
     * сохраняется ли он в Calculation.
     */
    @Test
    void startCalculation() {
        Calculation calculation = new Calculation();
        calculation.setUid(UidGenerator.generate());
        calculation.setNumber(20);
        calculation.setTreads(17);
        calculation.setStatusCalculation(ResponseEvent.StatusCalculation.EXECUTING);

        executeStartEvent.startCalculation(calculation);
        Mockito.verify(calculationDaoImpl).addNewCalculation(saveCalculation.capture());

        Assertions.assertNotNull(saveCalculation.getValue().getCalculateFactorial());
    }

}