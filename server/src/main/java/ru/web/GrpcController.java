package ru.web;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.*;
import ru.services.GrpcControllerServices;
import ru.util.UidGenerator;

/**
 * Контроллер входящих gRPC запросов
 */
@GrpcService
public class GrpcController extends ServerEndpointGrpc.ServerEndpointImplBase{

    private static Logger logger = LoggerFactory.getLogger(GrpcController.class);

    private final GrpcControllerServices grpcControllerServices;

    public GrpcController(@Autowired GrpcControllerServices grpcControllerServices) {
        this.grpcControllerServices = grpcControllerServices;
    }

    /**
     * Принимает запросы на выполнение операций над текущем вычислением.
     * @param request запрос по модели RequestEvent request.
     * @param responseObserver ответ по модели ResponseEvent с информацией о результате запроса.
     */
    @Override
    public void getEventCalculation (RequestEvent request,
                                     StreamObserver<ResponseEvent> responseObserver){
        logger.info("поступил gRPC запрос для манипуляций с запущенными вычислениями, тип запроса " + request.getTypeEvent().name() + " uid " +request.getUid());

        ResponseEvent response = grpcControllerServices.startEvent(request);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void processingCalculationStream(ServerRequest request,
                                            StreamObserver<ServerResponse> responseObserver){

        logger.info("поступил gRPC запрос на старт вычислений, число " + request.getNumber() + " количество потоков " +request.getTreads());

        String uid = UidGenerator.generate();
        ServerResponse response = ServerResponse.newBuilder()
                .setUid(uid)
                .setMessage("Вычисление успешно начато")
                .build();

        responseObserver.onNext(response);

        String resultCalculation = grpcControllerServices.startCalculation(request, uid);

        ServerResponse responseResult = ServerResponse.newBuilder()
                .setUid(uid)
                .setMessage("Вычисление успешно окончено")
                .setResultCalculating(resultCalculation)
                .build();

        responseObserver.onNext(responseResult);
        responseObserver.onCompleted();
    }

}
