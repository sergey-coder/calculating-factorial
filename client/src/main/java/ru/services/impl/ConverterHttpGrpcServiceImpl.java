package ru.services.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.model.CalculatingRequest;
import ru.services.ConverterHttpGrpcService;

@Service
public class ConverterHttpGrpcServiceImpl implements ConverterHttpGrpcService {

    /**
     *  Преобразует HTTP запрос в grpc
     *  Механизм отправки на end point сервера еще не определен
     */
    @Override
    public void sendRequest(CalculatingRequest calculatingRequest) {
        checkZeroNegativeNumber(calculatingRequest.getNumber(),
                                calculatingRequest.getTreads());
        // TODO
    }

    /**
     * Отправляет некий event на сервер с целью остановки вычесления
     * передаем id вычесления
     */
    @Override
    public void stopCalculat(CalculatingRequest calculatingRequest) {
        //TODO
    }

    /**
     * Отправляет некий event на сервер с целью получения информации
     * о текущем состоянии вычесления по его id
     */
    @Override
    public void getCalculatStatus(CalculatingRequest calculatingRequest) {
        //TODO
    }

    /**
     * Билдер запроса в gRPC
     */
    private Object getIdRequest(){
        //TODO
        return 0;
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
