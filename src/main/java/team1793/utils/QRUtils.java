package team1793.utils;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import team1793.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by tyler on 10/7/16.
 */
public class QRUtils {
    public static File generateQR(String name) {
        FileOutputStream outputStream;
        File file = new File(Config.qrDir,name.replace(" ","_") + ".png");
        try {
            if(!Config.qrDir.exists()) Config.qrDir.mkdirs();
            outputStream = new FileOutputStream(file);
            QRCode.from(name).to(ImageType.PNG).writeTo(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }
}
