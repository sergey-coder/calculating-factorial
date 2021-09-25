package ru.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.domain.Calculation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Класс для чтения данных из файла.
 */
@Component
public class ReadFile {

    private final static Logger logger = LoggerFactory.getLogger(ReadFile.class);
    static Path path = Paths.get(WriteFile.PATH_FILE);

    private final ReadWriteLock readWriteLock;

    public ReadFile(ReadWriteLock readWriteLock) {
        this.readWriteLock = readWriteLock;
    }

    /**
     * Получает запись о вычислении из файла,
     * парсит ее, создает новую запись о вычислении в памяти по модели Calculation.
     * @param uid uid вычисления, наличие записи о  котором уже проверено.
     * @return запись о вычислении по модели Calculation.
     */
    public Calculation getCalculation(String uid){
        logger.info("предоставление данных для старта вычислений");
        Calculation calculation = new Calculation();
        readWriteLock.readLock();
        try {
            String recordCalculation = Files.lines(path).filter(record -> record.contains(uid)).findFirst().get();
            String[] strings = recordCalculation.split(",");
            calculation.setUid(strings[0]);
            calculation.setNumber(Integer.parseInt(strings[1]));
            calculation.setTreads(Integer.parseInt(strings[2]));
            return calculation;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            readWriteLock.readUnLock();
        }
        return calculation;
    }

    /**
     * Получает данные из файла, проверяет содержится ли запись с переданным uid.
     * @param uid uid вычисления, наличие записи о  котором необходимо проверить.
     * @return true есть такая запись имеется.
     */
    public boolean checkCalculation(String uid){
        logger.info("проверка наличия незавершенного вычисления");
        readWriteLock.readLock();
        try {
            Optional<String> recordCalculation = Files.lines(path).filter(record -> record.contains(uid)).findFirst();
            return recordCalculation.isPresent();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            readWriteLock.readUnLock();
        }
        return false;
    }

}
