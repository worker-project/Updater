package com.workerai.updater.utils;

public class ThrowWait {
    public static void throwWait(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
