package GUI.util;

import javafx.scene.text.Font;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FontLoadUtil {

    public static Font loadFont(String url, double size) {
        try {
            // Create a URL object with the font URL
            URL fontUrl = new URL(url);

            // Open a connection to the URL
            URLConnection connection = fontUrl.openConnection();

            // Get an InputStream from the connection
            InputStream fontStream = connection.getInputStream();

            // Load the font with the desired size
            return Font.loadFont(fontStream, size);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load font from " + url, e);
        }
    }
}
