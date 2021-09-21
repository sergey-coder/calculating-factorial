package ru.web;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.RequestEvent;
import ru.ResponseEvent;
import ru.ServerEndpointGrpc;
import ru.services.EventSelection;

/**
 * Контроллер входящих gRPC запросов
 */
@GrpcService
public class GrpcController extends ServerEndpointGrpc.ServerEndpointImplBase {

    private static Logger logger = LoggerFactory.getLogger(GrpcController.class);

    private final EventSelection eventSelection;

    public GrpcController(EventSelection eventSelection) {
        this.eventSelection = eventSelection;
    }

    /**
     * Принимает запросы на выполнение операций над вычислениями.
     *
     * @param request          запрос по модели RequestEvent request.
     * @param responseObserver ответ по модели ResponseEvent с информацией о результате запроса.
     */
    @Override
    public void getEventCalculation(RequestEvent request,
                                    StreamObserver<ResponseEvent> responseObserver) {
        logger.info("поступил gRPC запрос, тип запроса " + request.getTypeEvent().name() + " uid " + request.getUid());

        ResponseEvent response = eventSelection.selectEventProcess(request.getTypeEvent()).startEvent(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
