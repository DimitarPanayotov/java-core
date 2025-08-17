# Photo Edge Detector 

Библиотека за обработка на изображения с функционалност за конвертиране в черно-бяло и откриване на ръбове.

## Поддържани формати
- JPEG
- PNG
- BMP

## Основни компоненти

### Алгоритми за обработка
- **LuminosityGrayscale** - конвертиране в черно-бяло (0.21R + 0.72G + 0.07B)
- **SobelEdgeDetection** - откриване на ръбове чрез Sobel оператор

### Файлова система
- **LocalFileSystemImageManager** - зареждане и съхраняване на изображения

## Пример за използване

```java
FileSystemImageManager fsImageManager = new LocalFileSystemImageManager();

BufferedImage image = fsImageManager.loadImage(new File("kitten.png"));

ImageAlgorithm grayscaleAlgorithm = new LuminosityGrayscale();
BufferedImage grayscaleImage = grayscaleAlgorithm.process(image);

ImageAlgorithm sobelEdgeDetection = new SobelEdgeDetection(grayscaleAlgorithm);
BufferedImage edgeDetectedImage = sobelEdgeDetection.process(image);

fsImageManager.saveImage(grayscaleImage, new File("kitten-grayscale.png"));
fsImageManager.saveImage(edgeDetectedImage, new File("kitten-edge-detected.png"));
```

## Пакетна структура

```
src/
└── bg.sofia.uni.fmi.mjt.imagekit/
    ├── algorithm/
    │   ├── detection/
    │   │   ├── EdgeDetectionAlgorithm.java
    │   │   └── SobelEdgeDetection.java
    │   ├── grayscale/
    │   │   ├── GrayscaleAlgorithm.java
    │   │   └── LuminosityGrayscale.java
    │   └── ImageAlgorithm.java
    └── filesystem/
        ├── FileSystemImageManager.java
        └── LocalFileSystemImageManager.java
```