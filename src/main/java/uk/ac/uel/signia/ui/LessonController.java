package uk.ac.uel.signia.ui;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import uk.ac.uel.signia.hardware.CameraAdapter;
import uk.ac.uel.signia.recognition.GestureRecognizer;

import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class LessonController extends ParentAware {

    @FXML
    private ImageView cameraView;

    @FXML
    private Text recognitionText;

    private AtomicInteger frameCounter = new AtomicInteger(0);

    @FXML
    public void initialize() {
        CameraAdapter cameraAdapter = new CameraAdapter();

        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    BufferedImage image = cameraAdapter.getImage();
                    Image jfxImage = SwingFXUtils.toFXImage(image, null);
                    cameraView.setImage(jfxImage);

                    int currentFrame = frameCounter.getAndIncrement();
                    if (currentFrame % 3 == 0) {
                        String category = GestureRecognizer.recognize(image);
                        System.out.println(category);
                        if (category.equals("unknown")) {
                            recognitionText.setText("...");
                        } else {
                            recognitionText.setText("Are you signing \"" + category + "\"?");
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }, 0, 100);
    }

}
