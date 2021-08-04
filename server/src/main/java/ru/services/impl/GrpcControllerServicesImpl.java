package ru.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.services.Calculate;
import ru.services.GrpcControllerServices;

import java.math.BigInteger;

@Service
public class GrpcControllerServicesImpl implements GrpcControllerServices {

    private final Calculate calculate;

    public GrpcControllerServicesImpl(@Autowired Calculate calculate) {
        this.calculate = calculate;
    }

    /**
     * Получить результат вычеслений
     */
    public BigInteger getResultCalculating(){
        return calculate.getResult();
    }

    /**
     * Получить текущий статус вычеслений
     */
    public Boolean getStatusCalculating(){
        return calculate.getStopCalculate();
    }

    /**
     * Принудительно остановить вычесления
     */
    public String stopCalculating(){
        calculate.setStopCalculate(true);
        return "вычесления остановлены";
    }
}
