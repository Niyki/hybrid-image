package com.niki.hybridimage;

import org.openimaj.image.FImage;
import org.openimaj.image.processor.SinglebandImageProcessor;

public class MyConvolution implements SinglebandImageProcessor<Float, FImage> {
    private final float[][] kernel;

    public MyConvolution(float[][] kernel) {
        // kernel is indexed by [row][column]
        this.kernel = kernel;
    }

    @Override
    public void processImage(FImage image) {
        // convolve image with kernel and store result back in image
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int kernelWidth = kernel.length;
        int kernelHeight = kernel[0].length;

        // Kernel center offsets
        int yOffset = (int) (double) (kernelHeight / 2);
        int xOffset = (int) (double) (kernelWidth / 2);

        // Create an output image of the same size
        FImage convolvedImage = image.newInstance(imageWidth, imageHeight);

        // Iterate over every pixel in the image
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                float sum = 0;

                // Apply kernel centered at (x, y)
                for (int ky = 0; ky < kernelHeight; ky++) {
                    for (int kx = 0; kx < kernelWidth; kx++) {
                        int ix = x + kx - xOffset;
                        int iy = y + ky - yOffset;

                        // Handle boundary with zero padding
                        if (ix >= 0 && ix < imageWidth && iy >= 0 && iy < imageHeight) {
                            sum += image.pixels[iy][ix] * kernel[ky][kx];
                        }
                        // else multiply by zero (implicitly ignored)
                    }
                }
                // Assign computed value to the output image
                convolvedImage.pixels[y][x] = sum;
            }
        }
        // Replace the original image with the convolved result
        image.internalAssign(convolvedImage);
    }
}
