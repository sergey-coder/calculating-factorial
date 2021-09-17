package ru.services.http;

import ru.model.CalculatingRequest;
import ru.model.CalculatingRespons;

public interface ConverterHttpGrpcService {
    CalculatingRespons startCalculat(CalculatingRequest calculatingRequest);

    CalculatingRespons stopCalculat(CalculatingRequest calculatingRequest);

    CalculatingRespons getCalculatStatus(CalculatingRequest calculatingRequest);

    CalculatingRespons recommenceCalculating(CalculatingRequest calculatingRequest);

    CalculatingRespons getCalculatingResult(CalculatingRequest calculatingRequest);
}
