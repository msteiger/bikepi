package bikepi;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

/**
 * TODO: describe
 */
public class FakePedalContoller implements PedalController {

    public FakePedalContoller(Duration dur, final Consumer<Void> consumer) {

        Timer timer = new Timer("fakePedal", true);
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                consumer.accept(null);
            }
        };
        timer.schedule(task, dur.toMillis(), dur.toMillis());
    }
}
