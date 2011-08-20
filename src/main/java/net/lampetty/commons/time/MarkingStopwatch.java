package net.lampetty.commons.time;

import java.util.ArrayList;
import java.util.List;

/**
 * �����̎��s���ԂɃ}�[�N��t���邱�Ƃ��ł���X�g�b�v�E�H�b�`�ł��B
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
     * �X�g�b�v�E�H�b�`���J�n���܂��B
     */
    public void start() {
        startedTime = System.currentTimeMillis();    
    }

    /**
     * �I����������Ƀ}�[�N�����܂��B{@link #report()}���\�b�h�Ŏg�p����܂��B
     * 
     * @param name �}�[�N�̖��O
     */
    public void mark(String name) {
        if (stoppedTime != -1) {
            throw new IllegalStateException("Already stopped.");
        }
        marks.add(new Mark(name, System.currentTimeMillis()));
    }
    
    /**
     * �X�g�b�v�E�H�b�`���~���܂��B
     */
    public void stop() {
        if (stoppedTime == -1) {
            stoppedTime = System.currentTimeMillis();
        }
    }
    
    /**
     * ���݂̎��� - �J�n���Ԃ��擾���܂��B
     * 
     * @return ���݂̎��� - �J�n����
     */
    public long getCurrentElapsedTime() {
        return System.currentTimeMillis() - startedTime;
    }
    
    /**
     * ���v���Ԃ��擾���܂��Bstop() - start()�̍��ɂȂ�܂��B
     * 
     * @return ���v����
     */
    public long getTotalTime() {
        stop();
        return stoppedTime - startedTime;
    }
    
    /**
     * �������Ԃ��܂Ƃ߂ĉ��L�̂悤�ȕ������Ԃ��܂��B
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
