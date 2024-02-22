package hei.school.sarisary;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
@RequestMapping("/blacks")
public class ConvertToBlackAndWhiteController {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        SpringApplication.run(ConvertToBlackAndWhiteController.class, args);
    }

    @GetMapping("/photo")
    public ResponseEntity<String> getBlackAndWhitePhoto() {
        String filePath = "chemin/vers/votre/image.jpg";

        Mat image = Imgcodecs.imread(filePath);

        if (image.empty()) {
            return new ResponseEntity<>("Impossible de charger l'image.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        String outputFilePath = "chemin/vers/votre/image_noir_et_blanc.jpg";
        Imgcodecs.imwrite(outputFilePath, grayImage);

        return new ResponseEntity<>("L'image a été convertie en noir et blanc avec succès.", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBlackAndWhiteImage(@PathVariable String id, @RequestBody String image) {
        return new ResponseEntity<>("Mise à jour de l'image avec l'ID " + id + " effectuée avec succès.", HttpStatus.OK);
    }
}
