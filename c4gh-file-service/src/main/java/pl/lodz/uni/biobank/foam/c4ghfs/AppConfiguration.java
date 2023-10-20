package pl.lodz.uni.biobank.foam.c4ghfs;

import io.minio.MinioClient;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
@EnableRabbit
public class AppConfiguration {
    private @Value("${outbox.path}") String outboxPath;
    private @Value("${crypt4gh.private-key-path}") Resource c4ghSecKeyPath;
    private @Value("${crypt4gh.private-key-password-path}") Resource c4ghPasswordPath;
    private @Value("${archive.path}") String archivePath;
    private @Value("${archive.s3.endpoint}") String s3Endpoint;
    private @Value("${archive.s3.accessKey}") String s3AccessKey;
    private @Value("${archive.s3.secretKey}") String s3SecretKey;

    @Bean
    @ConditionalOnProperty(name = "outbox.type", havingValue = "posix")
    public Outbox posixOutbox() {
        return new PosixOutbox(outboxPath);
    }

    @Bean
    @ConditionalOnProperty(name = "archive.type", havingValue = "posix")
    public Archive posixArchive() {
        return new PosixArchive(archivePath);
    }

    @Bean
    @ConditionalOnProperty(name = "archive.type", havingValue = "s3")
    public Archive s3Archive(MinioClient client) {
        return new S3Archive(client, archivePath);
    }

    @Bean
    public C4ghService service(Archive archive, Outbox outbox) {
        return new C4ghService(archive, outbox);
    }

    @Bean
    public String crypt4ghPrivateKey() {
        try (Reader reader = new InputStreamReader(c4ghSecKeyPath.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load content of file:" + e);
        }
    }

    @Bean
    public String crypt4ghPrivateKeyPassword() {
        try (Reader reader = new InputStreamReader(c4ghPasswordPath.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load content of file:" + e);
        }
    }

    @Bean
    public MessageListener listener(C4ghService service, String crypt4ghPrivateKey, String crypt4ghPrivateKeyPassword) {
        return new MessageListener(service, crypt4ghPrivateKey, crypt4ghPrivateKeyPassword);
    }

    @Bean
    ListenerErrorHandler sdaListenerErrorHandler() {
        return new ListenerErrorHandler();
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(s3Endpoint)
                .credentials(s3AccessKey, s3SecretKey)
                .build();
    }

}
