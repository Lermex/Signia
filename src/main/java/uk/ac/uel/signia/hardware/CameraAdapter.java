package uk.ac.uel.signia.hardware;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import uk.ac.uel.signia.util.NativeLibraryLoader;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

public class CameraAdapter {
    private VideoCapture capture;

    public CameraAdapter() {
        NativeLibraryLoader.loadNatives();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        capture = new VideoCapture(0);
    }

    public BufferedImage getImage() {
        if (!capture.isOpened()) {
            throw new RuntimeException("Video capture device must be opened first");
        }

        Mat webcamImage = new Mat();
        capture.read(webcamImage);

        if (!webcamImage.empty()) {
            return matToBufferedImage(webcamImage);
        } else {
            throw new RuntimeException("No image");
        }
    }

    private BufferedImage matToBufferedImage(Mat m) {
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), BufferedImage.TYPE_3BYTE_BGR);
        m.get(0, 0, ((DataBufferByte) image.getRaster().getDataBuffer()).getData());
        return image;
    }
}
