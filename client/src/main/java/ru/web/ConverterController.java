package ru.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.model.CalculatingRequest;
import ru.model.CalculatingRespons;
import ru.services.http.ConverterHttpGrpcService;

/**
 * Контроллер для приема HTTP запросов от пользователя
 * с последующей конвертацией в grpc и направлением для
 * производства вычисленний на сервер
 * <p>
 * Все запросы от пользователя должены передаваться через rest в формате json
 */
@RestController
@RequestMapping(value = "**/api/converter", consumes = "application/json", produces = "application/json")
public class ConverterController {

    private static Logger logger = LoggerFactory.getLogger(ConverterController.class);

    private final ConverterHttpGrpcService service;

    public ConverterController(@Autowired ConverterHttpGrpcService service) {
        this.service = service;
    }

    /**
     * Принимаем Post запрос на старт вычислений с двумя обязательными параметрами
     * Integer number Число от которого вычисляется факториал
     * Integer treads Количество потоков
     */
    @PostMapping("/start")
    public CalculatingRespons requestStartCalculating(@RequestBody CalculatingRequest calculatingRequest) {
        printLog(calculatingRequest, "start");
        return service.startCalculat(calculatingRequest);
    }

    /**
     * Принимает Post запрос для остановки ранее запущенного вычесления,
     * RequestBody должен содержать uid вычисления.
     */
    @PostMapping("/stop")
    public CalculatingRespons stopCalculatingId(@RequestBody CalculatingRequest calculatingRequest) {
        printLog(calculatingRequest, "stop");
        return service.stopCalculat(calculatingRequest);
    }

    /**
     * Принимает Post запрос для получения информации
     * о статусе вычеслений по его UD
     * <p>
     * Если вычисление по идентификатору не найдено должен возвращаться статус «Не запущено»
     * Если вычисление по идентификатору выполняется должен возвращаться статус «Выполняется. Завершено потоков X из Y»
     * Если вычисление по идентификатору завершено, должен возвращаться статус «Завершено. Значение факториала X равно Y»
     */
    @PostMapping("/status")
    public CalculatingRespons getCalculatingStatus(@RequestBody CalculatingRequest calculatingRequest) {
        printLog(calculatingRequest, "status");
        return service.getCalculatStatus(calculatingRequest);
    }

    /**
     * Принимает Post запрос для возобновления остановленого вычесления по его UID
     */
    @PostMapping("/recommence")
    public CalculatingRespons recommenceCalculating(@RequestBody CalculatingRequest calculatingRequest) {
        printLog(calculatingRequest, "recommence");
        return service.recommenceCalculating(calculatingRequest);
    }

    /**
     * Принимает Post запрос для получения результата вычесления по его UID
     */
    @PostMapping("/result")
    public CalculatingRespons getCalculatingResult(@RequestBody CalculatingRequest calculatingRequest) {
        printLog(calculatingRequest, "result");
        return service.getCalculatingResult(calculatingRequest);
    }

    private void printLog(CalculatingRequest calculatingRequest, String typerequest) {
        logger.info("Поступил HTTP запрос.Тип запроса: " + typerequest);
    }

}
