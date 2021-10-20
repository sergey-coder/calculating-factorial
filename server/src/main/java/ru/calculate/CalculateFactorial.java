package ru.calculate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ResponseEvent;
import ru.calculate.domain.Calculation;
import ru.calculate.impl.CalculationDaoImpl;
import ru.dao.ServiceRecommenceCalculate;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Вычисляет значение факториала переданного числа.
 */
public class CalculateFactorial implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(CalculateFactorial.class);

    private final ServiceRecommenceCalculate serviceRecommenceCalculate;
    private final CalculationDaoImpl calculationDaoImpl;
    private final Calculation calculation;
    private BigInteger finishResult;
    private ForkJoinPool forkJoinPool;

    public CalculateFactorial(
            ServiceRecommenceCalculate serviceRecommenceCalculate,
            CalculationDaoImpl calculationDaoImpl,
            Calculation calculation) {
        this.serviceRecommenceCalculate = serviceRecommenceCalculate;
        this.calculationDaoImpl = calculationDaoImpl;
        this.calculation = calculation;
    }

    /**
     * Запускает инстанс класса в отдельном потоке.
     */
    @Override
    public void run() {
        startCalculation();
        saveResult();
    }

    /**
     * Предоставляет сведения о количестве активных потоков.
     *
     * @return количество активных потоков.
     */
    public int getActiveThreadCount() {
        return forkJoinPool.getActiveThreadCount();
    }

    /**
     * Останавливает выполнение текущих задач, блокирует предоставление новых.
     */
    public void stopCalculation() {
        forkJoinPool.shutdownNow();
    }

    /**
     * Сохраняет в файл запись о вычислении.
     * Создает пулл потоков через ForkJoinPool.
     * Создает инстанс TaskCalculation класса и передает его на исполнении в ForkJoinPool.
     */
    private void startCalculation() {
        serviceRecommenceCalculate.saveDataCalculating(calculation);
        TaskCalculation taskCalculation = new TaskCalculation(1, calculation.getNumber());
        forkJoinPool = new ForkJoinPool(calculation.getTreads());
        finishResult = forkJoinPool.invoke(taskCalculation);
    }

    /**
     * После окончания вычислений сохраняет информацию о результатах, удаляет ранее сделанную запись из файла.
     */
    private void saveResult() {
        calculation.setResultCalculation(finishResult.toString());
        calculation.setStatusCalculation(ResponseEvent.StatusCalculation.FINISHED);
        calculationDaoImpl.updateCalculation(calculation);
        serviceRecommenceCalculate.deleteDataCalculating(calculation.getUid());
        logger.info("вычисления с uid " + calculation.getUid() + " окончены");
    }

    /**
     * Реализует ForkJoinTask для решения задач рекурсивно в ForkJoinPool.
     */
    private class TaskCalculation extends RecursiveTask<BigInteger> {

        private Integer startNumber;
        private final Integer finishNumber;

        public TaskCalculation(int startNumber, int finishNumber) {
            this.startNumber = startNumber;
            this.finishNumber = finishNumber;
        }

        /**
         * Запускает выполнение переданных задач.
         *
         * @return результат вычисления факториала числа.
         */
        @Override
        protected BigInteger compute() {
            if ((finishNumber - startNumber) <= calculation.getTreads()) {
                BigInteger currentResult = BigInteger.ONE;
                while (startNumber <= finishNumber) {
                    currentResult = currentResult.multiply(BigInteger.valueOf(startNumber));
                    startNumber++;
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return currentResult;
            } else {
                int mid = (startNumber + finishNumber) / 2;
                TaskCalculation firstTread = new TaskCalculation(startNumber, mid);

                firstTread.fork();
                TaskCalculation secondTread = new TaskCalculation(mid + 1, finishNumber);
                BigInteger resultSecondTread = secondTread.compute();
                return firstTread.join().multiply(resultSecondTread);
            }
        }
    }

}
