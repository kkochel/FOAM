package pl.lodz.uni.biobank.foam.outbox;

import org.apache.sshd.common.file.FileSystemFactory;
import org.apache.sshd.common.util.security.bouncycastle.BouncyCastleGeneratorHostKeyProvider;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.UserAuthPasswordFactory;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.auth.pubkey.UserAuthPublicKeyFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.sftp.server.SftpEventListener;
import org.apache.sshd.sftp.server.SftpSubsystemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(SftpConfig.class);

    @Value("${application.port}")
    private int outboxPort;

    @Value("${application.keypair}")
    private String outboxKeypair;

    @Bean
    public SshServer sshServer(SftpEventListener sftpEventListener, PasswordAuthenticator passwordAuthenticator, PublickeyAuthenticator publicKeyAuthenticator, FileSystemFactory localFileSystemFactory) throws IOException {
        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setPort(outboxPort);
        sshd.setKeyPairProvider(ObjectUtils.isEmpty(outboxKeypair) ? new SimpleGeneratorHostKeyProvider() : new BouncyCastleGeneratorHostKeyProvider(new File(outboxKeypair).toPath()));
        // Only allow password authentication for SFTP connections
        sshd.setUserAuthFactories(Arrays.asList(new UserAuthPasswordFactory()));
        SftpSubsystemFactory sftpSubsystemFactory = new SftpSubsystemFactory();
        sftpSubsystemFactory.addSftpEventListener(sftpEventListener);
        sshd.setSubsystemFactories(Collections.singletonList(sftpSubsystemFactory));
        sshd.setFileSystemFactory(localFileSystemFactory);
        sshd.setPasswordAuthenticator(passwordAuthenticator);
        sshd.setPublickeyAuthenticator(publicKeyAuthenticator);

        // Disable all SSH interactive features to prevent SSH login
        sshd.setShellFactory(null);
        sshd.setCommandFactory(null);
        sshd.setAgentFactory(null);
        sshd.setForwardingFilter(null);

        // Add additional configuration to block SSH connections
        // This ensures that only SFTP connections are allowed
        sshd.setChannelFactories(Collections.emptyList()); // Disable all channel factories except those needed by SFTP


        sshd.start();
        return sshd;
    }
}
