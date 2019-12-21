package bikepi;

import bikepi.pedal.FakePedal;
import bikepi.pedal.Pedal;
import bikepi.pedal.SpeedController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BikePiApplication extends Application {

    private SpeedController speedControl;
    private HeartController heartControl;

    @Override
    public void start(Stage primaryStage) {

        speedControl = new SpeedController();
        heartControl = new HeartController();
        Pedal controller = new FakePedal(java.time.Duration.ofSeconds(1), speedControl::pedalPing);


        primaryStage.setTitle("Overview");

        Group root = new Group();

        Scene scene = new Scene(root, 800, 600, Color.BLACK);
        primaryStage.setScene(scene);

        setupScene(scene);

        primaryStage.show();
    }

    /**
     * @param scene
     */
    private void setupScene(Scene scene) {
        Group root = (Group) scene.getRoot();

        Rectangle colors = background(scene);
        Group circles = createCircles();

        Rectangle rc = new Rectangle();
        rc.widthProperty().bind(scene.widthProperty());
        rc.heightProperty().bind(scene.heightProperty());
        rc.setFill(Color.color(0.4f, 0.4f, 0.4f));

        Group blendModeGroup = new Group(rc, colors, circles);
        colors.setBlendMode(BlendMode.OVERLAY);
        root.getChildren().add(blendModeGroup);

        addAnimation(circles);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        createUiElements(grid);

        root.getChildren().add(grid);
    }

    private void addAnimation(Group circles) {
        Timeline timeline = new Timeline();
        for (Node circle : circles.getChildren()) {
            timeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, // set start position at 0
                    new KeyValue(circle.translateXProperty(), Math.random() * 800),
                    new KeyValue(circle.translateYProperty(), Math.random() * 600)),
                    new KeyFrame(new Duration(10000),
                            new KeyValue(circle.translateXProperty(), Math.random() * 800),
                            new KeyValue(circle.translateYProperty(), Math.random() * 600)));
        }

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();
    }

    private void createUiElements(GridPane grid) {
        Font font = Font.font("Tahoma", FontWeight.NORMAL, 20);

        Text speedText = new Text();
        speedText.setFont(font);

        grid.add(speedText, 0, 0, 2, 1);

        Text distText = new Text();
        distText.setFont(font);
        grid.add(distText, 0, 1, 2, 1);

        Text heartBeatText = new Text();
        heartBeatText.setFont(font);
        grid.add(heartBeatText, 0, 2);

        Button resetButton = new Button("Reset");
        resetButton.setFont(font);
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                speedControl.reset();
            }
        });
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(resetButton);
        grid.add(hbBtn, 1, 3);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(500),
                ae -> {
                    distText.setText(String.format("Distance: %.2f km", speedControl.getDistance() * 0.001));
                    speedText.setText(String.format("Speed: %.1f km/h", speedControl.getSpeed()));
                    heartBeatText.setText(String.format("Heartbeat: %.0f/min", heartControl.getRate()));
                }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private Rectangle background(Scene scene) {
        Rectangle colors = new Rectangle(scene.getWidth(), scene.getHeight(),
                new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE,
                        new Stop[] { new Stop(0, Color.web("#f8bd55")), new Stop(0.14, Color.web("#c0fe56")),
                                new Stop(0.28, Color.web("#5dfbc1")), new Stop(0.43, Color.web("#64c2f8")),
                                new Stop(0.57, Color.web("#be4af7")), new Stop(0.71, Color.web("#ed5fc2")),
                                new Stop(0.85, Color.web("#ef504c")), new Stop(1, Color.web("#f2660f")), }));
        colors.widthProperty().bind(scene.widthProperty());
        colors.heightProperty().bind(scene.heightProperty());
        return colors;
    }

    private Group createCircles() {
        Group circles = new Group();
        for (int i = 0; i < 10; i++) {
            Circle circle = new Circle(150, Color.web("white", 0.05));
            circle.setStrokeType(StrokeType.OUTSIDE);
            circle.setStroke(Color.web("white", 0.16));
            circle.setStrokeWidth(4);
            circles.getChildren().add(circle);
        }
        circles.setEffect(new BoxBlur(10, 10, 3));
        return circles;
    }

}