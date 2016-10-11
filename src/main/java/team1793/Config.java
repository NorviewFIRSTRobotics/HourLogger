package team1793;

import java.io.*;
import java.util.Properties;

/**
 * Created by tyler on 10/10/16.
 */
public class Config {
    public static final File mainDir = new File(System.getProperty("user.home") + "/HourLogger/");
    public static final File configDir = new File(mainDir, "config.properties");
    public static File saveDir = new File(mainDir, "members");
    public static final File qrDir = new File(mainDir, "qr");

    public static Properties properties = new Properties();

    public static void init() {
        load();
        properties.setProperty("saveDir",saveDir.getAbsolutePath());
        save();
    }
    public static void load() {
        InputStream input = null;
        try {
            input = new FileInputStream(configDir);
            // load a properties file
            properties.load(input);
            saveDir = new File(properties.getProperty("saveDir"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void save() {
        OutputStream output = null;
        try {
            output = new FileOutputStream(configDir);
            // save properties to project root folder
            properties.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static void setSaveDir(File saveDir) {
        properties.setProperty("saveDir",( Config.saveDir = saveDir).getAbsolutePath());
        save();
    }
}
