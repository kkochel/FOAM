package pl.lodz.uni.biobank.foam.outbox;

import org.apache.sshd.common.file.FileSystemFactory;
import org.apache.sshd.common.util.security.bouncycastle.BouncyCastleGeneratorHostKeyProvider;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.UserAuthPasswordFactory;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.auth.pubkey.UserAuthPublicKeyFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.sftp.server.SftpEventListener;
import org.apache.sshd.sftp.server.SftpSubsystemFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SftpConfig {
    @Value("${application.port}")
    private int outboxPort;

    @Value("${application.keypair}")
    private String outboxKeypair;

    @Bean
    public SshServer sshServer(SftpEventListener sftpEventListener, PasswordAuthenticator passwordAuthenticator, PublickeyAuthenticator publicKeyAuthenticator, FileSystemFactory localFileSystemFactory) throws IOException {
        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setPort(outboxPort);
        sshd.setKeyPairProvider(ObjectUtils.isEmpty(outboxKeypair) ? new SimpleGeneratorHostKeyProvider() : new BouncyCastleGeneratorHostKeyProvider(new File(outboxKeypair).toPath()));
        sshd.setUserAuthFactories(Arrays.asList(new UserAuthPasswordFactory(), new UserAuthPublicKeyFactory()));
        SftpSubsystemFactory sftpSubsystemFactory = new SftpSubsystemFactory();
        sftpSubsystemFactory.addSftpEventListener(sftpEventListener);
        sshd.setSubsystemFactories(Collections.singletonList(sftpSubsystemFactory));
        sshd.setFileSystemFactory(localFileSystemFactory);
        sshd.setPasswordAuthenticator(passwordAuthenticator);
        sshd.setPublickeyAuthenticator(publicKeyAuthenticator);
        sshd.start();
        return sshd;
    }
}
