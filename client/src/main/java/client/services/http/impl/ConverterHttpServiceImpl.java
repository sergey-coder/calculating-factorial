package client.services.http.impl;

import client.services.grpc.GrpcService;
import client.services.http.ConverterHttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import client.model.CalculatingRequest;
import client.model.CalculatingRespons;
import client.model.TypeEvent;

/**
 * Обрабатывает запрос поступившие в контроллер, проводит проверку полей.
 */
@Service
public class ConverterHttpServiceImpl implements ConverterHttpService {

    private static final Logger logger = LoggerFactory.getLogger(ConverterHttpServiceImpl.class);

    private final GrpcService grpcService;

    public ConverterHttpServiceImpl(@Autowired GrpcService grpcService) {
        this.grpcService = grpcService;
    }

    /**
     * Проверяет наличие обязательных полей, при отсутствии сам формирует ответ для пользователя,
     * передает запрос для формирования запроса на gRPC сервер.
     * @param calculatingRequest запрос от пользователя по модели CalculatingRequest.
     * @return преобразованный ответ от gRPC сервера по модели CalculatingRespons.
     */
    @Override
    public CalculatingRespons startEventCalculating(CalculatingRequest calculatingRequest) {
        if(!checkTapeEvent(calculatingRequest)){
            return falseCheckValue("не указан тип события TypeEvent");
        }
        if(calculatingRequest.getTypeEvent()==TypeEvent.START){
            if (!checkZeroNegativeNumber(calculatingRequest)) {
                return falseCheckValue("введенное значение должно быть больше нуля");
            }
            return grpcService.sendGrpcRequest(calculatingRequest);
        }
        return checkUid(calculatingRequest)==Boolean.FALSE?
                falseCheckValue("отсутствует uid вычисления"):
                grpcService.sendGrpcRequest(calculatingRequest);
    }

    /**
     * Проверка веденных значений числа и количества потоков.
     * Введенные числа должны быть строго больше нуля.
     */
    private boolean checkZeroNegativeNumber(CalculatingRequest calculatingRequest) {
        return calculatingRequest != null
                && calculatingRequest.getNumber()!=null
                && calculatingRequest.getThread()!=null
                && calculatingRequest.getNumber() > 0
                && calculatingRequest.getThread() > 0;
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
     * Проверяет наличие TapeEvent.
     */
    private boolean checkTapeEvent(CalculatingRequest calculatingRequest) {
        return calculatingRequest != null && calculatingRequest.getTypeEvent() != null;
    }

    /**
     * Формирует ответ для запросов без обязательных параметров.
     * Выводит в лог предупреждение о запросе.
     */
    private CalculatingRespons falseCheckValue(String message) {
        CalculatingRespons response = new CalculatingRespons();
        response.setMessage(message);
        logger.warn(message);
        return response;
    }

}
