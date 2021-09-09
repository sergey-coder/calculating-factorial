package ru.grpc;

import ru.model.CalculatingRequest;
import ru.model.CalculatingRespons;
import ru.model.TypeEvent;

public interface GrpcService {

    CalculatingRespons sendGrpcRequest(TypeEvent typeEvent, CalculatingRequest calculatingRequest);

    void beginProcessingCalculation(CalculatingRequest calculatingRequest);
}
