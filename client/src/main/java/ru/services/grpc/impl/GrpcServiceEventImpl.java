package ru.services.grpc.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import ru.RequestEvent;
import ru.ResponseEvent;
import ru.model.CalculatingRequest;
import ru.model.CalculatingRespons;
import ru.model.TypeEvent;
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
     * Отправляем gRPC запрос по указанному URL
     *
     * @param typeEvent          тип события enum.
     * @param calculatingRequest запрос пользователя.
     * @return ответ от gRPC преобразованный в модель  CalculatingRespons.
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
     * /**
     * Преобразует ответ gRPC сервера по моедли CalculatingRespons
     *
     * @param responseEvent модель сообщения от gRPC
     * @param typeEvent     enum перечень типов событий обрабатываемых client
     * @return ответ сервера по модели CalculatingRespons.
     */
    private CalculatingRespons toCalculatingRespons(ResponseEvent responseEvent, TypeEvent typeEvent) {
        CalculatingRespons calculatingRespons = new CalculatingRespons();
        switch (typeEvent) {
            case STOP, RECOMMENCE, START -> {
                calculatingRespons.setMessage(responseEvent.getMessage());
                calculatingRespons.setUid(responseEvent.getUid());
                return calculatingRespons;
            }
            case GET_STATUS, RESULT -> {
                calculatingRespons.setMessage(responseEvent.getMessage());
                calculatingRespons.setUid(responseEvent.getUid());
                calculatingRespons.setResultCaculating(responseEvent.getResultCalculating());
                return calculatingRespons;
            }
            default -> throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "не известный тип запроса");
        }
    }

    /**
     * Определяет тип события.
     * В зависимости от типа собития определеям способ формирования запроса gPRC по модели RequestEvent.
     */
    private RequestEvent toServerRequest(TypeEvent typeEvent, CalculatingRequest calculatingRequest) {
        return switch (typeEvent) {
            case STOP -> getStopEvent(calculatingRequest);
            case GET_STATUS -> getStatusEvent(calculatingRequest);
            case RECOMMENCE -> getRecommenceEvent(calculatingRequest);
            case START -> getStartEvent(calculatingRequest);
            case RESULT -> getResultEvent(calculatingRequest);
        };
    }

    /**
     * Формирует запрос для события RESULT в gRPC request.
     */
    private RequestEvent getResultEvent(CalculatingRequest calculatingRequest) {
        return RequestEvent.newBuilder()
                .setUid(calculatingRequest.getUid())
                .setTypeEvent(RequestEvent.TypeEvent.RESULT)
                .build();
    }

    /**
     * Формирует запрос для события RECOMMENCE в gRPC request.
     */
    private RequestEvent getRecommenceEvent(CalculatingRequest calculatingRequest) {
        return RequestEvent.newBuilder()
                .setUid(calculatingRequest.getUid())
                .setTypeEvent(RequestEvent.TypeEvent.RECOMMENCE)
                .build();
    }

    /**
     * Формирует запрос для события GET_STATUS в gRPC request.
     */
    private RequestEvent getStatusEvent(CalculatingRequest calculatingRequest) {
        return RequestEvent.newBuilder()
                .setUid(calculatingRequest.getUid())
                .setTypeEvent(RequestEvent.TypeEvent.GET_STATUS)
                .build();
    }

    /**
     * Формирует запрос для события STOP в gRPC request.
     */
    private RequestEvent getStopEvent(CalculatingRequest calculatingRequest) {

        return RequestEvent.newBuilder()
                .setUid(calculatingRequest.getUid())
                .setTypeEvent(RequestEvent.TypeEvent.STOP)
                .build();
    }

    /**
     * Формирует запрос для события START в gRPC request.
     */
    private RequestEvent getStartEvent(CalculatingRequest calculatingRequest) {

        return RequestEvent.newBuilder()
                .setTypeEvent(RequestEvent.TypeEvent.START)
                .setTreads(calculatingRequest.getTreads())
                .setNumber(calculatingRequest.getNumber())
                .build();
    }
}