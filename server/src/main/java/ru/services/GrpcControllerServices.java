package ru.services;

import ru.RequestEvent;
import ru.ResponseEvent;

public interface GrpcControllerServices {
    ResponseEvent startEvent(RequestEvent request);
}
