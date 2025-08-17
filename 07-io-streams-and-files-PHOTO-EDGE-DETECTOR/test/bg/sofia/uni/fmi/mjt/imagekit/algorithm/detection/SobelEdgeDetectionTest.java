package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class SobelEdgeDetectionTest {

    private static final int WIDTH = 3;
    private static final int HEIGHT = 3;

    @Test
    void testProcessWithNullImageThrowsException() {
        SobelEdgeDetection sobel = new SobelEdgeDetection(new LuminosityGrayscale());
        assertThrows(IllegalArgumentException.class, () -> sobel.process(null));
    }

    @Test
    void testOutputImageIsNotNull() {
        BufferedImage testImage = createTestImage();
        SobelEdgeDetection sobel = new SobelEdgeDetection(new LuminosityGrayscale());

        BufferedImage result = sobel.process(testImage);

        assertNotNull(result);
    }

    @Test
    void testOutputImageHasSameDimensions() {
        BufferedImage testImage = createTestImage();
        SobelEdgeDetection sobel = new SobelEdgeDetection(new LuminosityGrayscale());

        BufferedImage result = sobel.process(testImage);

        assertEquals(WIDTH, result.getWidth());
        assertEquals(HEIGHT, result.getHeight());
    }

    @Test
    void testBlackAndWhiteEdgeCase() {
        BufferedImage testImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < 3; y++) {
            testImage.setRGB(0, y, new Color(0, 0, 0).getRGB());
            testImage.setRGB(1, y, new Color(255, 255, 255).getRGB());
            testImage.setRGB(2, y, new Color(255, 255, 255).getRGB());
        }

        SobelEdgeDetection sobel = new SobelEdgeDetection(new LuminosityGrayscale());
        BufferedImage result = sobel.process(testImage);

        Color edgeColor = new Color(result.getRGB(1, 1));

        assertTrue(edgeColor.getRed() > 0, "Expected edge pixel to have non-zero intensity");
    }

    private BufferedImage createTestImage() {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                int gray = (x + y) * 30;
                gray = Math.min(255, gray);
                image.setRGB(x, y, new Color(gray, gray, gray).getRGB());
            }
        }
        return image;
    }
}
