package ru.services.grpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.*;
import ru.model.CalculatingRespons;


import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;
import java.util.Objects;

@Component
public class SendRequestToGrpcServer {

    private static Logger logger = LoggerFactory.getLogger(SendRequestToGrpcServer.class);
    private static final String ADRESS_SERVER = "static://localhost:9091";

    /**
     * Отправляет запросы на совершение действий с уже имеющемися вычеслениями.
     * Отправляет подготовленый ServerRequest на gRPC server.
     * В отдельный метод выделен в целях тестирования класса.
     */
    public ResponseEvent sendRequestToServer(RequestEvent requestEvent){
        logger.info("отправка по протоколу gRPC запроса с uid "
                + requestEvent.getUid()
                + " тип запроса "
                + requestEvent.getTypeEvent());

        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(ADRESS_SERVER)
                .usePlaintext()
                .build();
        ServerEndpointGrpc.ServerEndpointBlockingStub blockingStub = ServerEndpointGrpc.newBlockingStub(channel);
        ResponseEvent responseEvent = blockingStub.getEventCalculation(requestEvent);
        channel.shutdown();
        return responseEvent;
    }

    public void sendStartRequestToServer(ServerRequest startEvent) {
        logger.info("отправка по протоколу gRPC запроса на старт вычислений, количество потоков "
                + startEvent.getTreads()
                + " число "
                + startEvent.getNumber());

        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(ADRESS_SERVER)
                .usePlaintext()
                .build();
        ServerEndpointGrpc.ServerEndpointBlockingStub blockingStub = ServerEndpointGrpc.newBlockingStub(channel);
        Iterator<ServerResponse> serverResponseIterator = blockingStub.processingCalculationStream(startEvent);

        ServerResponse serverResponse;
        CalculatingRespons calculatingRespons;
        while (serverResponseIterator.hasNext()){
            serverResponse = serverResponseIterator.next();
            calculatingRespons = new CalculatingRespons();
            calculatingRespons.setMessage(serverResponse.getMessage());
            calculatingRespons.setUid(serverResponse.getUid());
            if(!serverResponse.getResultCalculating().equals("")){
                calculatingRespons.setResultCaculating(serverResponse.getResultCalculating());
            }

            ResponseEntity responseEntity =null;
            try {
                responseEntity = ResponseEntity
                        .status(200)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ObjectMapper().writeValueAsString(calculatingRespons));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            sendResponseByCustomer(startEvent.getUrlRequest(), responseEntity);

        }
        channel.shutdown();
    }

    private void sendResponseByCustomer(String urlRequest, ResponseEntity responseEntity) {
        System.out.println("Входящий URL " + urlRequest);


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + urlRequest))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(Objects.requireNonNull(responseEntity.getBody()).toString()))
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

}
