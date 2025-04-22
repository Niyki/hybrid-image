package com.niki.hybridimage;

import java.io.File;
import java.io.IOException;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;

public class App {
    public static void main( String[] args ) throws IOException {
        // Load the low-pass image (Change path to desired image)
        File lowImageFile = new File("src/main/resources/einstein.bmp");
        MBFImage lowImage = ImageUtilities.readMBF(lowImageFile);
        DisplayUtilities.display(lowImage, "Low-Pass Image");

        // Load the high-pass image (Change path to desired image)
        File highImageFile = new File("src/main/resources/marilyn.bmp");
        MBFImage highImage = ImageUtilities.readMBF(highImageFile);
        DisplayUtilities.display(highImage, "High-Pass Image");

        // Create and display hybrid image
        MBFImage hybridImage = MyHybridImages.makeHybrid(lowImage, 2f, highImage, 2.5f);
        DisplayUtilities.display(hybridImage, "Hybrid Image");
    }
}