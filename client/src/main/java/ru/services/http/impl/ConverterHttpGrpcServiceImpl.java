package ru.services.http.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.model.CalculatingRequest;
import ru.model.CalculatingRespons;
import ru.model.TypeEvent;
import ru.services.grpc.GrpcService;
import ru.services.http.ConverterHttpGrpcService;

/**
 * Определят Тип event поступившего от пользователя,
 * на основании Тип определяется метод обработки запроса grpc сервисом.
 */
@Service
public class ConverterHttpGrpcServiceImpl implements ConverterHttpGrpcService {
    private final GrpcService grpcService;

    public ConverterHttpGrpcServiceImpl(@Autowired GrpcService grpcService) {
        this.grpcService = grpcService;
    }

    /**
     * Определяет тип события START,
     * передает данные для отправки запроса gRPC.
     * Запрос с двумя обязательными параметрами
     * * Integer number Число от которого вычисляется факториал
     * * Integer treads Количество потоков
     *
     * @return CalculatingRespons  с сообщением о результатах выполнения запроса,
     * и Uid вычисления.
     */
    @Override
    public CalculatingRespons startCalculat(CalculatingRequest calculatingRequest) {
        if (!checkZeroNegativeNumber(calculatingRequest)) {
            CalculatingRespons response = new CalculatingRespons();
            response.setMessage("введенное значение должно быть больше нуля");
            return response;
        }
        return grpcService.sendGrpcRequest(TypeEvent.START, calculatingRequest);
    }

    /**
     * Определяет тип события STOP,
     * передает данные для отправки запроса gRPC.
     * Запрос для остановки ранее запущенного вычесления
     *
     * @return CalculatingRespons  с сообщением о результатах выполнения запроса.
     */
    @Override
    public CalculatingRespons stopCalculat(CalculatingRequest calculatingRequest) {
        if (!checkUid(calculatingRequest)) {
            return dontCheckUid();
        }
        return grpcService.sendGrpcRequest(TypeEvent.STOP, calculatingRequest);
    }

    /**
     * Определяет тип события GET_STATUS,
     * передает данные для отправки запроса gRPC.
     *
     * @return CalculatingRespons  с сообщением о результатах выполнения запроса.
     */
    @Override
    public CalculatingRespons getCalculatStatus(CalculatingRequest calculatingRequest) {
        if (!checkUid(calculatingRequest)) {
            return dontCheckUid();
        }
        return grpcService.sendGrpcRequest(TypeEvent.GET_STATUS, calculatingRequest);
    }

    /**
     * Определяет тип события RECOMMENCE,
     * передает данные для отправки запроса gRPC.
     *
     * @return CalculatingRespons  с сообщением о результатах выполнения запроса.
     */
    @Override
    public CalculatingRespons recommenceCalculating(CalculatingRequest calculatingRequest) {
        if (!checkUid(calculatingRequest)) {
            return dontCheckUid();
        }
        return grpcService.sendGrpcRequest(TypeEvent.RECOMMENCE, calculatingRequest);
    }

    /**
     * Определяет тип события RESULT,
     * передает данные для отправки запроса gRPC.
     *
     * @return либо текущий результат, если вычисление не завершено,
     * либо окончательный результат - если завершено. Текст сообщения различный в этих случаях.
     */
    @Override
    public CalculatingRespons getCalculatingResult(CalculatingRequest calculatingRequest) {
        if (!checkUid(calculatingRequest)) {
            return dontCheckUid();
        }
        return grpcService.sendGrpcRequest(TypeEvent.RESULT, calculatingRequest);
    }

    /**
     * Проверка веденных значений числа и количества потоков.
     * Введенные числа должны быть строго больше нуля.
     */
    private boolean checkZeroNegativeNumber(CalculatingRequest calculatingRequest) {
        return calculatingRequest != null && calculatingRequest.getNumber() > 0 && calculatingRequest.getTreads() > 0;
    }

    /**
     * Проверяет наличие uid.
     */
    private boolean checkUid(CalculatingRequest calculatingRequest) {
        return calculatingRequest != null
                && calculatingRequest.getUid() != null
                && !calculatingRequest.getUid().equals("");
    }

    /**
     * Формирует ответ для запросов без обязательного Uid.
     */
    private CalculatingRespons dontCheckUid() {
        CalculatingRespons response = new CalculatingRespons();
        response.setMessage("отсутствует uid вычисления");
        return response;
    }
}
