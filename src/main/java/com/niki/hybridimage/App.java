package com.niki.hybridimage;

import java.io.File;
import java.io.IOException;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;

public class App {
    public static void main( String[] args ) throws IOException {
        // Change path to desired low pass image (blurry)
        File imageFile1 = new File("src/main/resources/einstein.bmp");
        MBFImage image1 = ImageUtilities.readMBF(imageFile1);
        DisplayUtilities.display(image1);

        // Change path to desired high pass image (edges)
        File imageFile2 = new File("src/main/resources/marilyn.bmp");
        MBFImage image2 = ImageUtilities.readMBF(imageFile2);
        DisplayUtilities.display(image2);

        DisplayUtilities.display(MyHybridImages.makeHybrid(image1, 2f, image2, 2.5f));

        // Pause to keep the window open
        try {
            Thread.sleep(10000); // 10 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}