package ru.calculate.concurrent;

import ru.ResponseEvent;
import ru.dao.CalculationDao;
import ru.domain.Calculation;

import java.math.BigInteger;
import java.util.concurrent.*;

public class CalculateMainThread implements Runnable{

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
        TaskCalculation taskCalculation = new TaskCalculation(1, calculation.getNumber());
        forkJoinPool = new ForkJoinPool(calculation.getTreads());
        finishResult = forkJoinPool.invoke(taskCalculation);
    }

    private void saveResult() {
        calculation.setResultCalculation(finishResult.toString());
        calculation.setStatusCalculation(ResponseEvent.StatusCalculation.FINISHED);
        calculationDao.updateCalculation(calculation);
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
                    System.out.println("текущий результ " + currentResult);
                    startNumber++;
                    try {
                        Thread.sleep(10000 );
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
