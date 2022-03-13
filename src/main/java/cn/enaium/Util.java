package cn.enaium;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Enaium
 */
public class Util {
    private static final AtomicLong atomicLong = new AtomicLong();

    public static long increment() {
        return atomicLong.incrementAndGet();
    }
}
