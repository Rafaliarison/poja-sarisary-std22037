package hei.school.sarisary.service.event;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
@RestController
@RequestMapping("/black-and-white")
public class BlackAndWhiteController {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        SpringApplication.run(BlackAndWhiteController.class, args);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> convertToBlackAndWhite(@PathVariable String id, @RequestBody byte[] imageData) {
        byte[] convertedImageData = convertImageToBlackAndWhite(imageData);

        String transformedImagePath = "transformed_" + id + ".png";
        try {
            Files.write(Paths.get(transformedImagePath), convertedImageData);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransformationResult> getBlackAndWhiteImage(@PathVariable String id) {
        String originalImageUrl = "https://original.url/" + id + ".png";
        String transformedImageUrl = "https://transformed.url/transformed_" + id + ".png";

        TransformationResult result = new TransformationResult(originalImageUrl, transformedImageUrl);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private byte[] convertImageToBlackAndWhite(byte[] imageData) {
        // Charger l'image
        Mat image = Imgcodecs.imdecode(new Mat(), Imgcodecs.IMREAD_UNCHANGED);

        if (image.empty()) {
            throw new IllegalArgumentException("Impossible de charger l'image.");
        }

        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        Mat outputImage = new Mat();
        Imgcodecs.imencode(".png", grayImage, (MatOfByte) outputImage);
        return outputImage.dump().getBytes();
    }
}

class TransformationResult {
    private String originalUrl;
    private String transformedUrl;

    public TransformationResult(String originalUrl, String transformedUrl) {
        this.originalUrl = originalUrl;
        this.transformedUrl = transformedUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getTransformedUrl() {
        return transformedUrl;
    }
}
