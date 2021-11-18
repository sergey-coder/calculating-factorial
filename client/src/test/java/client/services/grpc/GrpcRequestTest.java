package client.services.grpc;

import client.model.TypeEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ru.RequestEvent;
import client.model.CalculatingRequest;

/**
 * Тестируем создание RequestEvent сущностей.
 * Проверяет заполняемость null полей, проверка которых не происходит на стадии принятия Http запроса.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GrpcRequest.class)
class GrpcRequestTest {

    @Autowired
    GrpcRequest grpcRequest;

    /**
     * Тестируем создание RequestEvent события Start
     */
    @Test
    void createGrpcRequestStart() {
        CalculatingRequest calculatingRequest = new CalculatingRequest();
        calculatingRequest.setTypeEvent(TypeEvent.START);
        calculatingRequest.setThread(10);
        calculatingRequest.setNumber(20);
        System.out.println("!!!!!!!!!!");
        System.out.println(calculatingRequest.getTypeEvent());
        RequestEvent request = grpcRequest.createGrpcRequest(calculatingRequest);
        System.out.println(request);
        Assertions.assertEquals(RequestEvent.TypeEvent.START, request.getTypeEvent());
        Assertions.assertEquals(10, request.getTreads());
        Assertions.assertEquals(20, request.getNumber());
        Assertions.assertEquals("", request.getUid());
    }

    /**
     * Тестируем создание RequestEvent события STOP
     */
    @Test
    void createGrpcRequestSTOP() {
        CalculatingRequest calculatingRequest = new CalculatingRequest();
        calculatingRequest.setTypeEvent(TypeEvent.STOP);
        calculatingRequest.setUid("testUid");


        RequestEvent request = grpcRequest.createGrpcRequest(calculatingRequest);
        Assertions.assertEquals(RequestEvent.TypeEvent.STOP, request.getTypeEvent());
        Assertions.assertEquals(0, request.getTreads());
        Assertions.assertEquals(0, request.getNumber());
        Assertions.assertEquals("testUid", request.getUid());
    }

    /**
     * Тестируем создание RequestEvent события GET_STATUS
     */
    @Test
    void createGrpcRequestStatus() {
        CalculatingRequest calculatingRequest = new CalculatingRequest();
        calculatingRequest.setTypeEvent(TypeEvent.GET_STATUS);

        RequestEvent request = grpcRequest.createGrpcRequest(calculatingRequest);
        Assertions.assertEquals(RequestEvent.TypeEvent.GET_STATUS, request.getTypeEvent());
        Assertions.assertEquals(0, request.getTreads());
        Assertions.assertEquals(0, request.getNumber());
        Assertions.assertEquals("", request.getUid());
    }

    /**
     * Тестируем создание RequestEvent события RECOMMENCE
     */
    @Test
    void createGrpcRequestRECOMMENCE() {
        CalculatingRequest calculatingRequest = new CalculatingRequest();
        calculatingRequest.setTypeEvent(TypeEvent.RECOMMENCE);

        RequestEvent request = grpcRequest.createGrpcRequest(calculatingRequest);
        Assertions.assertEquals(RequestEvent.TypeEvent.RECOMMENCE, request.getTypeEvent());
        Assertions.assertEquals(0, request.getTreads());
        Assertions.assertEquals(0, request.getNumber());
        Assertions.assertEquals("", request.getUid());
    }

    /**
     * Тестируем создание RequestEvent события RESULT.
     */
    @Test
    void createGrpcRequestRESULT() {
        CalculatingRequest calculatingRequest = new CalculatingRequest();
        calculatingRequest.setTypeEvent(TypeEvent.RESULT);

        RequestEvent request = grpcRequest.createGrpcRequest(calculatingRequest);
        Assertions.assertEquals(RequestEvent.TypeEvent.RESULT, request.getTypeEvent());
        Assertions.assertEquals(0, request.getTreads());
        Assertions.assertEquals(0, request.getNumber());
        Assertions.assertEquals("", request.getUid());
    }

}