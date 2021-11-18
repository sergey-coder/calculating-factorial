package client.services.grpc;

import client.model.CalculatingRequest;
import client.model.CalculatingRespons;

public interface GrpcService {
    CalculatingRespons sendGrpcRequest(CalculatingRequest calculatingRequest);
}
