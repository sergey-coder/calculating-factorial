package ru.services.grpc.impl;

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
import ru.model.CalculatingRequest;
import ru.model.CalculatingRespons;
import ru.model.TypeEvent;
import ru.services.grpc.GrpcRequest;
import ru.services.grpc.SendRequestToGrpcServer;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GrpcServiceEventImpl.class)
class GrpcServiceEventImplTest {

    @Autowired
    GrpcServiceEventImpl grpcServiceEvent;

    @MockBean
    SendRequestToGrpcServer sendRequestToGrpcServerMock;

    @MockBean
    GrpcRequest grpcRequest;

    /**
     * Тестируем вызов метода createGrpcRequest().
     */
    @Test
    void callGrpcRequest() {
        CalculatingRequest request = new CalculatingRequest();
        request.setTypeEvent(TypeEvent.START);
        request.setNumber(111);
        request.setThread(333);

        ResponseEvent responseEvent = ResponseEvent.newBuilder()
                .setUid("TestUid")
                .setMessage("вычисления успешно начаты").build();

        Mockito.when(sendRequestToGrpcServerMock.sendRequestToServer(Mockito.any())).thenReturn(responseEvent);

        grpcServiceEvent.sendGrpcRequest(request);
        Mockito.verify(grpcRequest, Mockito.times(1)).createGrpcRequest(request);

    }

    /**
     * Тестируем вызов метода sendRequestToServer().
     */
    @Test
    void callSendRequestToGrpcServer() {
        CalculatingRequest request = new CalculatingRequest();
        request.setTypeEvent(TypeEvent.START);
        request.setNumber(111);
        request.setThread(333);

        ResponseEvent responseEvent = ResponseEvent.newBuilder()
                .setUid("TestUid")
                .setMessage("вычисления успешно начаты").build();

        RequestEvent requestEvent = RequestEvent.newBuilder().setNumber(5454).build();

        Mockito.when(grpcRequest.createGrpcRequest(request)).thenReturn(requestEvent);
        Mockito.when(sendRequestToGrpcServerMock.sendRequestToServer(requestEvent)).thenReturn(responseEvent);
        grpcServiceEvent.sendGrpcRequest(request);
        Mockito.verify(sendRequestToGrpcServerMock, Mockito.times(1)).sendRequestToServer(requestEvent);
    }

    /**
     * Правильно ли преобразует приходящие с севера данные по модели CalculatingRequest.
     */
    @Test
    void createCalculatingRespons() {
        CalculatingRequest request = new CalculatingRequest();
        request.setTypeEvent(TypeEvent.START);
        request.setNumber(111);
        request.setThread(333);

        ResponseEvent responseEvent = ResponseEvent.newBuilder()
                .setUid("TestUid")
                .setMessage("вычисления успешно начаты")
                .setResultCalculating("222").build();

        Mockito.when(sendRequestToGrpcServerMock.sendRequestToServer(Mockito.any())).thenReturn(responseEvent);
        CalculatingRespons calculatingRespons = grpcServiceEvent.sendGrpcRequest(request);
        Assertions.assertEquals("TestUid", calculatingRespons.getUid());
        Assertions.assertEquals("вычисления успешно начаты", calculatingRespons.getMessage());
        Assertions.assertEquals("222", calculatingRespons.getResultCaculating());
    }

}