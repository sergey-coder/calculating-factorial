package ru.services;

import ru.model.CalculatingRequest;

public interface ConverterHttpGrpcService {
    void sendRequest(CalculatingRequest calculatingRequest);

    void stopCalculat(CalculatingRequest calculatingRequest);

    void getCalculatStatus(CalculatingRequest calculatingRequest);
}
