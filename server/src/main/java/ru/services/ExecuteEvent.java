package ru.services;

import ru.RequestEvent;
import ru.ResponseEvent;

public interface ExecuteEvent {
    ResponseEvent startEvent(RequestEvent request);
}
