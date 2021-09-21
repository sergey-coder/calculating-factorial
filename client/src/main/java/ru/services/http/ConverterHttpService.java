package ru.services.http;

import ru.model.CalculatingRequest;
import ru.model.CalculatingRespons;

public interface ConverterHttpService {
    CalculatingRespons startEventCalculating(CalculatingRequest calculatingRequest);
}
