package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class LuminosityGrayscale implements GrayscaleAlgorithm {
    private static final double LUMINOSITY_RED = 0.21;
    private static final double LUMINOSITY_GREEN = 0.72;
    private static final double LUMINOSITY_BLUE = 0.07;
    public LuminosityGrayscale() {

    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage grayscaleImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color color = new Color(image.getRGB(i, j));

                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                int gray = (int) Math.round(LUMINOSITY_RED * red + LUMINOSITY_GREEN * green + LUMINOSITY_BLUE * blue);
                Color grayColor = new Color(gray, gray, gray);
                grayscaleImage.setRGB(i, j, grayColor.getRGB());
            }
        }

        return grayscaleImage;
    }
}
