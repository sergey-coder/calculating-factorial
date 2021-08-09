package ru.grpc;

import ru.model.CalculatingRequest;
import ru.model.TypeEvent;

public interface GrpcService {

    void sendGrpcRequest(TypeEvent typeEvent, CalculatingRequest calculatingRequest);
}
