package com.avsystem.fred.hello;

import scala.collection.JavaConversions;
import scala.collection.Seq;
import scala.collection.Traversable;
import scala.collection.immutable.Map;

import java.util.Arrays;

/**
 * @author ghik
 */
public class Kąponęt {
    interface MyClock {
        long currentTimeMillis();
    }

    public Kąponęt(MyClock clock) {
//        strSupplier.get();
//        strSupplier.get();
    }

    public static Integer getInteger() {
        System.out.println("GETTING INTEGER");
        return 42;
    }

    public static void main(String[] args) {
        Seq<String> scalaSeq = JavaConversions.asScalaBuffer(
                Arrays.asList("foo", "bar"));

        Map<Integer, Traversable<String>> map =
                scalaSeq.groupBy(String::length);

        new Kąponęt(System::currentTimeMillis);

        switch (42) {
            case 42: {
                String lol = "fuu";
            }
            default: {
                String lol = "fuu";
            }
        }
    }
}
