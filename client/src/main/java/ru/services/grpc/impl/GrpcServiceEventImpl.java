package ru.services.grpc.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import ru.RequestEvent;
import ru.ResponseEvent;
import ru.ServerRequest;
import ru.grpc.GrpcService;
import ru.model.CalculatingRequest;
import ru.model.CalculatingRespons;
import ru.model.TypeEvent;
import ru.services.grpc.SendRequestToGrpcServer;

@Component
public class GrpcServiceEventImpl implements GrpcService {

    private final SendRequestToGrpcServer sendRequestToGrpcServer;

    public GrpcServiceEventImpl(SendRequestToGrpcServer sendRequestToGrpcServer) {
        this.sendRequestToGrpcServer = sendRequestToGrpcServer;
    }

    /**
     * Отправляем gRPC запрос по указанному URL
     * @param typeEvent тип события enum.
     * @param calculatingRequest запрос пользователя.
     * @return
     */
    @Override
    public CalculatingRespons sendGrpcRequest(TypeEvent typeEvent, CalculatingRequest calculatingRequest) {
        ResponseEvent responseEvent = sendRequestToGrpcServer.sendRequestToServer
                (
                        toServerRequest(
                                typeEvent,
                                calculatingRequest
                        )
                );

        return toCalculatingRespons(responseEvent, typeEvent);
    }

    /**
     * Отправляет запрос на начало вычислений.
     * @param calculatingRequest
     */
    @Override
    public void beginProcessingCalculation(CalculatingRequest calculatingRequest) {
        sendRequestToGrpcServer.sendStartRequestToServer(getStartEvent(calculatingRequest));
    }



    /**
     * Преобразует ответ gRPC сервера по моедли CalculatingRespons
     * @param responseEvent
     * @param typeEvent
     * @return
     */
    private CalculatingRespons toCalculatingRespons(ResponseEvent responseEvent, TypeEvent typeEvent){
        CalculatingRespons calculatingRespons = new CalculatingRespons();
        switch (typeEvent){
            case STOP: calculatingRespons.setUid(responseEvent.getUid());
                       calculatingRespons.setMessage(responseEvent.getMessage());
                       return calculatingRespons;
            case GET_STATUS: return  null;
            case RECOMMENCE: return  null;
            default: throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"не известный тип запроса");
        }
    }

    /**
     * Определяет тип события.
     * Преобразует UI запрос пользователя в gRPC request.
     */
    private RequestEvent toServerRequest(TypeEvent typeEvent, CalculatingRequest calculatingRequest){
        switch (typeEvent){
            case STOP: return  getStopEvent(calculatingRequest);
            case GET_STATUS: return  getStatusEvent(calculatingRequest);
            case RECOMMENCE: return  getRecommenceEvent(calculatingRequest);
            default: throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"не известный тип запроса");
        }
    }

    /**
     * Формирует событие recommence в gRPC request.
     */
    private RequestEvent getRecommenceEvent(CalculatingRequest calculatingRequest) {
        return RequestEvent.newBuilder()
                .setUid(calculatingRequest.getUid())
                .setTypeEvent(RequestEvent.TypeEvent.RECOMMENCE)
                .build();
    }

    /**
     * Формирует событие status в gRPC request.
     */
    private RequestEvent getStatusEvent(CalculatingRequest calculatingRequest) {
        return RequestEvent.newBuilder()
                .setUid(calculatingRequest.getUid())
                .setTypeEvent(RequestEvent.TypeEvent.GET_STATUS)
                .build();
    }

    /**
     * Формирует событие stop в gRPC request.
     */
    private RequestEvent getStopEvent(CalculatingRequest calculatingRequest) {

        return RequestEvent.newBuilder()
                .setUid(calculatingRequest.getUid())
                .setTypeEvent(RequestEvent.TypeEvent.STOP)
                .build();
    }

    /**
     * Формирует запрос на вычисление в gRPC request.
     */
    private ServerRequest getStartEvent(CalculatingRequest calculatingRequest){
        return ServerRequest.newBuilder()
                .setTreads(calculatingRequest.getTreads())
                .setNumber(calculatingRequest.getNumber())
                .setUrlRequest(calculatingRequest.getUrlRequest())
                .build();
    }
}