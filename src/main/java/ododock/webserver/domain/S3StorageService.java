package ododock.webserver.domain;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

public class S3StorageService implements StorageService {

    private final String bucketName;
    private final S3Client s3Client;

    public S3StorageService(String bucketName, S3Client s3Client) {
        this.bucketName = bucketName;
        this.s3Client = s3Client;
        if (this.s3Client.listBuckets().buckets()
                .stream()
                .noneMatch(bucket ->
                        bucket.name().equals(bucketName))) {
            this.s3Client.createBucket(b -> b.bucket(bucketName));
        }
    }

    @Override
    public byte[] getData(String id, String filename) {
        String key = String.format(bucketName + "/%s/%s", id, filename);

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());

        return objectBytes.asByteArray();
    }

    @Override
    public void saveData(String id, String filename, MultipartFile file) throws IOException {
        String key = String.format(bucketName + "/%s/%s", id, filename);
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );
    }

    @Override
    public void updateData() {

    }

    @Override
    public void deleteData() {

    }

}
