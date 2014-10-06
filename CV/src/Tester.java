import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_features2d.*;
import org.bytedeco.javacpp.opencv_nonfree.*;

import static org.bytedeco.javacpp.opencv_highgui.imread;

/**
 * Created by kkuznets on 10.09.2014.
 */
public class Tester {
    public static void main(String[] testImages){
        SIFT sift = new SIFT();
        final opencv_core.Mat image = imread(testImages[0]);
        final KeyPoint keyPoints = new KeyPoint();
        // JVM crashes when JavaCV native binaries and OpenCV binaries are build with different versions of VisualStudio
        // For instance, JavaCV is build with VC10 and OpenCV with VC11.

        sift.detect(image, keyPoints);
        float size = keyPoints.size();
    }
}
