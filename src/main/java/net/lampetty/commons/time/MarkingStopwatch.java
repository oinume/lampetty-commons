package net.lampetty.commons.time;

import java.util.ArrayList;
import java.util.List;

/**
 * 処理の実行時間にマークを付けることができるストップウォッチです。
 * 
 * <pre>
 * MarkingStopWatch watch = new MarkingStopWatch();
 * watch.start();
 * watch.mark("first");
 * watch.mark("second");
 * watch.stop();
 * watch.report();
 * </pre>
 * 
 * <pre>
 * 
 * </pre>
 */
public class MarkingStopwatch {

    private List<Mark> marks = new ArrayList<Mark>();
    private long startedTime = -1;
    private long stoppedTime = -1;
    
    /**
     * ストップウォッチを開始します。
     */
    public void start() {
        startedTime = System.currentTimeMillis();    
    }

    /**
     * 終わった処理にマークをつけます。{@link #report()}メソッドで使用されます。
     * 
     * @param name マークの名前
     */
    public void mark(String name) {
        if (stoppedTime != -1) {
            throw new IllegalStateException("Already stopped.");
        }
        marks.add(new Mark(name, System.currentTimeMillis()));
    }
    
    /**
     * ストップウォッチを停止します。
     */
    public void stop() {
        if (stoppedTime == -1) {
            stoppedTime = System.currentTimeMillis();
        }
    }
    
    /**
     * 現在の時刻 - 開始時間を取得します。
     * 
     * @return 現在の時刻 - 開始時間
     */
    public long getCurrentElapsedTime() {
        return System.currentTimeMillis() - startedTime;
    }
    
    /**
     * 合計時間を取得します。stop() - start()の差になります。
     * 
     * @return 合計時間
     */
    public long getTotalTime() {
        stop();
        return stoppedTime - startedTime;
    }
    
    /**
     * 処理時間をまとめて下記のような文字列を返します。
     * 
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
            long cumulative = mark.time - startedTime; // 累積時間
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
