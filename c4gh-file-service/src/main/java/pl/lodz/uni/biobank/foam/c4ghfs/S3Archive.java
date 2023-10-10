package pl.lodz.uni.biobank.foam.c4ghfs;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class S3Archive implements Archive {
    private final MinioClient client;
    private final String bucketName;

    public S3Archive(MinioClient client, String bucketName) {
        this.client = client;
        this.bucketName = bucketName;
    }

    @Override
    public InputStream getFile(String path) throws IOException {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(path)
                .build();

        try  {
            return client.getObject(getObjectArgs);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            throw new RuntimeException(e);
        }
    }
}
