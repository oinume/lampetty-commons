package net.lampetty.commons.time.test;

import net.lampetty.commons.time.MarkingStopwatch;

import org.junit.BeforeClass;
import org.junit.Test;

public class MarkingStopwatchTest {

    private static MarkingStopwatch stopwatch;
    
    @BeforeClass
    public static void beforeClass() {
        stopwatch = new MarkingStopwatch();
    }
    
    @Test
    public void testReport() throws Exception {
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
