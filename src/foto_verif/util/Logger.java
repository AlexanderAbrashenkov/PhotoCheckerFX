package foto_verif.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by market6 on 15.11.2016.
 */
public class Logger {
    private static StringBuilder message = new StringBuilder();

    public static void log(String message1) {
        message.append(message1 + "\n");
    }

    public static void writeLog() {
        if (message.length() > 0) {
            String path = new File(".").getAbsolutePath();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(path + "/log.txt"));
                writer.write(message.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
