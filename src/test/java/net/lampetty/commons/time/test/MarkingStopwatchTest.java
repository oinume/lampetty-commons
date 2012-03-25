package net.lampetty.commons.time.test;

import net.lampetty.commons.time.MarkingStopwatch;

import org.junit.Test;

public class MarkingStopwatchTest {
    
    @Test
    public void testReport() throws Exception {
        MarkingStopwatch stopwatch = new MarkingStopwatch();
        stopwatch.start();
        Thread.sleep(100);
        stopwatch.mark("1");
        Thread.sleep(100);
        stopwatch.mark("2");
        Thread.sleep(100);
        stopwatch.stop();
        
        System.out.println(stopwatch.report());
    }
}
