package ru.dao.dao_file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.calculate.domain.Calculation;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ReadFile.class)
class ReadFileTest {

    @SpyBean
    private ReadWriteLock readWriteLock;

    /**
     * Тестируем проверку наличия записи с конкретным Uid  в файле.
     */
    @Test
    void checkCalculation() throws InterruptedException {
        ReadFile readFile = new ReadFile(readWriteLock);
        ReadFile.path = Paths.get("src/test/resources/testFile.txt");

        final boolean[] checkCalculation = new boolean[1];
        Thread thread = new Thread(() -> {
            checkCalculation[0] = readFile.checkCalculation("testUid");
        });
        thread.start();
        thread.join(1000);

        assertTrue(checkCalculation[0]);
    }

    /**
     * Тестируем возможность чтения файла при наличии блокировки чтения со стороны пишушего в файл потока.
     */
    @Test
    void checkCalculationWriteLock() throws InterruptedException {
        ReadFile readFile = new ReadFile(readWriteLock);
        ReadFile.path = Paths.get("src/test/resources/testFile.txt");

        final boolean[] checkCalculation = new boolean[1];

        Thread thread = new Thread(() -> {
            readWriteLock.writeLock();
            checkCalculation[0] = readFile.checkCalculation("testUid");
        });
        thread.start();
        thread.join(1000);

        assertFalse(checkCalculation[0]);
        readWriteLock.writeUnlock();
    }

    /**
     * Тестируем формирование записи о вычислении по модели Calculation на основании данных файла.
     */
    @Test
    void getCalculation() {
        ReadFile readFile = new ReadFile(readWriteLock);
        ReadFile.path = Paths.get("src/test/resources/testFile.txt");

        Calculation calculation = readFile.getCalculation("testUid");
        Assertions.assertEquals("testUid", calculation.getUid());
        Assertions.assertEquals(15, calculation.getNumber());
        Assertions.assertEquals(51, calculation.getTreads());
    }

}