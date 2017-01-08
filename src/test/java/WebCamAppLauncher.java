import com.github.sarxos.webcam.Webcam;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicReference;


/**
 * This example demonstrates how to use Webcam Capture API in a JavaFX application.
 *
 * @author Rakesh Bhatt (rakeshbhatt10)
 */
public class WebCamAppLauncher extends Application {

    private BorderPane root;
    private ImageView imgWebCamCapturedImage;
    private Webcam webCam = null;
    private boolean stopCamera = false;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
    private BorderPane webCamPane;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Connecting Camera Device Using Webcam Capture API");

        root = new BorderPane();
        webCamPane = new BorderPane();

        webCamPane.setStyle("-fx-background-color: #ccc;");
        imgWebCamCapturedImage = new ImageView();
        webCamPane.setCenter(imgWebCamCapturedImage);

        root.setCenter(webCamPane);
        webCam = Webcam.getDefault();
        Task<Void> webCamTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                webCam.open();
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        final AtomicReference<WritableImage> ref = new AtomicReference<>();
                        BufferedImage img;
                        while (!stopCamera) {
                            try {
                                if ((img = webCam.getImage()) != null) {
                                    ref.set(SwingFXUtils.toFXImage(img, ref.get()));
                                    img.flush();

                                    Platform.runLater(() -> imageProperty.set(ref.get()));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }
                };

                Thread th = new Thread(task);
                th.setDaemon(true);
                th.start();
                imgWebCamCapturedImage.imageProperty().bind(imageProperty);

                return null;
            }
        };
        Thread webCamThread = new Thread(webCamTask);
        webCamThread.setDaemon(true);
        webCamThread.start();

        primaryStage.setScene(new Scene(root));
        primaryStage.setHeight(700);
        primaryStage.setWidth(600);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}