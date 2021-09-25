package ru.web;

import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.RequestEvent;
import ru.ResponseEvent;
import ru.services.EventSelection;
import ru.services.impl.ExecuteStartEvent;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GrpcController.class)
class GrpcControllerTest {

    @Autowired
    private GrpcController grpcController;

    @MockBean
    private EventSelection eventSelection;

    @MockBean
    private ExecuteStartEvent executeStartEvent;

    /**
     * Проверяем, вызывается ли, при поступлении запроса, метод selectEventProcess();
     */
    @Test
    void processingCalculationResponse() {
        RequestEvent request = RequestEvent.newBuilder()
                .setTypeEvent(RequestEvent.TypeEvent.START).build();
        StreamRecorder<ResponseEvent> responseObserver = StreamRecorder.create();
        Mockito.when(eventSelection.selectEventProcess(RequestEvent.TypeEvent.START)).thenReturn(executeStartEvent);
        grpcController.getEventCalculation(request, responseObserver);

        Mockito.verify(eventSelection, Mockito.times(1)).selectEventProcess(RequestEvent.TypeEvent.START);
    }

}