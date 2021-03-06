package uk.ac.uel.signia.recognition;

import org.tensorflow.Graph;
import org.tensorflow.Output;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import uk.ac.uel.signia.util.NativeLibraryLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GestureRecognizer {

    public static String recognize(BufferedImage input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(input, "png", baos);
        baos.flush();
        byte[] imageBytes = baos.toByteArray();
        baos.close();

        InputStream graphInputStream = NativeLibraryLoader.class.getResourceAsStream("/model/retrained_graph.pb");
        InputStream labelsInputStream = NativeLibraryLoader.class.getResourceAsStream("/model/retrained_labels.txt");
        byte[] graphDef = graphInputStream.readAllBytes();
        List<String> labels = readLines(labelsInputStream);


        try (Tensor<Float> image = constructAndExecuteGraphToNormalizeImage(imageBytes)) {
            float[] labelProbabilities = executeInceptionGraph(graphDef, image);
            int bestLabelIdx = maxIndex(labelProbabilities);
//            System.out.println(
//                String.format("BEST MATCH: %s (%.2f%% likely)",
//                    labels.get(bestLabelIdx),
//                    labelProbabilities[bestLabelIdx] * 100f));
            return labels.get(bestLabelIdx);
        }
    }

    private static Tensor<Float> constructAndExecuteGraphToNormalizeImage(byte[] imageBytes) {
        try (Graph g = new Graph()) {
            GraphBuilder b = new GraphBuilder(g);
            // The model was trained with images scaled to 224x224 pixels.
            final int H = 224;
            final int W = 224;
            final float mean = 128f;
            final float scale = 128f;

            // Since the graph is being constructed once per execution here, we can use a constant for the
            // input image. If the graph were to be re-used for multiple input images, a placeholder would
            // have been more appropriate.
            final Output<String> input = b.constant("input", imageBytes);
            final Output<Float> output =
                b.div(
                    b.sub(
                        b.resizeBilinear(
                            b.expandDims(
                                b.cast(b.decodeJpeg(input, 3), Float.class),
                                b.constant("make_batch", 0)),
                            b.constant("size", new int[]{H, W})),
                        b.constant("mean", mean)),
                    b.constant("scale", scale));
            try (Session s = new Session(g)) {
                return s.runner().fetch(output.op().name()).run().get(0).expect(Float.class);
            }
        }
    }

    private static float[] executeInceptionGraph(byte[] graphDef, Tensor<Float> image) {
        try (Graph g = new Graph()) {
            g.importGraphDef(graphDef);
            try (Session s = new Session(g);
                 Tensor<Float> result =
                     s.runner().feed("input", image).fetch("final_result").run().get(0).expect(Float.class)) {
                final long[] rshape = result.shape();
                if (result.numDimensions() != 2 || rshape[0] != 1) {
                    throw new RuntimeException(
                        String.format(
                            "Expected model to produce a [1 N] shaped tensor where N is the number of labels, instead it produced one with shape %s",
                            Arrays.toString(rshape)));
                }
                int nlabels = (int) rshape[1];
                return result.copyTo(new float[1][nlabels])[0];
            }
        }
    }

    private static int maxIndex(float[] probabilities) {
        int best = 0;
        for (int i = 1; i < probabilities.length; ++i) {
            if (probabilities[i] > probabilities[best]) {
                best = i;
            }
        }
        return best;
    }

    private static List<String> readLines(InputStream stream) {
        return new BufferedReader(new InputStreamReader(stream,
            StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
    }
}