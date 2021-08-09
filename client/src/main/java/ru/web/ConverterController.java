package ru.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.model.CalculatingRequest;
import ru.services.ConverterHttpGrpcService;

/**
 * Контроллер для приема HTTP запросов от пользователя
 * с последующей конвертацией в grpc и направлением для
 * производства вычисленний на сервер
 *
 * Все запросы от пользователя должены передаваться через rest в формате json
 */
@RestController
@RequestMapping(value = "/api/converter"/*, consumes = "application/json", produces = "application/json"*/)
public class ConverterController {

    ConverterHttpGrpcService service;

    public ConverterController(@Autowired ConverterHttpGrpcService service) {
        this.service = service;
    }

    /**
     * Принимаем Post запрос с двумя обязательными параметрами
     * Integer number Число от которого вычисляется факториал
     * Integer treads Количество потоков
     */
    @PostMapping
    public void requestCalculating(@RequestBody CalculatingRequest calculatingRequest){
         service.sendRequest(calculatingRequest);
    }

    /**
     * Принимает Post запрос для остановки ранее запущенного вычесления
     */
    @PostMapping("/stop")
    public void stopCalculatingId(@RequestBody CalculatingRequest calculatingRequest){
        service.stopCalculat(calculatingRequest);
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
        service.getCalculatStatus(calculatingRequest);
    }

}
