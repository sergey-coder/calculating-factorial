package ru.util;

import org.apache.commons.lang3.RandomStringUtils;

public class UidGenerator {
    private  static  final int SIZE = 5;

    private UidGenerator(){}

    public static  String generate() {
        return RandomStringUtils.random(SIZE,"1234567890lkjhlkljs");
    }
}
