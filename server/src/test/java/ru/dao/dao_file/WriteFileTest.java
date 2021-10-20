package ru.dao.dao_file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.calculate.domain.Calculation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WriteFile.class)
class WriteFileTest {

    @SpyBean
    private ReadWriteLock readWriteLock;

    /**
     * Проверяем правильность записи в тестовый файл данных о вычислении.
     */
    @Test
    void saveDataCalculating() throws IOException {
        WriteFile writeFile = new WriteFile(readWriteLock);
        WriteFile.PATH_FILE = "src/test/resources/testFileForWrite.txt";
        writeFile.init();

        Calculation calculationForSave = new Calculation();
        calculationForSave.setNumber(333);
        calculationForSave.setTreads(222);
        calculationForSave.setUid("testUid");

        writeFile.saveDataCalculating(calculationForSave);

        Calculation calculationForCompare = new Calculation();
        String recordCalculation = Files.lines(Paths.get(WriteFile.PATH_FILE)).filter(record -> record.contains(calculationForSave.getUid())).findFirst().get();
        String[] strings = recordCalculation.split(",");
        calculationForCompare.setUid(strings[0]);
        calculationForCompare.setNumber(Integer.parseInt(strings[1]));
        calculationForCompare.setTreads(Integer.parseInt(strings[2]));

        Assertions.assertEquals(calculationForCompare.getUid(), calculationForSave.getUid());
        Assertions.assertEquals(calculationForCompare.getTreads(), calculationForSave.getTreads());
        Assertions.assertEquals(calculationForCompare.getNumber(), calculationForSave.getNumber());
    }

    /**
     * Проверяем удаляется ли сохраненная запись о вычислении из тестового файла.
     */
    @Test
    void deleteDataCalculating() throws IOException {
        WriteFile writeFile = new WriteFile(readWriteLock);
        WriteFile.PATH_FILE = "src/test/resources/testFileForWrite.txt";
        writeFile.init();

        Calculation calculation = new Calculation();
        calculation.setNumber(555);
        calculation.setTreads(666);
        calculation.setUid("testUid111");

        writeFile.saveDataCalculating(calculation);
        Optional<String> recordCalculationTrue = Files.lines(Paths.get(WriteFile.PATH_FILE)).filter(record -> record.contains(calculation.getUid())).findFirst();
        assertTrue(recordCalculationTrue.isPresent());

        writeFile.deleteDataCalculating(calculation.getUid());
        Optional<String> recordCalculationFalse = Files.lines(Paths.get(WriteFile.PATH_FILE)).filter(record -> record.contains(calculation.getUid())).findFirst();

        assertFalse(recordCalculationFalse.isPresent());
    }
}