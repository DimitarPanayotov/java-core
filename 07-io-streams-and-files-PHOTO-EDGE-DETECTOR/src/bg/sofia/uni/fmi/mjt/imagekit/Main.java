package bg.sofia.uni.fmi.mjt.imagekit;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection.SobelEdgeDetection;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import bg.sofia.uni.fmi.mjt.imagekit.filesystem.FileSystemImageManager;
import bg.sofia.uni.fmi.mjt.imagekit.filesystem.LocalFileSystemImageManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

public class Main {
    public static void main(String[] args) {
        FileSystemImageManager fsImageManager = new LocalFileSystemImageManager();
        try {
            BufferedImage kittenImage = fsImageManager.loadImage(new File("kitten.png"));
            BufferedImage carImage = fsImageManager.loadImage(new File("car.jpg"));
            ImageAlgorithm grayscaleAlgorithm = new LuminosityGrayscale();

            BufferedImage grayscaleKittenImage = grayscaleAlgorithm.process(kittenImage);
            BufferedImage grayscaleCarImage = grayscaleAlgorithm.process(carImage);

            ImageAlgorithm sobelEdgeDetection = new SobelEdgeDetection(grayscaleAlgorithm);

            BufferedImage edgeDetectedKittenImage = sobelEdgeDetection.process(kittenImage);
            BufferedImage edgeDetectedCarImage = sobelEdgeDetection.process(carImage);

            fsImageManager.saveImage(grayscaleKittenImage, new File("kitten-grayscale.png"));
            fsImageManager.saveImage(edgeDetectedKittenImage, new File("kitten-edge-detected.png"));

            fsImageManager.saveImage(grayscaleCarImage, new File("car-grayscale.png"));
            fsImageManager.saveImage(edgeDetectedCarImage, new File("car-edge-detected.png"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
