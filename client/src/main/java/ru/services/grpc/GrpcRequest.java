package ru.services.grpc;

import org.springframework.stereotype.Component;
import ru.RequestEvent;
import ru.model.CalculatingRequest;
import ru.model.TypeEvent;

/**
 * Создает запрос к gRPC модулю по модели RequestEvent.
 */
@Component
public class GrpcRequest {

    /**
     * Через newBuilder создает RequestEvent сущность,
     * заполняет её поля.
     *
     * @param calculatingRequest запрос пользователя по модели CalculatingRequest.
     * @return запрос к gRPC модулю по модели RequestEvent.
     */
    public RequestEvent createGrpcRequest(CalculatingRequest calculatingRequest) {
        return RequestEvent.newBuilder()
                .setUid(checkField(calculatingRequest.getUid()) ?
                        calculatingRequest.getUid() :
                        "")

                .setTypeEvent(toGrpcTypeEvent(calculatingRequest.getTypeEvent()))

                .setTreads(checkField(calculatingRequest.getThread()) ?
                        calculatingRequest.getThread() :
                        0)

                .setNumber(checkField(calculatingRequest.getNumber()) ?
                        calculatingRequest.getNumber() :
                        0)

                .build();
    }

    /**
     * Сопоставляет типы Even gRPC и Http модулей.
     *
     * @param typeEvent тип Event Http модуля.
     * @return тип Event gRPC модуля.
     */
    private RequestEvent.TypeEvent toGrpcTypeEvent(TypeEvent typeEvent) {
        return switch (typeEvent) {
            case STOP -> RequestEvent.TypeEvent.STOP;
            case GET_STATUS -> RequestEvent.TypeEvent.GET_STATUS;
            case RECOMMENCE -> RequestEvent.TypeEvent.RECOMMENCE;
            case START -> RequestEvent.TypeEvent.START;
            case RESULT -> RequestEvent.TypeEvent.RESULT;
        };
    }

    /**
     * Проверять объекты на значение null.
     *
     * @param field поля модели CalculatingRequest.
     * @return true если поле не равно null.
     */
    private boolean checkField(Object field) {
        return field != null;
    }

}
