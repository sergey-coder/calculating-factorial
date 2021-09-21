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
import ru.services.http.ConverterHttpService;

/**
 * Контроллер для приема HTTP запросов от пользователя
 * с последующей конвертацией в grpc и направлением для
 * производства вычисленний на сервер
 * Все запросы от пользователя должены передаваться через rest в формате json
 */
@RestController
@RequestMapping(value = "**/api/converter", consumes = "application/json", produces = "application/json")
public class ConverterController {

    private static final Logger logger = LoggerFactory.getLogger(ConverterController.class);

    private final ConverterHttpService service;

    public ConverterController(@Autowired ConverterHttpService service) {
        this.service = service;
    }

    /**
     * Принимаем Post запрос на запуск события с вычислением факториала. вычислений с двумя обязательными параметрами
     * @param calculatingRequest запрос от UI пользователя по модели CalculatingRequest.
     *         запрос по событию START запускает вычисления, должен содержать три обязательных параметра:
     *                 Integer number Число от которого вычисляется факториал
     *                 Integer treads Количество потоков
     *                 TypeEvent START
     *
     *         запрос по событию STOP останавливает ранее запущенное вычесления, должен содержать два обязательных параметра:
     *                 String uid вычисления
     *                 TypeEvent STOP
     *
     *         запрос по событию GET_STATUS предоставляет информацию о вычислении, должен содержать два обязательных параметра:
     *                 String uid вычисления
     *                 TypeEvent GET_STATUS
     *              Если вычисление по идентификатору не найдено возвращаться статус «Не запущено»
     *              Если вычисление по идентификатору выполняется возвращаться статус «Выполняется. Завершено потоков X из Y»
     *              Если вычисление по идентификатору завершено, возвращаться статус «Завершено. Значение факториала X равно Y»
     *
     *         запрос по событию RECOMMENCE возобновляет вычисление, если оно не завершено,
     *                           в следствие остановки сервера, должен содержать два обязательных параметра:
     *                 String uid вычисления
     *                 TypeEvent RECOMMENCE
     *
     *         запрос по событию RESULT выдает результат вычесления по его UID, должен содержать два обязательных параметра:
     *                 String uid вычисления
     *                 TypeEvent RESULT
     *
     * @return ответ от gRPC сервера с ответом по модели CalculatingRespons.
     */
    @PostMapping("/factorial")
    public CalculatingRespons getEventCalculating(@RequestBody CalculatingRequest calculatingRequest) {
        printLog(calculatingRequest);
        return service.startEventCalculating(calculatingRequest);
    }

    private void printLog(CalculatingRequest calculatingRequest) {
        logger.info("Поступил HTTP запрос.Тип event: " + calculatingRequest.getTypeEvent());
    }

}
