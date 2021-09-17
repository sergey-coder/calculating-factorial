package ru.services.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.RequestEvent;
import ru.ResponseEvent;
import ru.ServerEndpointGrpc;

@Component
public class SendRequestToGrpcServer {

    private static Logger logger = LoggerFactory.getLogger(SendRequestToGrpcServer.class);
    /**
     * Зарезирвированный local Url для gRPC server.
     */
    private static final String ADRESS_SERVER = "static://localhost:9091";

    /**
     * Отправляет запросы на gRPC server.
     * return синхронный ответ gRPC server.
     */
    public ResponseEvent sendRequestToServer(RequestEvent requestEvent) {
        logger.info("отправка по протоколу gRPC запроса с uid "
                + requestEvent.getUid()
                + " тип запроса "
                + requestEvent.getTypeEvent());

        logger.info("дебаговый лог "
                + "Uid " + requestEvent.getUid() + "\\n"
                + "Number " + requestEvent.getNumber() + "\\n"
                + "Treads " + requestEvent.getTreads() + "\\n"
                + "TypeEvent " + requestEvent.getTypeEvent());

        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(ADRESS_SERVER)
                .usePlaintext()
                .build();
        ServerEndpointGrpc.ServerEndpointBlockingStub blockingStub = ServerEndpointGrpc.newBlockingStub(channel);
        ResponseEvent responseEvent = blockingStub.getEventCalculation(requestEvent);
        channel.shutdown();
        return responseEvent;
    }

}
