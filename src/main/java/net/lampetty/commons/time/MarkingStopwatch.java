package net.lampetty.commons.time;

import java.util.ArrayList;
import java.util.List;

/**
 * A stopwatch class to be able to mark texts for codes.
 * An example is below.
 * <pre>
 * MarkingStopWatch watch = new MarkingStopWatch();
 * watch.start();
 * Thread.sleep(100);
 * watch.mark("first");
 * Thread.sleep(100);
 * watch.mark("second");
 * Thread.sleep(100);
 * watch.stop();
 * System.out.println(watch.report());
 * </pre>
 * It produces
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
     * Starts this stopwatch
     */
    public void start() {
        startedTime = System.currentTimeMillis();    
    }

    /**
     * Add a mark to current code.
     * 
     * @param name a name of mark
     */
    public void mark(String name) {
        if (stoppedTime != -1) {
            throw new IllegalStateException("Already stopped.");
        }
        marks.add(new Mark(name, System.currentTimeMillis()));
    }
    
    /**
     * Stops this stopwatch.
     */
    public void stop() {
        if (stoppedTime == -1) {
            stoppedTime = System.currentTimeMillis();
        }
    }
    
    /**
     * Returns currently elapsed time.
     * 
     * @return elapsed time
     */
    public long getCurrentElapsedTime() {
        return System.currentTimeMillis() - startedTime;
    }
    
    /**
     * Stops this stopwatch and returns total elapsed time.
     * 
     * @return total elapsed time
     */
    public long getTotalTime() {
        stop();
        return stoppedTime - startedTime;
    }
    
    /**
     * Returns a report text like this.
     * <pre>
     * NAME                      TIME        CUMULATIVE      PERCENTAGE
     * read from database        0.123       0.123           34.462%
     * write to disk             0.234       0.357           65.530%
     * _stop_                    0.000       0.357           0.008%
     * </pre>
     * 
     * @return 
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
