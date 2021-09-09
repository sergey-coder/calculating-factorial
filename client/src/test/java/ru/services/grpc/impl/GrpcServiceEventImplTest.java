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
import ru.services.grpc.SendRequestToGrpcServer;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GrpcServiceEventImpl.class)
class GrpcServiceEventImplTest {

    @Autowired
    GrpcServiceEventImpl grpcService;

    @MockBean
    SendRequestToGrpcServer sendRequestToGrpcServer;

    @Test
    void sendStopRequest(){
        CalculatingRequest request = new CalculatingRequest();
        request.setUid("testUid");

        CalculatingRespons calculatingRespons = new CalculatingRespons();
        calculatingRespons.setUid("testUid");
        calculatingRespons.setMessage("запрос на остановку вычеслений выполнен");

        ResponseEvent responseEvent = ResponseEvent.newBuilder()
                .setUid("testUid")
                .setMessage("запрос на остановку вычеслений выполнен").build();


        Mockito.when(sendRequestToGrpcServer.sendRequestToServer(Mockito.any(RequestEvent.class))).thenReturn(responseEvent);
        CalculatingRespons respons = grpcService.sendGrpcRequest(TypeEvent.STOP, request);
        Assertions.assertEquals(respons, calculatingRespons);
    }

}