package com.avsystem.fred.hello;

import java.util.List;
import java.util.stream.IntStream;

public class Zuo {
    public static void main(String[] args) {
        // Arrays in Java are covariant. The language doesn't stop you from assigning a String[] into an Object[].
        Object[] objarr = args;
        // ArrayStoreException ¯\_(ツ)_/¯
        objarr[0] = new Object();

        String msg = "costam";
        IntStream.range(0, 5).forEach(i -> System.out.println(msg + " " + i));
    }

    public static String type() {
        return "fuu";
    }

    public static void wypisz(List<Object> objs) {
        System.out.println(objs);
    }
}
