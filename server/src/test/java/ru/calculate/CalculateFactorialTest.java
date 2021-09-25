package ru.calculate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.ResponseEvent;
import ru.dao.impl.CalculationDaoImpl;
import ru.domain.Calculation;
import ru.file.WriteFile;

import java.math.BigInteger;
import java.util.stream.Stream;

class CalculateFactorialTest {

    /**
     * Проверяем общую логику работы в одном потоке.
     */
    @Test
    void startCalculationVerifyMethod() {
        WriteFile writeFile = Mockito.mock(WriteFile.class);
        CalculationDaoImpl calculationDaoImpl = Mockito.mock(CalculationDaoImpl.class);

        Calculation calculation = new Calculation();
        calculation.setUid("testUid");
        calculation.setTreads(40);

        CalculateFactorial calculateFactorial = new CalculateFactorial(writeFile, calculationDaoImpl, calculation);
        calculateFactorial.run();

        Mockito.verify(writeFile, Mockito.times(1)).saveDataCalculating(calculation);
        Mockito.verify(writeFile, Mockito.times(1)).deleteDataCalculating(calculation.getUid());
    }

    /**
     * Проверяем математическую верность вычислений в ForkJoinPool.
     *
     * @param testData исходные тестовые данные сгенерированные в stream.
     */
    @ParameterizedTest
    @MethodSource("testDataGenerator")
    void startCalculationCorrectResult(Integer testData) {
        WriteFile writeFile = Mockito.mock(WriteFile.class);
        CalculationDaoImpl calculationDaoImpl = Mockito.mock(CalculationDaoImpl.class);

        Calculation calculation = new Calculation();
        calculation.setUid("testUid");
        calculation.setTreads(10);
        calculation.setNumber(testData);

        CalculateFactorial calculateFactorial = new CalculateFactorial(writeFile, calculationDaoImpl, calculation);
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
        WriteFile writeFile = Mockito.mock(WriteFile.class);
        CalculationDaoImpl calculationDaoImpl = Mockito.mock(CalculationDaoImpl.class);

        Calculation calculation = new Calculation();
        calculation.setUid("testUid");
        calculation.setTreads(10);
        calculation.setNumber(20);

        CalculateFactorial calculateFactorial = new CalculateFactorial(writeFile, calculationDaoImpl, calculation);
        calculateFactorial.run();

        Assertions.assertEquals("2432902008176640000", calculation.getResultCalculation());
        Assertions.assertEquals(ResponseEvent.StatusCalculation.FINISHED, calculation.getStatusCalculation());
        Assertions.assertEquals("testUid", calculation.getUid());
    }

}