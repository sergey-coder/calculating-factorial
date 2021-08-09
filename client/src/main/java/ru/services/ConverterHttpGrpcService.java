package ru.services;

import ru.model.CalculatingRequest;

/**
 * Обрабатывает запросы от UI пользователя.
 */
public interface ConverterHttpGrpcService {
    void sendRequest(CalculatingRequest calculatingRequest);

    void stopCalculat(CalculatingRequest calculatingRequest);

    void getCalculatStatus(CalculatingRequest calculatingRequest);
}
