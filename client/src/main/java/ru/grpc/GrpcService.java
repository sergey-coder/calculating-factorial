package ru.grpc;

import ru.model.CalculatingRequest;

public interface GrpcService {

    void sendGrpcRequest(CalculatingRequest calculatingRequest);
}
