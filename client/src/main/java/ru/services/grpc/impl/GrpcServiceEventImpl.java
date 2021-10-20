package ru.services.grpc.impl;

import org.springframework.stereotype.Component;
import ru.RequestEvent;
import ru.ResponseEvent;
import ru.model.CalculatingRequest;
import ru.model.CalculatingRespons;
import ru.services.grpc.GrpcRequest;
import ru.services.grpc.GrpcService;
import ru.services.grpc.SendRequestToGrpcServer;

/**
 * Преобразует HTTP запрос по модели CalculatingRequest в
 * gRPC запрос по модели RequestEvent.
 */
@Component
public class GrpcServiceEventImpl implements GrpcService {

    private final SendRequestToGrpcServer sendRequestToGrpcServer;

    public GrpcServiceEventImpl(SendRequestToGrpcServer sendRequestToGrpcServer) {
        this.sendRequestToGrpcServer = sendRequestToGrpcServer;
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
        RequestEvent requestEvent = new GrpcRequest().createGrpcRequest(calculatingRequest);
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