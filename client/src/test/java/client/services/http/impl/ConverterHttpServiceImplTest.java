package client.services.http.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import client.model.CalculatingRequest;
import client.model.CalculatingRespons;
import client.model.TypeEvent;
import client.services.grpc.GrpcService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ConverterHttpServiceImpl.class)
class ConverterHttpServiceImplTest {

    @MockBean
    private GrpcService grpcServiceMock;

    @Autowired
    private ConverterHttpServiceImpl converterHttpService;

    /**
     * Тестируем вызов метода sendGrpcRequest при валидных параметрах.
     */
    @Test
    void sendGrpcRequestValid() {
        CalculatingRequest request = new CalculatingRequest();
        request.setNumber(1);
        request.setThread(1);

        request.setTypeEvent(TypeEvent.START);
        converterHttpService.startEventCalculating(request);

        request.setUid("testUid");
        request.setTypeEvent(TypeEvent.STOP);
        converterHttpService.startEventCalculating(request);

        request.setTypeEvent(TypeEvent.RECOMMENCE);
        converterHttpService.startEventCalculating(request);

        request.setTypeEvent(TypeEvent.GET_STATUS);
        converterHttpService.startEventCalculating(request);

        request.setTypeEvent(TypeEvent.RESULT);
        converterHttpService.startEventCalculating(request);

        Mockito.verify(grpcServiceMock, Mockito.times(5)).sendGrpcRequest(request);
    }

    /**
     * Тестируем вызов метода sendGrpcRequest при не допустимых параметрах.
     */
    @Test
    void sendGrpcRequestNotValid() {
        CalculatingRespons calculatingRespons;

        CalculatingRequest requestNull = null;
        calculatingRespons = converterHttpService.startEventCalculating(requestNull);
        Assertions.assertEquals("не указан тип события TypeEvent", calculatingRespons.getMessage());

        CalculatingRequest requestObject = new CalculatingRequest();
        calculatingRespons = converterHttpService.startEventCalculating(requestObject);
        Assertions.assertEquals("не указан тип события TypeEvent", calculatingRespons.getMessage());

        CalculatingRequest requestNullNumber = new CalculatingRequest();
        requestNullNumber.setNumber(0);
        requestNullNumber.setThread(100);
        requestNullNumber.setTypeEvent(TypeEvent.START);
        calculatingRespons = converterHttpService.startEventCalculating(requestNullNumber);
        Assertions.assertEquals("введенное значение должно быть больше нуля", calculatingRespons.getMessage());

        CalculatingRequest requestNullThread = new CalculatingRequest();
        requestNullThread.setNumber(200);
        requestNullThread.setThread(0);
        requestNullThread.setTypeEvent(TypeEvent.START);
        calculatingRespons = converterHttpService.startEventCalculating(requestNullThread);
        Assertions.assertEquals("введенное значение должно быть больше нуля", calculatingRespons.getMessage());

        CalculatingRequest requestNullUid = new CalculatingRequest();
        requestNullUid.setTypeEvent(TypeEvent.STOP);
        calculatingRespons = converterHttpService.startEventCalculating(requestNullUid);
        Assertions.assertEquals("отсутствует uid вычисления", calculatingRespons.getMessage());
    }
}