package nju.ora.base;

import java.time.LocalDate;

interface AlgoTimer {
    String today = LocalDate.now().toString();
    long start = System.currentTimeMillis();

    default void startTimer() {
        // In Java, instance variables cannot be changed in an interface
        // You can use a separate implementing class instead
        // For demonstration, let's assume we have a class that implements this interface
        long currentStart = System.currentTimeMillis();
    }

    default double time() {
        return 0.001 * (System.currentTimeMillis() - start);
    }
}

/**
 * @author FeiAiYue
 * @date 2024年11月02日 21:03
 * @description
 */
public class Base {
    public static final double EPS = 1e-6; // precision epsilon

    public static int ceilToInt(double x) {
        return (int) (x + 0.5);
    }

    public static boolean equal(double a, double b) {
        return Math.abs(a - b) < EPS;
    }

    public static boolean notEqual(double a, double b) {
        return Math.abs(a - b) > EPS;
    }
}
