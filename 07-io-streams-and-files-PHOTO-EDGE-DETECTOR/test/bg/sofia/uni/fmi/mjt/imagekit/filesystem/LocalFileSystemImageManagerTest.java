package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.*;

public class LocalFileSystemImageManagerTest {

    private FileSystemImageManager imageManager;

    @BeforeEach
    void setUp() {
        imageManager = new LocalFileSystemImageManager();
    }

    @Test
    void testLoadImageWithNullFileThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> imageManager.loadImage(null));
    }

    @Test
    void testLoadImageWithUnsupportedFormatThrowsException(@TempDir Path tempDir) {
        File unsupported = new File(tempDir.toFile(), "image.txt");
        try {
            assertTrue(unsupported.createNewFile());
        } catch (IOException e) {
            fail("Couldn't create test file");
        }

        assertThrows(IOException.class, () -> imageManager.loadImage(unsupported));
    }

    @Test
    void testLoadValidImage(@TempDir Path tempDir) throws IOException {
        File testImageFile = createTestImage(tempDir.resolve("test.png").toFile());

        BufferedImage loadedImage = imageManager.loadImage(testImageFile);

        assertNotNull(loadedImage);
        assertEquals(100, loadedImage.getWidth());
        assertEquals(100, loadedImage.getHeight());
    }

    @Test
    void testLoadImagesFromDirectoryWithMixedFiles(@TempDir Path tempDir) throws IOException {
        createTestImage(tempDir.resolve("image1.jpg").toFile());
        createTestImage(tempDir.resolve("image2.png").toFile());
        File textFile = tempDir.resolve("not_image.txt").toFile();
        assertTrue(textFile.createNewFile());

        List<BufferedImage> images = imageManager.loadImagesFromDirectory(tempDir.toFile());

        assertEquals(2, images.size());
    }

    @Test
    void testLoadImagesFromDirectoryNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> imageManager.loadImagesFromDirectory(null));
    }

    @Test
    void testLoadImagesFromInvalidDirectoryThrows(@TempDir Path tempDir) {
        File notADirectory = tempDir.resolve("file.txt").toFile();
        try {
            assertTrue(notADirectory.createNewFile());
        } catch (IOException e) {
            fail("Could not create file");
        }

        assertThrows(IOException.class, () -> imageManager.loadImagesFromDirectory(notADirectory));
    }

    @Test
    void testSaveImage(@TempDir Path tempDir) throws IOException {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        File outFile = tempDir.resolve("saved.jpg").toFile();

        imageManager.saveImage(image, outFile);

        assertTrue(outFile.exists());

        BufferedImage loaded = ImageIO.read(outFile);
        assertNotNull(loaded);
        assertEquals(100, loaded.getWidth());
        assertEquals(100, loaded.getHeight());
    }

    @Test
    void testSaveImageWithNullThrows(@TempDir Path tempDir) {
        File outFile = tempDir.resolve("saved.png").toFile();

        assertThrows(IllegalArgumentException.class, () -> imageManager.saveImage(null, outFile));
        assertThrows(IllegalArgumentException.class, () -> imageManager.saveImage(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB), null));
    }

    @Test
    void testSaveImageToExistingFileThrows(@TempDir Path tempDir) throws IOException {
        File outFile = tempDir.resolve("existing.png").toFile();
        createTestImage(outFile);

        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        assertThrows(IOException.class, () -> imageManager.saveImage(image, outFile));
    }

    @Test
    void testSaveImageToNonExistingParentDirectoryThrows(@TempDir Path tempDir) {
        File nonExistentDir = tempDir.resolve("nonexistent").resolve("img.jpg").toFile();
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        assertThrows(IOException.class, () -> imageManager.saveImage(image, nonExistentDir));
    }

    private File createTestImage(File file) throws IOException {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        boolean written = ImageIO.write(image, "png", file);
        if (!written) {
            throw new IOException("Failed to write test image");
        }
        return file;
    }
}
