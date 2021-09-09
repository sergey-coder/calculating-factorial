package ru.services.http.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.RequestEvent;
import ru.ResponseEvent;
import ru.ServerRequest;
import ru.calculate.Calculate;
import ru.services.GrpcControllerServices;
import ru.util.UidGenerator;

import java.math.BigInteger;

@Service
public class GrpcControllerServicesImpl implements GrpcControllerServices {

    private final Calculate calculate;

    public GrpcControllerServicesImpl(@Autowired Calculate calculate) {
        this.calculate = calculate;
    }

    /**
     * Получить результат вычеслений
     */
    public BigInteger getСurrentResultCalculating(){
        return calculate.getСurrentResult();
    }

    /**
     * Получить текущий статус вычеслений
     */
    public ResponseEvent getStatusCalculating(String uid){
        ResponseEvent.Status status = calculate.getStatusCalculate().equals(Boolean.FALSE)?
                ResponseEvent.Status.FINISHED :
                ResponseEvent.Status.EXECUTING;

        return ResponseEvent.newBuilder()
                .setUid(uid)
                .setMessage("статус вычисления получен успешно")
                .setStatus(status)
                .build();
    }

    /**
     * Принудительно остановить вычесления
     * вернуть ответ по модели ResponseEvent
     */
    public ResponseEvent stopCalculating(String uid){
        if(calculate.getStatusCalculate().equals(Boolean.FALSE)){
            calculate.setStopCalculate(true);
            return ResponseEvent.newBuilder()
                    .setUid(uid)
                    .setMessage("вычесления остановлены")
                    .build();
        }
        return ResponseEvent.newBuilder()
                .setUid(uid)
                .setMessage("вычесления не могут остановлены, так как уже завершены")
                .build();
    }

    /**
     * Принимает запрос на вычисления,
     * организует формирование entity вычисления.
     * @param request запрос по модели ServerRequest.
     * @param uid
     * @return uid вычисления.
     */
    @Override
    public String startCalculation(ServerRequest request, String uid) {
        //сохранение в БД
        return getFinishResultCalculating(request.getNumber());
    }

    /**
     * Инициирует  старт вычислений, ждет окончательный результат,
     * формирует ответ для потребителя
     * @param number
     */
    private String getFinishResultCalculating(int number) {
        return calculate.calculatingFactorial(number).toString();
    }

    /**
     * Определяет вид пришедшего Event, запускает соотвествующую Event логику
     * @param request
     * @return
     */
    @Override
    public ResponseEvent startEvent(RequestEvent request) {
        String requestUid = request.getUid();
        if(!checkUid(requestUid)){
            return ResponseEvent.newBuilder()
                    .setUid(requestUid)
                    .setMessage("вычисления с данным uid не существует")
                    .build();
        }

        RequestEvent.TypeEvent typeEvent = request.getTypeEvent();

        switch (typeEvent){
            case STOP: return  stopCalculating(requestUid);
            case GET_STATUS: return  getStatusCalculating(requestUid);
            case RECOMMENCE: return  recommenceCalculating(requestUid);
            default: throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"не известный тип запроса");
        }
    }

    /**
     * Запрашивает из базы данных по uid параметры вычеслений,
     * в том числе и результат на момент остановки,
     * запускает дальше вычесления
     * @param requestUid
     * @return
     */
    private ResponseEvent recommenceCalculating(String requestUid) {
// все переписать
        return ResponseEvent.newBuilder()
                .setUid(requestUid)
                .setMessage("вычесления остановлены")
                .build();
    }

    private boolean checkUid (String requestUid) {
        // запросить в БД данные о наличии вычесления
       return  Boolean.TRUE;
        //return  Boolean.FALSE;
    }
}
