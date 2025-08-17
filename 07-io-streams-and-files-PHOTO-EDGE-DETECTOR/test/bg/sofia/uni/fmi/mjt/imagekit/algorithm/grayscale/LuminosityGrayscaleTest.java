package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class LuminosityGrayscaleTest {

    private LuminosityGrayscale algorithm;

    @BeforeEach
    void setUp() {
        algorithm = new LuminosityGrayscale();
    }

    @Test
    void testProcessThrowsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> algorithm.process(null));
    }

    @Test
    void testProcessReturnsSameSizeImage() {
        BufferedImage original = new BufferedImage(50, 40, BufferedImage.TYPE_INT_RGB);

        BufferedImage result = algorithm.process(original);

        assertEquals(original.getWidth(), result.getWidth());
        assertEquals(original.getHeight(), result.getHeight());
    }

    @Test
    void testPixelsAreGrayscale() {
        BufferedImage original = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Color color = new Color(120, 200, 80);
                original.setRGB(x, y, color.getRGB());
            }
        }

        BufferedImage result = algorithm.process(original);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Color c = new Color(result.getRGB(x, y));
                assertEquals(c.getRed(), c.getGreen(), "Pixel at (" + x + "," + y + ") is not grayscale");
                assertEquals(c.getGreen(), c.getBlue(), "Pixel at (" + x + "," + y + ") is not grayscale");
            }
        }
    }

    @Test
    void testLuminosityCalculationExample() {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Color input = new Color(100, 150, 200);
        img.setRGB(0, 0, input.getRGB());

        BufferedImage result = algorithm.process(img);
        Color output = new Color(result.getRGB(0, 0));

        int expectedGray = (int) Math.round(0.21 * 100 + 0.72 * 150 + 0.07 * 200);

        assertEquals(expectedGray, output.getRed());
        assertEquals(expectedGray, output.getGreen());
        assertEquals(expectedGray, output.getBlue());
    }
}
