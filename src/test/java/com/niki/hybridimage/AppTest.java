package com.niki.hybridimage;

import org.junit.Test;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Test class for Hybrid Image functionality.
 * Includes tests for Gaussian kernel creation, convolution, and hybrid image generation.
 */
public class AppTest {

    // Test the creation and output of a small Gaussian kernel.
    @Test
    public void testKernelGeneration() {
        float[][] kernel = MyHybridImages.makeGaussianKernel(0.5f);
        System.out.println(Arrays.deepToString(kernel));
    }

    /**
     * Test the convolution process on each band of an image.
     * Applies a Gaussian blur to each channel individually and reconstructs the image.
     */
    @Test
    public void testConvolve() throws IOException {
        // Load RGB image from local path
        File imageFile = new File("src/main/resources/Dark_Side_of_the_Moon.png");
        MBFImage originalImage = ImageUtilities.readMBF(imageFile);
        DisplayUtilities.display(originalImage, "Original Image");

        // Extract individual RGB bands
        FImage red = originalImage.getBand(0);
        FImage green = originalImage.getBand(1);
        FImage blue = originalImage.getBand(2);

        // Apply Gaussian convolution with sigma = 5
        MyConvolution convolution = new MyConvolution(MyHybridImages.makeGaussianKernel(5f));
        convolution.processImage(red);
        convolution.processImage(green);
        convolution.processImage(blue);

        // Reconstruct image from processed bands
        MBFImage blurredImage = new MBFImage();
        blurredImage.addBand(red);
        blurredImage.addBand(green);
        blurredImage.addBand(blue);
        DisplayUtilities.display(blurredImage, "Blurred Image");

        // Pause to keep the window open
        try {
            Thread.sleep(10000); // 10 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test creating a hybrid image by combining a low-pass and high-pass image.
     */
    @Test
    public void testHybridImageCreation() throws IOException {
        // Load the low-pass image
        File lowImageFile = new File("src/main/resources/einstein.bmp");
        MBFImage lowImage = ImageUtilities.readMBF(lowImageFile);
        DisplayUtilities.display(lowImage, "Low-Pass Image");

        // Load the high-pass image
        File highImageFile = new File("src/main/resources/marilyn.bmp");
        MBFImage highImage = ImageUtilities.readMBF(highImageFile);
        DisplayUtilities.display(highImage, "High-Pass Image");

        // Create and display hybrid image
        MBFImage hybridImage = MyHybridImages.makeHybrid(lowImage, 2f, highImage, 2.5f);
        DisplayUtilities.display(hybridImage, "Hybrid Image");

        // Pause to keep the window open
        try {
            Thread.sleep(10000); // 10 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
