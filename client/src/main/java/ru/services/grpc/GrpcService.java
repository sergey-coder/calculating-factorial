package ru.services.grpc;

import ru.model.CalculatingRequest;
import ru.model.CalculatingRespons;

public interface GrpcService {
    CalculatingRespons sendGrpcRequest(CalculatingRequest calculatingRequest);
}
