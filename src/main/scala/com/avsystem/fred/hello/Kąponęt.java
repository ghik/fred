package com.avsystem.fred.hello;

import java.util.function.Supplier;

/**
 * @author ghik
 */
public class Kąponęt {
    interface MyClock {
        long currentTimeMillis();
    }

    public Kąponęt(Supplier<String> strSupplier) {
//        strSupplier.get();
//        strSupplier.get();
    }

    public static Integer getInteger() {
        System.out.println("GETTING INTEGER");
        return 42;
    }

    public static void main(String[] args) {
        new Kąponęt(getInteger()::toString);
    }
}
