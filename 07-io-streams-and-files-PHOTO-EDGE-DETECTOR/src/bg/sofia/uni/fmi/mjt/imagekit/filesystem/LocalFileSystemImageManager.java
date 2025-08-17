package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalFileSystemImageManager implements FileSystemImageManager {
    private static final String[] SUPPORTED_FORMATS = {"jpg", "jpeg", "png", "bmp"};

    public LocalFileSystemImageManager() {

    }

    @Override
    public BufferedImage loadImage(File imageFile) throws IOException {
        if (imageFile == null) {
            throw new IllegalArgumentException("Image file cannot be null!");
        }
        if (!imageFile.exists() || !imageFile.isFile() || !isSupportedFormat(imageFile)) {
            throw new IOException("Invalid image file: does not exist, not a file or unsupported format!");
        }

        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IOException("Failed to read image from file!");
        }
        return image;
    }

    @Override
    public List<BufferedImage> loadImagesFromDirectory(File imagesDirectory) throws IOException {
        if (imagesDirectory == null) {
            throw new IllegalArgumentException("Directory cannot be null!");
        }
        if (!imagesDirectory.exists() || !imagesDirectory.isDirectory()) {
            throw new IOException("Invalid directory: does not exist or not a directory!");
        }

        File[] files = imagesDirectory.listFiles();
        if (files == null) {
            throw new IOException("Failed to list files from directory!");
        }

        List<BufferedImage> images = new ArrayList<>();
        for (File file : files) {
            if (file.isFile() && isSupportedFormat(file)) {
                BufferedImage image = ImageIO.read(file);
                if (image != null) {
                    images.add(image);
                }
            }
        }
        return images;
    }

    @Override
    public void saveImage(BufferedImage image, File imageFile) throws IOException {
        if (image == null || imageFile == null) {
            throw new IllegalArgumentException("Image and file cannot be null!");
        }
        if (imageFile.exists()) {
            throw new IOException("File already exists" + imageFile.getPath());
        }

        File parentDir = imageFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            throw new IOException("Parent directory does not exist!");
        }

        String format = getFileExtension(imageFile);
        if (format == null || !isSupportedFormat(format)) {
            throw new IOException("Unsupported file format" + format);
        }

        boolean success = ImageIO.write(image, format, imageFile);
        if (!success) {
            throw new IOException("Failed to write the image!");
        }
    }

    private boolean isSupportedFormat(File file) {
        String extension = getFileExtension(file);
        return extension != null && isSupportedFormat(extension);
    }

    private boolean isSupportedFormat(String extension) {
        for (String supported : SUPPORTED_FORMATS) {
            if (supported.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == name.length() - 1) {
            return null;
        }
        return name.substring(dotIndex + 1).toLowerCase();
    }
}
