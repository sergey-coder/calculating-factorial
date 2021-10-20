package ru.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.RequestEvent;
import ru.ResponseEvent;
import ru.calculate.CalculateFactorial;
import ru.calculate.domain.Calculation;
import ru.calculate.impl.CalculationDaoImpl;
import ru.dao.ServiceRecommenceCalculate;
import ru.services.ExecuteEvent;
import ru.services.util.UidGenerator;

/**
 * Реализует выполнение event Start.
 */
@Service
public class ExecuteStartEvent implements ExecuteEvent {

    @Autowired
    private CalculationDaoImpl calculationDaoImpl;

    @Autowired
    private ServiceRecommenceCalculate serviceRecommenceCalculate;

    /**
     * Для хранения данных о вычислении.
     */
    private  Calculation calculation;

    /**
     * Формирует запись в памяти о вычислении,
     * устанавливает статус вычислений на EXECUTING.
     * Возвращает ответ с uid вычисления.
     *
     * @param request запрос от client по модели RequestEvent.
     * @return ответ по модели ResponseEvent.
     */
    @Override
    public ResponseEvent startEvent(RequestEvent request) {
        createCalculation(request);
        startCalculation();

        return ResponseEvent.newBuilder()
                .setUid(calculation.getUid())
                .setMessage("вычисления успешно начаты")
                .build();
    }

    /**
     * Запускает вычисления факториала.
     * @param calculation данные о вычислении по модели Calculation.
     */
    public void startCalculation(Calculation calculation) {
        this.calculation = calculation;
        startCalculation();
    }

    /**
     * Сохраняет ссылку на инстанс CalculateFactorial,
     * Запускает вычисления факториала в отдельном потоке.
     */
    private void startCalculation() {
        CalculateFactorial calculateFactorial = new CalculateFactorial(serviceRecommenceCalculate, calculationDaoImpl, calculation);
        calculation.setCalculateFactorial(calculateFactorial);
        calculationDaoImpl.addNewCalculation(calculation);

        Thread thread = new Thread(calculateFactorial);
        thread.start();
    }

    /**
     * Формирует запись о вычислении по модели Calculation.
     * @param request запрос от client по модели RequestEvent.
     */
    private void createCalculation(RequestEvent request){
        calculation = new Calculation();
        calculation.setUid(UidGenerator.generate());
        calculation.setNumber(request.getNumber());
        calculation.setTreads(request.getTreads());
        calculation.setStatusCalculation(ResponseEvent.StatusCalculation.EXECUTING);
    }

}
