package com.avsystem.fred.hello;

public class Zuo {
    public static void main(String[] args) {
        // Arrays in Java are covariant. The language doesn't stop you from assigning a String[] into an Object[].
        Object[] objarr = args;
        // ArrayStoreException ¯\_(ツ)_/¯
        objarr[0] = new Object();
    }
}
