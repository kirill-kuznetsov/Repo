/**
 * Created by kkuznets on 10.09.2014.
 */
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

public class Smoother {
    public static void smooth(String filename) {
        IplImage image = cvLoadImage(filename);
        if (image != null) {
            cvSmooth(image, image);
            cvSaveImage(filename + "_sm", image);
            cvReleaseImage(image);
        }
    }
}