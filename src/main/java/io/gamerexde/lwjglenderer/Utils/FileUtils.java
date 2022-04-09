package io.gamerexde.lwjglenderer.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface FileUtils {
    static String loadAsString(String file) {
        try {
            return Files.readString(Path.of(file));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
