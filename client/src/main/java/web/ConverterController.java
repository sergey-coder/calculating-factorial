package web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import services.ConverterHttpGrpcService;

/**
 * Контроллер для приема HTTP запросов от пользователя
 * с последующей конвертацией в grpc и направлением для
 * производства вычисленний на сервер
 */
@RestController
@RequestMapping(value = "/api/converter", consumes = "application/json")
public class ConverterController {

    ConverterHttpGrpcService service;

    public ConverterController(@Autowired ConverterHttpGrpcService service) {
        this.service = service;
    }

    /**
     * Принимаем get запрос с двумя обязательными параметрами
     * Integer number Число от которого вычисляется факториал
     * Integer treads Количество потоков
     */
    @GetMapping
    public void requestCalculating(@RequestParam Integer number, @RequestParam Integer treads){
         service.sendRequest(number, treads);
    }

    /**
     * Принимает get запрос для остановки ранее запущенного вычесления
     * @param id
     */
    @GetMapping("/{id}")
    public void stopCalculatingId(@PathVariable Integer id){
        service.stopCalculat(id);
    }

    /**
     * Принимает get запрос для получения информации
     * о статусе вычеслений по его id
     * @param id
     *
     * Если вычисление по идентификатору не найдено должен возвращаться статус «Не запущено»
     * Если вычисление по идентификатору выполняется должен возвращаться статус «Выполняется. Завершено потоков X из Y»
     * Если вычисление по идентификатору завершено, должен возвращаться статус «Завершено. Значение факториала X равно Y»
     */
    @GetMapping("/{id}/status")
    public void getCalculatingStatus(@PathVariable Integer id){
        service.getCalculatStatus(id);
    }


}
