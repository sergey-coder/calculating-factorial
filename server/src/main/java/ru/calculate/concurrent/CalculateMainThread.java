package ru.calculate.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ResponseEvent;
import ru.dao.CalculationDao;
import ru.domain.Calculation;
import ru.services.http.impl.GrpcControllerServicesImpl;
import ru.util.WriteToFile;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class CalculateMainThread implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(CalculateMainThread.class);

    private CalculationDao calculationDao;
    private Calculation calculation;
    private BigInteger finishResult;
    private ForkJoinPool forkJoinPool;

    public CalculateMainThread(CalculationDao calculationDao, Calculation calculation) {
        this.calculationDao = calculationDao;
        this.calculation = calculation;
    }

    @Override
    public void run() {
        startCalculation();
        saveResult();
    }

    public int getActiveThreadCount(){
        return forkJoinPool.getActiveThreadCount();
    }

    public void stopCalculation() {
        forkJoinPool.shutdownNow();
    }

    private void startCalculation(){
        WriteToFile.saveDataCalculating(calculation);
        TaskCalculation taskCalculation = new TaskCalculation(1, calculation.getNumber());
        forkJoinPool = new ForkJoinPool(calculation.getTreads());
        finishResult = forkJoinPool.invoke(taskCalculation);
    }

    private void saveResult() {
        calculation.setResultCalculation(finishResult.toString());
        calculation.setStatusCalculation(ResponseEvent.StatusCalculation.FINISHED);
        calculationDao.updateCalculation(calculation);
        WriteToFile.deleteDataCalculating(calculation.getUid());
        logger.info("вычисления с uid " + calculation.getUid() + " окончены" );
    }

    private class TaskCalculation extends RecursiveTask<BigInteger> {

        private  Integer startNumber;
        private final Integer finishNumber;

        public TaskCalculation(int startNumber, int finishNumber) {
            this.startNumber = startNumber;
            this.finishNumber = finishNumber;
        }

        @Override
        protected BigInteger compute() {
            if((finishNumber - startNumber) <= calculation.getTreads()){
                BigInteger currentResult = BigInteger.ONE;
                while (startNumber <= finishNumber) {
                    currentResult = currentResult.multiply(BigInteger.valueOf(startNumber));
                    startNumber++;
                    try {
                        Thread.sleep(10000  );
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
                BigInteger resultSecondTread= secondTread.compute();
                return firstTread.join().multiply(resultSecondTread);
            }
        }
    }


}
