package ru.services;

import ru.RequestEvent;
import ru.ResponseEvent;
import ru.ServerRequest;

public interface GrpcControllerServices {

    String startCalculation(ServerRequest request, String uid);

    ResponseEvent startEvent(RequestEvent request);
}
