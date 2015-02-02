import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_features2d.*;
import org.bytedeco.javacpp.opencv_nonfree;
import org.bytedeco.javacpp.opencv_nonfree.*;

import java.io.File;
import java.nio.file.Path;
import java.util.Comparator;

import static org.bytedeco.javacpp.opencv_features2d.drawKeypoints;
import static org.bytedeco.javacpp.opencv_features2d.drawMatches;
import static org.bytedeco.javacpp.opencv_highgui.*;

/**
 * Created by kkuznets on 10.09.2014.
 */
public class Tester {

    public static final String SIFT = "SIFT";

    public static void main(String[] testImages){
//        SIFT sift = new SIFT(10);

        ORB sift = new ORB();
        int patternsNumber = testImages.length;

        final Mat image1 = imread(testImages[0]);
        final Mat image2 = imread(testImages[1]);

        final KeyPoint keyPoints1 = new KeyPoint();
        final KeyPoint keyPoints2 = new KeyPoint();

        Mat featureDescriptors1 = new Mat();
        Mat featureDescriptors2 = new Mat();

        detectKeypoints(sift, image1, keyPoints1, featureDescriptors1);
        detectKeypoints(sift, image2, keyPoints2, featureDescriptors2);

        BFMatcher matcher = new BFMatcher();
        DMatch matches = new DMatch(10);
        matcher.match(featureDescriptors1, featureDescriptors2, matches);

        Mat outImage = new Mat();
        Scalar keypointColor = new Scalar(CvScalar.WHITE);

        matches = selectBest(matches, 10);
        drawMatches(image1, keyPoints1, image2, keyPoints2, matches,  outImage);

        File image = new File(testImages[0]);
        String resultPath = image.getAbsolutePath();
        imwrite(resultPath + "resultImage.jpg", outImage);
        namedWindow("Result");
        imshow("Result", outImage);
        while(waitKey() != 'q');

        return;
    }

    private static void detectKeypoints(ORB sift, Mat image, KeyPoint keyPoints, Mat featureDescriptors) {
//        sift.detectAndCompute(image, new Mat(), keyPoints, featureDescriptors);
        new SIFT().apply(image, new Mat(), keyPoints, featureDescriptors);
    }

    private static DMatch selectBest(DMatch matches, int size) {
        int oldPosition = matches.position();
        DMatch a[] = new DMatch[matches.capacity()];
        for (int i = 0; i < a.length; i++) {
            DMatch src = matches.position(i);
            DMatch dest = new DMatch();
            copy(src, dest);
            a[i] = dest;
        }
        // Reset position explicitly to avoid issues from other uses of this
        // position-based container.
        matches.position(oldPosition);

        // Sort

        DMatch aSorted[] = a;
        java.util.Arrays.sort(aSorted, new DistanceComparator());

        // DMatch aSorted[]=sort(a);

        // Create new JavaCV list
        DMatch best = new DMatch(size);
        for (int i = 0; i < size; i++) {
            copy(aSorted[i], best.position(i));
        }
        best.position(0);

        return best;
    }

    private static void copy(DMatch src, DMatch dest) {
        dest.distance(src.distance());
        dest.imgIdx(src.imgIdx());
        dest.queryIdx(src.queryIdx());
        dest.trainIdx(src.trainIdx());
    }

    static class DistanceComparator implements Comparator<DMatch> {
        public int compare(DMatch o1, DMatch o2) {
            return (int) Math.signum(o1.distance() - o2.distance());
        }
    };
}
