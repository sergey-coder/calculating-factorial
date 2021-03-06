package client.services.grpc.impl;

import client.services.grpc.GrpcRequest;
import client.services.grpc.GrpcService;
import org.springframework.stereotype.Component;

import ru.RequestEvent;
import ru.ResponseEvent;
import client.model.CalculatingRequest;
import client.model.CalculatingRespons;
import client.services.grpc.SendRequestToGrpcServer;

/**
 * Преобразует HTTP запрос по модели CalculatingRequest в
 * gRPC запрос по модели RequestEvent.
 */
@Component
public class GrpcServiceEventImpl implements GrpcService {

    private final SendRequestToGrpcServer sendRequestToGrpcServer;
    private final GrpcRequest grpcRequest;

    public GrpcServiceEventImpl(SendRequestToGrpcServer sendRequestToGrpcServer, GrpcRequest grpcRequest) {
        this.sendRequestToGrpcServer = sendRequestToGrpcServer;
        this.grpcRequest = grpcRequest;
    }

    /**
     * Принимает запрос по модели CalculatingRequest,
     * передает запрос по модели RequestEvent для отправки на gRPC сервер,
     * принимает от сервера ответ по модели ResponseEvent,
     * передает инициатору ответ по модели CalculatingRespons.
     *
     * @param calculatingRequest запрос пользователя по модели CalculatingRequest.
     * @return ответ от gRPC преобразованный в модель  CalculatingRespons.
     */
    @Override
    public CalculatingRespons sendGrpcRequest(CalculatingRequest calculatingRequest) {
        RequestEvent requestEvent = grpcRequest.createGrpcRequest(calculatingRequest);
        ResponseEvent responseEvent = sendRequestToGrpcServer.sendRequestToServer(requestEvent);
        return toCalculatingRespons(responseEvent);
    }

    /**
     * Преобразует ответ gRPC сервера по моедли CalculatingRespons
     *
     * @param responseEvent модель сообщения от gRPC
     * @return ответ сервера по модели CalculatingRespons.
     */
    private CalculatingRespons toCalculatingRespons(ResponseEvent responseEvent) {
        CalculatingRespons calculatingRespons = new CalculatingRespons();
        calculatingRespons.setMessage(responseEvent.getMessage());
        calculatingRespons.setUid(responseEvent.getUid());
        calculatingRespons.setResultCaculating(responseEvent.getResultCalculating());
        return calculatingRespons;
    }

}