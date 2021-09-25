package ru.services.impl;

import org.springframework.stereotype.Service;
import ru.RequestEvent;
import ru.ResponseEvent;
import ru.calculate.CalculateFactorial;
import ru.dao.impl.CalculationDaoImpl;
import ru.domain.Calculation;
import ru.file.WriteFile;
import ru.services.ExecuteEvent;
import ru.util.UidGenerator;

/**
 * Реализует выполнение event Start.
 */
@Service
public class ExecuteStartEvent implements ExecuteEvent {

    private final WriteFile writeFile;
    private final CalculationDaoImpl calculationDaoImpl;

    public ExecuteStartEvent(WriteFile writeFile, CalculationDaoImpl calculationDaoImpl) {
        this.writeFile = writeFile;
        this.calculationDaoImpl = calculationDaoImpl;
    }

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
        Calculation calculation = new Calculation();
        calculation.setUid(UidGenerator.generate());
        calculation.setNumber(request.getNumber());
        calculation.setTreads(request.getTreads());
        calculation.setStatusCalculation(ResponseEvent.StatusCalculation.EXECUTING);

        startCalculation(calculation);

        return ResponseEvent.newBuilder()
                .setUid(calculation.getUid())
                .setMessage("вычисления успешно начаты")
                .build();
    }

    /**
     * Сохраняет ссылку на инстанс CalculateFactorial,
     * Запускает вычисления факториала в отдельном потоке.
     *
     * @param calculation созданная запись о вычислении.
     */
    public void startCalculation(Calculation calculation) {
        CalculateFactorial calculateFactorial = new CalculateFactorial(writeFile, calculationDaoImpl, calculation);
        calculation.setCalculateFactorial(calculateFactorial);
        calculationDaoImpl.addNewCalculation(calculation);

        Thread thread = new Thread(calculateFactorial);
        thread.start();
    }
}
