package ru.calculate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.ResponseEvent;
import ru.calculate.domain.Calculation;
import ru.calculate.impl.CalculationDaoImpl;
import ru.dao.dao_file.ServiceReadWriteFile;

import java.math.BigInteger;
import java.util.stream.Stream;

class CalculateFactorialTest {

    /**
     * Проверяем общую логику работы в одном потоке.
     */
    @Test
    void startCalculationVerifyMethod() {
        CalculationDaoImpl calculationDaoImpl = Mockito.mock(CalculationDaoImpl.class);
        ServiceReadWriteFile serviceReadWriteFile = Mockito.mock(ServiceReadWriteFile.class);

        Calculation calculation = new Calculation();
        calculation.setUid("testUid");
        calculation.setTreads(40);

        CalculateFactorial calculateFactorial = new CalculateFactorial(serviceReadWriteFile, calculationDaoImpl, calculation);
        calculateFactorial.run();

        Mockito.verify(serviceReadWriteFile, Mockito.times(1)).saveDataCalculating(calculation);
        Mockito.verify(serviceReadWriteFile, Mockito.times(1)).deleteDataCalculating(calculation.getUid());
    }

    /**
     * Проверяем математическую верность вычислений в ForkJoinPool.
     *
     * @param testData исходные тестовые данные сгенерированные в stream.
     */
    @ParameterizedTest
    @MethodSource("testDataGenerator")
    void startCalculationCorrectResult(Integer testData) {
        CalculationDaoImpl calculationDaoImpl = Mockito.mock(CalculationDaoImpl.class);
        ServiceReadWriteFile serviceReadWriteFile = Mockito.mock(ServiceReadWriteFile.class);

        Calculation calculation = new Calculation();
        calculation.setUid("testUid");
        calculation.setTreads(10);
        calculation.setNumber(testData);

        CalculateFactorial calculateFactorial = new CalculateFactorial(serviceReadWriteFile, calculationDaoImpl, calculation);
        calculateFactorial.run();

        BigInteger resultForComparison = BigInteger.ONE;
        for (int i = 1; i <= testData; i++) {
            resultForComparison = resultForComparison.multiply(BigInteger.valueOf(i));
        }

        Assertions.assertEquals(resultForComparison.toString(), calculation.getResultCalculation());
    }

    static Stream<Integer> testDataGenerator() {
        return Stream.iterate(1, n -> n + 1).limit(100);
    }

    /**
     * Проверяем правильность формирования записи о результатах вычисления.
     */
    @Test
    void saveResult() {
        CalculationDaoImpl calculationDaoImpl = Mockito.mock(CalculationDaoImpl.class);
        ServiceReadWriteFile serviceReadWriteFile = Mockito.mock(ServiceReadWriteFile.class);

        Calculation calculation = new Calculation();
        calculation.setUid("testUid");
        calculation.setTreads(10);
        calculation.setNumber(20);

        CalculateFactorial calculateFactorial = new CalculateFactorial(serviceReadWriteFile, calculationDaoImpl, calculation);
        calculateFactorial.run();

        Assertions.assertEquals("2432902008176640000", calculation.getResultCalculation());
        Assertions.assertEquals(ResponseEvent.StatusCalculation.FINISHED, calculation.getStatusCalculation());
        Assertions.assertEquals("testUid", calculation.getUid());
    }

}