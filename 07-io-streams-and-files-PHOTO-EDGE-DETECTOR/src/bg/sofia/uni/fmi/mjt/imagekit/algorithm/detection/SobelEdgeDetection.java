package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class SobelEdgeDetection implements EdgeDetectionAlgorithm {
    private final ImageAlgorithm grayscaleAlgorithm;
    private static final int[][] VERTICAL_SOBEL_KERNEL = {{-1, -2, -1},
        {0, 0, 0},
        {1, 2, 1}};
    private static final int[][] HORIZONTAL_SOBEL_KERNEL = {{-1, 0, 1},
        {-2, 0, 2},
        {-1, 0, 1}};

    public SobelEdgeDetection(ImageAlgorithm grayscaleAlgorithm) {
        if (grayscaleAlgorithm == null) {
            throw new IllegalArgumentException("Grayscale algorithm cannot be null!");
        }
        this.grayscaleAlgorithm = grayscaleAlgorithm;
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null!");
        }

        BufferedImage grayscale = grayscaleAlgorithm.process(image);

        int width = grayscale.getWidth();
        int height = grayscale.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                int sumX = 0;
                int sumY = 0;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int pixel = new Color(grayscale.getRGB(x + i, y + j)).getRed();
                        sumX += VERTICAL_SOBEL_KERNEL[j + 1][i + 1] * pixel;
                        sumY += HORIZONTAL_SOBEL_KERNEL[j + 1][i + 1] * pixel;
                    }
                }
                int magnitude = (int) Math.min(255, Math.hypot(sumX, sumY));
                Color edgeColor = new Color(magnitude, magnitude, magnitude);
                result.setRGB(x, y, edgeColor.getRGB());
            }
        }
        return result;
    }
}
