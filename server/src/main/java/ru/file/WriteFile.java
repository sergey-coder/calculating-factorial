package ru.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.domain.Calculation;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WriteFile {
    private static Logger logger = LoggerFactory.getLogger(WriteFile.class);

    public static String PATH_FILE = "server/src/main/resources/calculation-data.txt";
    private static Path path;

    private final ReadWriteLock readWriteLock;

    public WriteFile(ReadWriteLock readWriteLock) {
        this.readWriteLock = readWriteLock;
    }

    @PostConstruct
    public void init(){
        logger.info("создание файла для хранения данных");
        path = Paths.get(PATH_FILE);
        try {
            Files.createFile(path);
        } catch (IOException e) {
            logger.info("файл для хранения данных уже создан");
        }
    }

    public void saveDataCalculating(Calculation calculation){
        logger.info("запись в файл");
        readWriteLock.writeLock();
        try {
            Files.write(path, Collections.singleton(calculation.toString()), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            readWriteLock.writeUnlock();
        }
    }

    private void saveDataCalculating(String calculation){
        try {
            Files.write(path, Collections.singleton(calculation), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteDataCalculating(String uid){
        logger.info("удаление записи из файла");
        readWriteLock.writeLock();
        try {
            List<String> recordsToSave = Files.lines(path).filter(record -> !record.contains(uid)).collect(Collectors.toList());
            Files.delete(path);
            Files.createFile(path);
            recordsToSave.forEach(this::saveDataCalculating);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            readWriteLock.writeUnlock();
        }
    }

}
