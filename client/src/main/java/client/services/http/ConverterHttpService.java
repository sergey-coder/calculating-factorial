package client.services.http;

import client.model.CalculatingRequest;
import client.model.CalculatingRespons;

public interface ConverterHttpService {
    CalculatingRespons startEventCalculating(CalculatingRequest calculatingRequest);
}
