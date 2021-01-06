package com.tuannh.sraft.utility.timer;

import com.tuannh.sraft.commons.observer.Listener;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class DefaultTimerTest {

    @SuppressWarnings("java:S2925")
    @Test
    public void simpleTest() throws InterruptedException {
        AtomicInteger i = new AtomicInteger(0);
        List<Listener<Long>> listeners = Collections.singletonList(
                event -> System.out.printf("timer timeout %d time(s), at %d%n", i.incrementAndGet(), event)
        );
        Timer timer = DefaultTimer.create("timer", 500, listeners);
        Thread.sleep(4005); // add 5ms
        assertEquals(8, i.get());
        timer.reset(100);
        Thread.sleep(1005); // add 5ms
        assertEquals(18, i.get());
    }

    @SuppressWarnings("java:S2925")
    @Test
    public void simpleTest2() throws InterruptedException {
        Timer timer = DefaultTimer.create("timer", 500, Collections.singletonList(event -> System.out.println("A")));
        Timer timer1 = DefaultTimer.create("timer1", 200, Collections.singletonList(event -> System.out.println("B")));
        Thread.sleep(4005); // add 5ms
    }
}
