package ru.services.http.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.ServerRequest;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GrpcControllerServicesImpl.class)
class GrpcControllerServicesImplTest {

    @Autowired
    GrpcControllerServicesImpl grpcControllerServices;

    @Test
    void startCalculation() {
        ServerRequest request = ServerRequest.newBuilder()
                .setNumber(600).build();
        //String calculation = grpcControllerServices.startCalculation(request);
//        BigInteger bigInteger = calculate.calculatingFactorial(request.getNumber());
//        System.out.println(bigInteger);
       // System.out.println(calculation);
    }
}