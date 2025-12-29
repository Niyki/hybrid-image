package com.niki.hybridimage;

import org.openimaj.image.MBFImage;

import static java.lang.Math.floor;

/**
 * A utility class for generating hybrid images by combining low-pass and high-pass filtered images.
 */
public class MyHybridImages {

    /**
     * Create a hybrid image by combining the low-pass version of one image with the high-pass version of another.
     *
     * @param lowImage   Image to be low-pass filtered.
     * @param lowSigma   Standard deviation for low-pass Gaussian kernel.
     * @param highImage  Image to be high-pass filtered.
     * @param highSigma  Standard deviation for the low-pass component of the high-pass filter.
     * @return Combined hybrid image.
     */
    public static MBFImage makeHybrid(MBFImage lowImage, float lowSigma, MBFImage highImage, float highSigma) {
        // Apply low-pass filter
        MyConvolution lowConvolution = new MyConvolution(makeGaussianKernel(lowSigma));
        MBFImage lowPassed = lowImage.process(lowConvolution);

        // Apply low-pass filter to get the base for high-pass filtering
        MyConvolution highConvolution = new MyConvolution(makeGaussianKernel(highSigma));
        MBFImage highBlurred = highImage.process(highConvolution);

        // Subtract the blurred image from the original to get high-frequency components
        MBFImage highPassed = new MBFImage(highImage.getWidth(), highImage.getHeight());
        for (int bandIndex = 0; bandIndex < highImage.bands.size(); bandIndex++) {
            for (int x = 0; x < highImage.getWidth(); x++) {
                for (int y = 0; y < highImage.getHeight(); y++) {
                    highPassed.getBand(bandIndex).pixels[y][x] =
                            highImage.getBand(bandIndex).pixels[y][x] - highBlurred.getBand(bandIndex).pixels[y][x];
                }
            }
        }

        // Combine low-frequency and high-frequency components
        return sum(lowPassed, highPassed);
    }

    /**
     * Sum two MBFImages pixel-by-pixel.
     *
     * @param image1 First image.
     * @param image2 Second image.
     * @return Summed image.
     */
    public static MBFImage sum(MBFImage image1, MBFImage image2) {
        MBFImage result = new MBFImage(image1.getWidth(), image1.getHeight(), image1.getColourSpace());
        for (int bandIndex = 0; bandIndex < image1.bands.size(); bandIndex++) {
            for (int x = 0; x < image1.getCols(); x++) {
                for (int y = 0; y < image1.getRows(); y++) {
                    result.getBand(bandIndex).pixels[y][x] =
                            image1.getBand(bandIndex).pixels[y][x] + image2.getBand(bandIndex).pixels[y][x];
                }
            }
        }
        return result;
    }

    /**
     * Generate a 2D Gaussian kernel with values summing to 1.
     *
     * @param sigma Standard deviation of the Gaussian.
     * @return Normalized 2D Gaussian kernel.
     */
    public static float[][] makeGaussianKernel(float sigma) {
        // Calculate kernel size: ensure it's odd
        int size = (int) floor(8 * sigma + 1);
        if (size % 2 == 0) size++;

        int center = size / 2;
        float[][] kernel = new float[size][size];
        double sum = 0;

        // Compute Gaussian weights
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                int dx = x - center;
                int dy = y - center;

                double exponent = -(dx * dx + dy * dy) / (2 * sigma * sigma);
                double value = Math.exp(exponent);
                kernel[x][y] = (float) value;
                sum += value;
            }
        }

        // Normalize kernel so that total sum is 1
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                kernel[x][y] /= (float) sum;
            }
        }

        return kernel;
    }
}
