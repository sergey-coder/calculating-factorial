package ru.web;

/**
 * Контроллер входящих gRPC запросов
 */
public class GrpcController {
}
//    public ConverterController(@Autowired ConverterHttpGrpcService service) {
//        this.service = service;
//    }
//
//    /**
//     * Принимаем Post запрос с двумя обязательными параметрами
//     * Integer number Число от которого вычисляется факториал
//     * Integer treads Количество потоков
//     */
//    @PostMapping
//    public void requestCalculating(@RequestBody CalculatingRequest calculatingRequest){
//        service.sendRequest(calculatingRequest);
//    }
//
//    /**
//     * Принимает Post запрос для остановки ранее запущенного вычесления
//     */
//    @PostMapping("/stop")
//    public void stopCalculatingId(@RequestBody CalculatingRequest calculatingRequest){
//        service.stopCalculat(calculatingRequest);
//    }
//
//    /**
//     * Принимает Post запрос для получения информации
//     * о статусе вычеслений по его id
//     *
//     * Если вычисление по идентификатору не найдено должен возвращаться статус «Не запущено»
//     * Если вычисление по идентификатору выполняется должен возвращаться статус «Выполняется. Завершено потоков X из Y»
//     * Если вычисление по идентификатору завершено, должен возвращаться статус «Завершено. Значение факториала X равно Y»
//     */
//    @GetMapping("/status")
//    public void getCalculatingStatus(@RequestBody CalculatingRequest calculatingRequest){
//        service.getCalculatStatus(calculatingRequest);
//    }


//6. Сервер принимает запрос в gRPC, генерирует ID возвращает его через клиент пользователю, заполняет сущность, сохраняет в БД, запускает вычесление
//        - какая то логика по получению промежуточных результатов
//        - какая то логика по остановке вычеслений и возрату того чего есть
//        - какая то логика по постоянному сохранению данных в БД с целью возобновления вычеслений после остановки
