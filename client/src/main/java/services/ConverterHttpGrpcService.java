package services;

public interface ConverterHttpGrpcService {
    void sendRequest(Integer number, Integer treads);

    void stopCalculat(Integer id);

    void getCalculatStatus(Integer id);
}
