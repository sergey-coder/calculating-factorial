package ru.services.http;

import ru.model.CalculatingRequest;
import ru.model.CalculatingRespons;

/**
 * Обрабатывает запросы от UI пользователя.
 */
public interface ConverterHttpGrpcService {
    void startCalculat(CalculatingRequest calculatingRequest);

    CalculatingRespons stopCalculat(CalculatingRequest calculatingRequest);

    void getCalculatStatus(CalculatingRequest calculatingRequest);

    void recommenceCalculating(CalculatingRequest calculatingRequest);
}
