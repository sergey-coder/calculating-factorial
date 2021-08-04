package ru.grpc.impl;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ru.ServerEndpointGrpc;
import ru.ServerRequest;
import ru.grpc.GrpcService;
import ru.model.CalculatingRequest;

public class GrpcServiceImpl implements GrpcService {

    private static final String ADRESS_SERVER = "http://localhost:8080";


    @Override
    public void sendGrpcRequest(CalculatingRequest calculatingRequest) {
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
}
