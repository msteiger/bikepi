package bikepi.pedal;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.MinMaxPriorityQueue;

/**
 * TODO: describe
 */
public class SpeedController {

    private static final Logger logger = LoggerFactory.getLogger(SpeedController.class);

    private static final int MAX_ELEMENTS = 10;
    private static final double METER_PER_PING = 2.1;    // default 28 inch bike tire

    private final EvictingQueue<Instant> queue = EvictingQueue.create(MAX_ELEMENTS);
    private Instant latest = Instant.now();

    private double gearMultiplier = 2.0;

    private double distance;

    public synchronized void pedalPing() {
        latest = Instant.now();
        queue.add(latest);

        distance += METER_PER_PING * gearMultiplier;

        logger.debug("Pedal ping received: {}", latest);
    }

    public synchronized double getSpeed() {
        int count = queue.size();
        if (count < 2) {
            return 0;
        }

        Instant oldest = queue.peek();
        Duration d = Duration.between(oldest, latest);
        double secs = d.toMillis() * 0.001;
        double meter = METER_PER_PING * count * gearMultiplier;
        double speed_ms = meter / secs;

        return speed_ms * 3.6;
    }

    /**
     * @return the distance in meter
     */
    public double getDistance() {
        return distance;
    }

    public void reset() {
        distance = 0;
    }
}
