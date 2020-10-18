package uit.media.controller;

import com.amazonaws.services.codecommit.model.File;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uit.media.config.AmazonS3Client;
import uit.media.entity.Image;
import uit.media.service.AmazonS3Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class S3Controller {
    @Autowired
    private AmazonS3Service amazonS3Service;

//    @PostMapping("/upload")
//    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
//        return this.amazonS3Service.uploadFile(file);
//    }

    @PostMapping("/upload/{postId}")
    public List<String> uploadMultipleFile(@RequestPart(value = "images") MultipartFile[] images,
                                     @PathVariable long postId) {
        List<String> response = new ArrayList<>();
        Arrays.asList(images).stream().forEach(file -> {
            String fileUrl = this.amazonS3Service.uploadFile(file, postId);
            response.add(fileUrl);
        });
        return response;
    }

    @GetMapping("/image/{postId}")
    public List<Image> getPostImages(@PathVariable long postId) {
        return amazonS3Service.getPostImages(postId);
    }

    @DeleteMapping("/delete")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.amazonS3Service.deleteFileFromS3Bucket(fileUrl);
    }
}
