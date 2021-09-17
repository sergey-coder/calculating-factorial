package ru.services.http.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.RequestEvent;
import ru.ResponseEvent;
import ru.calculate.concurrent.CalculateMainThread;
import ru.dao.CalculationDao;
import ru.domain.Calculation;
import ru.services.GrpcControllerServices;
import ru.util.UidGenerator;

@Service
public class GrpcControllerServicesImpl implements GrpcControllerServices {

    private final CalculationDao calculationDao;

    public GrpcControllerServicesImpl(@Autowired CalculationDao calculationDao) {
        this.calculationDao = calculationDao;
    }

    /**
     * Определяет вид пришедшего Event, запускает соотвествующую Event логику
     *
     * @param request запрос по модели RequestEvent.
     * @return ответс результатами выполнения запроса по модели ResponseEvent.
     */
    @Override
    public ResponseEvent startEvent(RequestEvent request) {

        if (request.getTypeEvent() == RequestEvent.TypeEvent.START) {
            return startCalculation(request);
        }

        String requestUid = request.getUid();
        if (!checkUid(requestUid)) {
            return ResponseEvent.newBuilder()
                    .setUid(requestUid)
                    .setMessage("вычисление с данным uid не запущено")
                    .setStatus(ResponseEvent.StatusCalculation.NOT_FOUND)
                    .build();
        }

        RequestEvent.TypeEvent typeEvent = request.getTypeEvent();

        return switch (typeEvent) {
            case STOP -> stopCalculating(requestUid);
            case GET_STATUS -> getStatusCalculating(requestUid);
           // case RECOMMENCE -> recommenceCalculating(requestUid);
            case START -> startCalculation(request);
            case RESULT -> getCurrentResultCalculating(requestUid);
            default -> throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "не известный тип запроса");
        };
    }

    /**
     * Запускает вычисления событие START.
     * Формирует запись в памяти о вычислении, сохраняет ссылку на инстанс MenedgThread,
     * устанавливает статус вычислений на EXECUTING.
     * запускается MenedgThread в отдельном потоке.
     */
    private ResponseEvent startCalculation(RequestEvent request) {
        Calculation calculation = new Calculation();
        calculation.setUid(UidGenerator.generate());
        calculation.setNumber(request.getNumber());
        calculation.setTreads(request.getTreads());
        calculation.setStatusCalculation(ResponseEvent.StatusCalculation.EXECUTING);

       /* localFactorialCalculate = new MenedgThread(calculationDao, calculation);
        calculation.setMenedgThread(localFactorialCalculate);
        calculationDao.addNewCalculation(calculation);*/
        CalculateMainThread calculateMainThread = new CalculateMainThread(calculationDao, calculation);
        calculation.setCalculateMainThread(calculateMainThread);
        calculationDao.addNewCalculation(calculation);
        //Thread thread = new Thread(localFactorialCalculate);
        Thread thread = new Thread(calculateMainThread);
        thread.start();

        return ResponseEvent.newBuilder()
                .setUid(calculation.getUid())
                .setMessage("вычисления успешно начаты")
                .build();
    }

    /**
     * Получить результат вычеслений - событие RESULT.
     * Запрашиваем сведения из колекции с Calculation.
     */
    private ResponseEvent getCurrentResultCalculating(String uid) {
        Calculation calculation = calculationDao.findByUid(uid);
        if (calculation.getStatusCalculation() == ResponseEvent.StatusCalculation.EXECUTING) {
            return ResponseEvent.newBuilder()
                    .setUid(uid)
                    .setMessage("вычисления еще не завершены")
                    .setResultCalculating("результат вычислений еще не определен")
                    .build();
        } else if (calculation.getStatusCalculation() == ResponseEvent.StatusCalculation.STOPPED){
            return ResponseEvent.newBuilder()
                    .setUid(uid)
                    .setMessage("вычисления остановлены")
                    .build();
        }
        return ResponseEvent.newBuilder()
                .setUid(uid)
                .setMessage("результат вычислений получен")
                .setResultCalculating(calculation.getResultCalculation())
                .build();
    }

    /**
     * Получить текущий статус вычеслений - событие GET_STATUS.
     * Данные запрашиваются только из колекции с Calculation.
     */
    private ResponseEvent getStatusCalculating(String uid) {
        Calculation calculation = calculationDao.findByUid(uid);
        String message;
        switch (calculation.getStatusCalculation()){
            case EXECUTING -> {
                message = "Выполняется. Завершено потоков "
                        + (calculation.getTreads() - calculation.getCalculateMainThread().getActiveThreadCount())
                        + " из " + calculation.getTreads();
            }
            case FINISHED -> {
                message = "Завершено. Значение факториала " + calculation.getNumber() + " равно " + calculation.getResultCalculation();
            }

            case STOPPED -> {
                message = "Остановлено";
            }
            default -> {
                message = "Статус вычисления опредилить не удалось";
            }
        }
        return ResponseEvent.newBuilder()
                .setUid(uid)
                .setMessage(message)
                .setStatus(calculation.getStatusCalculation())
                .setResultCalculating(calculation.getResultCalculation())
                .build();
    }

    /**
     * Принудительно остановить вычесления
     * вернуть ответ по модели ResponseEvent - событие STOP.
     */
    private ResponseEvent stopCalculating(String uid) {
        Calculation calculation = calculationDao.findByUid(uid);
        if (calculation.getStatusCalculation() == ResponseEvent.StatusCalculation.EXECUTING) {
            calculation.setStatusCalculation(ResponseEvent.StatusCalculation.STOPPED);
            calculationDao.updateCalculation(calculation);
            calculation.getCalculateMainThread().stopCalculation();
            return ResponseEvent.newBuilder()
                    .setUid(uid)
                    .setMessage("вычесления остановлены")
                    .build();
        }
        return ResponseEvent.newBuilder()
                .setUid(uid)
                .setMessage("вычесления не могут остановлены")
                .setStatus(calculation.getStatusCalculation())
                .build();
    }

    /**
     * Запускает ранее остановленое вычисление - событие RECOMMENCE.
     */
    /*private ResponseEvent recommenceCalculating(String requestUid) {
        Calculation calculation = calculationDao.findByUid(requestUid);

        if (calculation.getStatusCalculation() != ResponseEvent.StatusCalculation.STOPPED) {
            return ResponseEvent.newBuilder()
                    .setUid(requestUid)
                    .setMessage("вычесления не могут возобновлены")
                    .setStatus(calculation.getStatusCalculation())
                    .build();
        }
        calculation.setStatusCalculation(ResponseEvent.StatusCalculation.EXECUTING);
        calculationDao.updateCalculation(calculation);
        localFactorialCalculate = new FactorialCalculate(calculation, calculationDao);
        localFactorialCalculate.setIteratorCalculat(calculation.getCurrentIteratorCalculat());
        Thread thread = new Thread(localFactorialCalculate);
        thread.start();

        return ResponseEvent.newBuilder()
                .setUid(requestUid)
                .setMessage("вычесления возобновлены")
                .build();
    }*/

    /**
     * Проверяет, имеется ли запись о вычислении с данным Uid.
     *
     * @param requestUid - Uid вычисления.
     */
    private boolean checkUid(String requestUid) {
        return calculationDao.checkCalculationUid(requestUid);
    }
}
