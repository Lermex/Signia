package uk.ac.uel.signia.util;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class NativeLibraryLoader {

    private static File temporaryDirectory;

    public static void loadNatives() {
        try {
            if (temporaryDirectory == null) {
                temporaryDirectory = createTempDirectory();
                temporaryDirectory.deleteOnExit();
            }

            copy("/opencv_java340.dll");
            copy("/libopencv_java340.dylib");

            // Force JVM to reevaluate java.library.path
            System.setProperty("java.library.path", temporaryDirectory.getAbsolutePath() );
            Field fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
            fieldSysPath.setAccessible( true );
            fieldSysPath.set( null, null );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void copy(String path) throws IOException {
        File temp = new File(temporaryDirectory, path);
        try (InputStream is = NativeLibraryLoader.class.getResourceAsStream(path)) {
            Files.copy(is, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (NullPointerException e) {
            throw new FileNotFoundException("File " + path + " was not found inside JAR.");
        }
    }

    private static File createTempDirectory() throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        System.out.println(tempDir);
        File generatedDir = new File(tempDir, "signianatives");

        if (!generatedDir.exists() && !generatedDir.mkdir()) {
            throw new IOException("Failed to create temp directory " + generatedDir.getName());
        }

        return generatedDir;
    }
}
