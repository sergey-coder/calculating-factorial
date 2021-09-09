package ru.services.http.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.grpc.GrpcService;
import ru.model.CalculatingRequest;
import ru.model.CalculatingRespons;
import ru.model.TypeEvent;
import ru.services.http.ConverterHttpGrpcService;

@Service
public class ConverterHttpGrpcServiceImpl implements ConverterHttpGrpcService {
    private final GrpcService grpcService;

    public ConverterHttpGrpcServiceImpl(@Autowired GrpcService grpcService) {
        this.grpcService = grpcService;
    }

    /**
     *  Определяет тип события START,
     *  передает данные для отправки запроса gRPC.
     *  Запрос с двумя обязательными параметрами
     *      * Integer number Число от которого вычисляется факториал
     *      * Integer treads Количество потоков
     */
    @Override
    public void startCalculat(CalculatingRequest calculatingRequest) {
        checkZeroNegativeNumber(calculatingRequest);
        grpcService.beginProcessingCalculation(calculatingRequest);
    }

    /**
     * Определяет тип события STOP,
     * передает данные для отправки запроса gRPC.
     * Запрос для остановки ранее запущенного вычесления
     * @return
     */
    @Override
    public CalculatingRespons stopCalculat(CalculatingRequest calculatingRequest) {
        checkUid(calculatingRequest);
        return grpcService.sendGrpcRequest(TypeEvent.STOP, calculatingRequest);
    }

    /**
     * Определяет тип события GET_STATUS,
     * передает данные для отправки запроса gRPC.
     */
    @Override
    public void getCalculatStatus(CalculatingRequest calculatingRequest) {
        checkUid(calculatingRequest);
        grpcService.sendGrpcRequest(TypeEvent.GET_STATUS, calculatingRequest);
    }

    /**
     * Определяет тип события RECOMMENCE,
     * передает данные для отправки запроса gRPC.
     */
    @Override
    public void recommenceCalculating(CalculatingRequest calculatingRequest) {
        checkUid(calculatingRequest);
        grpcService.sendGrpcRequest(TypeEvent.RECOMMENCE, calculatingRequest);
    }

    /**
     * Проверка веденных значений числа и количества потоков.
     * Введенные числа должны быть строго больше нуля.
     */
    private void checkZeroNegativeNumber(CalculatingRequest calculatingRequest){
        if(calculatingRequest == null || calculatingRequest.getNumber() <= 0 || calculatingRequest.getTreads() <= 0){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"введенное значение должно быть больше нуля");
        }
    }

    /**
     *  Проверяет наличие uid.
     */
    private void checkUid(CalculatingRequest calculatingRequest){
        if(calculatingRequest == null || calculatingRequest.getUid()==null || calculatingRequest.getUid().equals("")){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"отсутствует uid вычисления");
        }
    }
}
