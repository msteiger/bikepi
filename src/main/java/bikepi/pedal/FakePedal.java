package bikepi.pedal;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

/**
 * TODO: describe
 */
public class FakePedal implements Pedal {

    public FakePedal(Duration dur, final Runnable consumer) {

        Timer timer = new Timer("fakePedal", true);
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                try {
                    Thread.sleep((long) (Math.random() * dur.toMillis() / 4));
                } catch (InterruptedException e) {
                    // ignore
                }
                consumer.run();
            }
        };
        timer.schedule(task, dur.toMillis(), dur.toMillis());
    }
}
