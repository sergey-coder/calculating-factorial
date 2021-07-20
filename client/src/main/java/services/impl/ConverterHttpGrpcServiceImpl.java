package services.impl;

import org.springframework.stereotype.Service;
import services.ConverterHttpGrpcService;

@Service
public class ConverterHttpGrpcServiceImpl implements ConverterHttpGrpcService {

    /**
     *  Преобразует HTTP запрос в grpc
     *  Механизм отправки на end point сервера еще не определен
     */
    @Override
    public void sendRequest(Integer number, Integer treads) {
        // TODO
    }

    /**
     * Отправляет некий event на сервер с целью остановки вычесления
     * @param id
     */
    @Override
    public void stopCalculat(Integer id) {
        //TODO
    }

    /**
     * Отправляет некий event на сервер с целью получения информации
     * о текущем состоянии вычесления по его id
     * @param id
     */
    @Override
    public void getCalculatStatus(Integer id) {
        //TODO
    }

    /**
     * Метод выдает идентификатор вычисления для конкретного запроса
     */
    private int getIdRequest(){
        //TODO
        return 0;
    }
}
