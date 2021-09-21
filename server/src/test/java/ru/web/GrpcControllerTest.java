package ru.web;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GrpcController.class)
class GrpcControllerTest {

    @Autowired
    GrpcController grpcController;



    /**
     * Формирует ли правильный ответ.
     */
//    @Test
//    void processingCalculationResponse(){
//        ServerRequest request = ServerRequest.newBuilder().setNumber(3).build();
//        Mockito.when(grpcControllerServices.startCalculation(request, uid)).thenReturn("testUiad");
//        StreamRecorder<ServerResponse> responseObserver = StreamRecorder.create();
//
//        grpcController.processingCalculation(request, responseObserver);
//
//        Assertions.assertNull(responseObserver.getError());
//        Assertions.assertEquals(1, responseObserver.getValues().size());
//        Assertions.assertEquals("testUiad", responseObserver.getValues().get(0).getUid());
//        Assertions.assertEquals("Вычисление успешно начато", responseObserver.getValues().get(0).getMessage());
//    }
//
//    /**
//     * Вызывает ли метод startCalculation.
//     */
//    @Test
//    void processingCalculation(){
//        ServerRequest request = ServerRequest.newBuilder().setNumber(3).build();
//        Mockito.when(grpcControllerServices.startCalculation(request, uid)).thenReturn("testUiad");
//        StreamRecorder<ServerResponse> responseObserver = StreamRecorder.create();
//
//        grpcController.processingCalculation(request, responseObserver);
//        Mockito.verify(grpcControllerServices, Mockito.times(1)).startCalculation(request, uid);
//    }

}