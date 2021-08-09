package ru.grpc.impl;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;
import ru.ServerEndpointGrpc;
import ru.ServerRequest;
import ru.grpc.GrpcService;
import ru.model.CalculatingRequest;
import ru.model.TypeEvent;

@Component
public class GrpcServiceImpl implements GrpcService {

    private static final String ADRESS_SERVER = "https://localhost:5001";


    @Override
    public void sendGrpcRequest(TypeEvent typeEvent, CalculatingRequest calculatingRequest) {
        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(ADRESS_SERVER)
                .usePlaintext()
                .build();
        ServerEndpointGrpc.ServerEndpointBlockingStub blockingStub = ServerEndpointGrpc.newBlockingStub(channel);
         blockingStub.process(ServerRequest.newBuilder()
                 .setNumber(555l)
                 .setTreads(456l)
                 .setUid("erger")
                 .build());
        channel.shutdown();
    }

    /**
     * Преобразует UI запрос пользователя в gRPC request.
     */
    private ServerRequest toServerRequest(CalculatingRequest calculatingRequest){
        return ServerRequest.newBuilder()
                .setTreads(calculatingRequest.getTreads())
                .setNumber(calculatingRequest.getNumber()).build();
    }
}
