package uit.media.service;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uit.media.config.AmazonS3Client;
import uit.media.entity.Image;
import uit.media.repository.ImageRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class AmazonS3Service {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AmazonS3Client amazonS3Client;

    public List<Image> getPostImages(long postId) {
         return imageRepository.findByPostId(postId);
    }

    public String uploadFile(MultipartFile multipartFile, long postId) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = amazonS3Client.getEndpointUrl() + "/" + amazonS3Client.getBucketName() + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Image image = new Image();
        image.setPostId(postId);
        image.setURL(fileUrl);
        imageRepository.save(image);

        return fileUrl;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        amazonS3Client.getS3client().putObject(new PutObjectRequest(amazonS3Client.getBucketName(), fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public String deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        amazonS3Client.getS3client().deleteObject(new DeleteObjectRequest(amazonS3Client.getBucketName(), fileName));
        return "Successfully deleted";
    }
}
