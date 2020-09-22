package uit.media.controller;

import com.amazonaws.services.codecommit.model.File;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uit.media.config.AmazonS3Client;
import uit.media.service.AmazonS3Service;

import java.util.Arrays;

@RestController
public class S3Controller {
    @Autowired
    private AmazonS3Service amazonS3Service;

    @PostMapping("/upload")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return this.amazonS3Service.uploadFile(file);
    }

    @PostMapping("/upload/multiple")
    public String uploadMultipleFile(@RequestPart(value = "file") MultipartFile[] files) {
        Arrays.asList(files).stream().forEach(file -> {
            this.amazonS3Service.uploadFile(file);
        });
        return "Upload multiple images successfully";
    }

    @DeleteMapping("/delete")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.amazonS3Service.deleteFileFromS3Bucket(fileUrl);
    }
}
