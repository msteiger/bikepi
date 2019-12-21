package bikepi.pedal;

import java.util.function.Consumer;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * TODO: describe
 */
public class RealPedal {

    private GpioController controller;
    private Pin pedalPin;
    private GpioPinDigitalInput pedalInput;

    public RealPedal(Pin pin, Consumer<Void> consumer) {
        controller = GpioFactory.getInstance();
        pedalPin = pin; //RaspiPin.GPIO_17;

        pedalInput = controller.provisionDigitalInputPin(pedalPin, PinPullResistance.PULL_DOWN);

        pedalInput.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        pedalInput.addListener(new GpioUsageExampleListener(consumer));
    }

    public static class GpioUsageExampleListener implements GpioPinListenerDigital {

        private Consumer<Void> consumer;

        public GpioUsageExampleListener(Consumer<Void> consumer) {
            this.consumer = consumer;
        }

        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            if (event.getState() == PinState.HIGH) {
                consumer.accept(null);
            }
        }
    }
}
