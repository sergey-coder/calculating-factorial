package ru.services.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Генерирует Uid вычисления.
 */
public class UidGenerator {
    private static final int SIZE = 5;

    private UidGenerator() {
    }

    /**
     * Генерирует Uid вычисления.
     *
     * @return рандомный String длинной 5 символов.
     */
    public static String generate() {
        return RandomStringUtils.random(SIZE, "1234567890lkjhscvbnm");
    }
}
