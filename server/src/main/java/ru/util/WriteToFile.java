package ru.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.domain.Calculation;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class WriteToFile {

    private static Logger logger = LoggerFactory.getLogger(WriteToFile.class);

    private String pathFile = "server/src/main/resources/calculation-data.txt";
    private static Path path;

    @PostConstruct
    public void init(){
        logger.info("создание файла для хранения данных");
        path = Paths.get(pathFile);
        try {
            Files.createFile(path);
        } catch (IOException e) {
            logger.info("файл для хранения данных уже создан");
        }
    }

    public static void saveDataCalculating(Calculation calculation){
        logger.info("запись в файл");
        try {
            Files.write(path, Collections.singleton(calculation.toString()), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveDataCalculating(String calculation){
        try {
            Files.write(path, Collections.singleton(calculation), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDataCalculating(String uid){
        logger.info("удаление записи из файла");
        try {
            List<String> recordsToSave = Files.lines(path).filter(record -> !record.contains(uid)).collect(Collectors.toList());
            Files.delete(path);
            Files.createFile(path);
            recordsToSave.forEach(WriteToFile::saveDataCalculating);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Calculation getCalculation(String uid){
        logger.info("предоставление данных для старта вычислений");
        Calculation calculation = new Calculation();
        try {
            String recordCalculation = Files.lines(path).filter(record -> record.contains(uid)).findFirst().get();
            String[] strings = recordCalculation.split(",");
            calculation.setUid(strings[0]);
            calculation.setNumber(Integer.parseInt(strings[1]));
            calculation.setTreads(Integer.parseInt(strings[2]));
            //deleteDataCalculating(uid);
            return calculation;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return calculation;
    }

    public static boolean checkCalculation(String uid){
        logger.info("проверка наличия незавершенного вычисления");
        try {
            Optional<String> recordCalculation = Files.lines(path).filter(record -> record.contains(uid)).findFirst();
            return recordCalculation.isPresent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
