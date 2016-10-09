package team1793.utils;

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by tyler on 10/8/16.
 */
public class CameraUtils {
    public static void main(String[] args) {
        // get default webcam and open it
        Webcam webcam = Webcam.getDefault();
        webcam.open();

        // get image
        BufferedImage image = webcam.getImage();

        // save image to PNG file
        try {
            ImageIO.write(image, "PNG", new File("test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
