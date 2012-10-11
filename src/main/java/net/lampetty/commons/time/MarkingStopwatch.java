package net.lampetty.commons.time;

import java.util.ArrayList;
import java.util.List;

/**
 * MarkingStopwatch enables you to add marks for a point of code.
 *
 * <pre>
 * MarkingStopWatch watch = new MarkingStopWatch();
 * watch.start();
 * watch.mark("first");
 * // Do something
 * watch.mark("second");
 * watch.stop();
 * System.out.println(watch.report());
 * </pre>
 * Run above, you can measure from mark to mark as follows.
 * <pre>
 * NAME                                   TIME(ms)    CUMULATIVE(ms)  PERCENTAGE
 * first                                   102         102             33.55%
 * second                                  101         203             33.22%
 * _stop_                                  101         304             33.22%
 * <pre>
 */
public class MarkingStopwatch {

    private List<Mark> marks = new ArrayList<Mark>();
    private long startedTime = -1;
    private long stoppedTime = -1;
    
    /**
     * Starts the stopwatch.
     */
    public void start() {
        startedTime = System.currentTimeMillis();    
    }

    /**
     * Adds a mark for some code.
     * 
     * @param name mark
     */
    public void mark(String name) {
        if (stoppedTime != -1) {
            throw new IllegalStateException("Already stopped.");
        }
        marks.add(new Mark(name, System.currentTimeMillis()));
    }
    
    /**
     * Stops the stopwatch.
     */
    public void stop() {
        if (stoppedTime == -1) {
            stoppedTime = System.currentTimeMillis();
        }
    }
    
    /**
     * Returns current time - started time.
     * 
     * @return currentTime - startedTime
     */
    public long getCurrentElapsedTime() {
        return System.currentTimeMillis() - startedTime;
    }
    
    /**
     * Returns stopped time - started time.
     * 
     * @return ���v����
     */
    public long getTotalTime() {
        stop();
        return stoppedTime - startedTime;
    }
    
    /**
     * Returns result like following string.
     * 
     * <pre>
     * NAME                      TIME        CUMULATIVE      PERCENTAGE
     * read from database        0.123       0.123           34.462%
     * write to disk             0.234       0.357           65.530%
     * _stop_                    0.000       0.357           0.008%
     * </pre>
     * 
     * @return string
     */
    public String report() {
        stop();
        marks.add(new Mark("_stop_", stoppedTime));

        StringBuilder out = new StringBuilder();
        out.append(String.format(
                "%-41.40s %-11s %-15s %s\n",
                "NAME", "TIME(ms)", "CUMULATIVE(ms)", "PERCENTAGE"));
        
        long previousTime = startedTime;
        long totalTime = getTotalTime();    
        for (Mark mark : marks) {
            long duration = mark.time - previousTime;
            long cumulative = mark.time - startedTime; // �ݐώ���
            double percentage = ((double)duration / (double)totalTime) * 100;
            
            out.append(String.format(
                    " %-41.40s %-11d %-15d %.2f%%\n",
                    mark.name, duration, cumulative, percentage));
            
            previousTime = mark.time;
        }
        
        return out.toString();
    }
    
    private static final class Mark {
        private String name;
        private long time;
        
        private Mark(String name, long time) {
            this.name = name;
            this.time = time;
        }
    }
}
