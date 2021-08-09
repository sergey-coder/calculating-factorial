package ru.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.grpc.GrpcService;
import ru.model.CalculatingRequest;
import ru.model.TypeEvent;
import ru.services.ConverterHttpGrpcService;

@Service
public class ConverterHttpGrpcServiceImpl implements ConverterHttpGrpcService {
    private final GrpcService grpcService;

    public ConverterHttpGrpcServiceImpl(@Autowired GrpcService grpcService) {
        this.grpcService = grpcService;
    }

    /**
     *  Определяет тип события START,
     *  передает данные для отправки запроса gRPC.
     */
    @Override
    public void sendRequest(CalculatingRequest calculatingRequest) {
        checkZeroNegativeNumber(calculatingRequest.getNumber(),
                                calculatingRequest.getTreads());

        grpcService.sendGrpcRequest(TypeEvent.START, calculatingRequest);
    }

    /**
     * Определяет тип события STOP,
     * передает данные для отправки запроса gRPC.
     */
    @Override
    public void stopCalculat(CalculatingRequest calculatingRequest) {
        grpcService.sendGrpcRequest(TypeEvent.STOP, calculatingRequest);
    }

    /**
     * Определяет тип события GET_STATUS,
     * передает данные для отправки запроса gRPC.
     */
    @Override
    public void getCalculatStatus(CalculatingRequest calculatingRequest) {
        grpcService.sendGrpcRequest(TypeEvent.GET_STATUS, calculatingRequest);
    }

    /**
     * Проверка веденных значений числа и количества потоков.
     * Введенные числа должны быть строго больше нуля.
     * @param number
     */
    private void checkZeroNegativeNumber(int number, int tread){
        if(number <= 0 || tread <= 0){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"введенное значение должно быть больше нуля");
        }
    }
}
