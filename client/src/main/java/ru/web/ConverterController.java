package ru.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.model.CalculatingRequest;
import ru.model.CalculatingRespons;
import ru.services.http.ConverterHttpGrpcService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Контроллер для приема HTTP запросов от пользователя
 * с последующей конвертацией в grpc и направлением для
 * производства вычисленний на сервер
 *
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
     * Принимаем Post запрос с двумя обязательными параметрами
     * Integer number Число от которого вычисляется факториал
     * Integer treads Количество потоков
     */
    @PostMapping
    public void requestStartCalculating(@RequestBody CalculatingRequest calculatingRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        printLog(calculatingRequest, "start");
        //переписан название сета
        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();
        calculatingRequest.setUrlRequest(uri.toString());
        service.startCalculat(calculatingRequest);
    }

    /**
     * Принимает Post запрос для остановки ранее запущенного вычесления
     */
    @PostMapping("/stop")
    public CalculatingRespons stopCalculatingId(@RequestBody CalculatingRequest calculatingRequest){
        printLog(calculatingRequest, "stop");
        return service.stopCalculat(calculatingRequest);
    }

    /**
     * Принимает Post запрос для получения информации
     * о статусе вычеслений по его UD
     *
     * Если вычисление по идентификатору не найдено должен возвращаться статус «Не запущено»
     * Если вычисление по идентификатору выполняется должен возвращаться статус «Выполняется. Завершено потоков X из Y»
     * Если вычисление по идентификатору завершено, должен возвращаться статус «Завершено. Значение факториала X равно Y»
     */
    @PostMapping("/status")
    public void getCalculatingStatus(@RequestBody CalculatingRequest calculatingRequest){
        printLog(calculatingRequest, "status");
        service.getCalculatStatus(calculatingRequest);
    }

    /**
     * Принимает Post запрос для возобновления остановленого вычесления по его UD
     */
    @PostMapping("/recommence")
    public void recommenceCalculating(@RequestBody CalculatingRequest calculatingRequest){
        printLog(calculatingRequest, "recommence");
        service.recommenceCalculating(calculatingRequest);
    }

    private void printLog(CalculatingRequest calculatingRequest, String typerequest){
        logger.info("Поступил HTTP запрос.Тип запроса: " + typerequest);
    }

}
