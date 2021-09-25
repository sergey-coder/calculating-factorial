package ru.file;

import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;

@Component
public class ReadWriteLock {

    private final Semaphore readSemaphore = new Semaphore(1);
    private final Semaphore writeSemaphore = new Semaphore(1);

    /**
     * Количество потоков на чтение файла.
     */
    private int count = 0;

    /**
     * Во время работы потока на чтение, блокируем доступ потока на запись.
     */
    public void readLock() {
        try {
            readSemaphore.acquire();
            if(count == 0){
                writeSemaphore.acquire();
            }
            count++;
            readSemaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * После окончания работы потока на чтение, если больше нет других потоков на чтение - разблокирует доступ на запись.
     */
    public void readUnLock() {
        try {
            readSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count--;
        if(count == 0){
            writeSemaphore.release();
        }
        readSemaphore.release();
    }

    /**
     * Во время работы потока на запись, блокируем доступ на запись кому-либо еще.
     */
    public void writeLock() {
        try {
            writeSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * После окончания работы потока на запись, разблокируем доступ другим потокам.
     */
    public void writeUnlock(){
        writeSemaphore.release();
    }
}
