package ru.dao.dao_file;

import org.springframework.beans.factory.annotation.Autowired;
import ru.calculate.domain.Calculation;
import ru.dao.ServiceRecommenceCalculate;

/**
 * Предоставляет единый API для сохранения-записи данных о вычислении в текстовом файле.
 */
public class ServiceReadWriteFile implements ServiceRecommenceCalculate {

    private final ReadFile readFile;

    private final WriteFile writeFile;

    public ServiceReadWriteFile(
            @Autowired ReadFile readFile,
            @Autowired WriteFile writeFile) {
        this.readFile = readFile;
        this.writeFile = writeFile;
    }

    /**
     * Получает сохраненные данные о вычислении.
     * @param uid идентификатор вычисления.
     * @return данные о вычислении по модели Calculation.
     */
    @Override
    public Calculation getCalculation(String uid) {
        return readFile.getCalculation(uid);
    }

    /**
     * Проверяет наличие сохраненного вычисления.
     * @param uid идентификатор вычисления.
     * @return true если вычисление имеется.
     */
    @Override
    public boolean checkCalculation(String uid) {
        return readFile.checkCalculation(uid);
    }

    /**
     * Сохраняет данные о вычислении.
     * @param calculation данные о вычислении по модели Calculation.
     */
    @Override
    public void saveDataCalculating(Calculation calculation) {
        writeFile.saveDataCalculating(calculation);
    }

    /**
     * Удаляет данные о вычислении.
     * @param uid идентификатор вычисления.
     */
    @Override
    public void deleteDataCalculating(String uid) {
        writeFile.deleteDataCalculating(uid);
    }
}
