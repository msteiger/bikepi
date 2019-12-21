package bikepi;

import java.time.Duration;
import java.util.function.Consumer;

/**
 * TODO: describe
 */
public class BikePiApplication {

    public static void main(String[] args) {
        Consumer<Void> consumer = (o) -> { System.out.println("PIN"); };

        PedalController controller = new FakePedalContoller(Duration.ofSeconds(1), consumer);

        System.out.println("df");
    }
}
