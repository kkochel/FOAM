package pl.lodz.uni.biobank.foam.outbox;

import com.jcraft.jsch.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(properties = {
        "application.local.directory=./ega/outbox/"
})
@ActiveProfiles("test")
@Testcontainers
class OutboxApplicationTest {

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "testpassword";
    private static final String TEST_PASSWORD_HASH = "$2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGLcZEiGDMVr5yUP1KUOYTa";

    @Value("${application.port}")
    private int sftpPort;

    @MockBean
    private CegaCredentialsProvider credentialsProvider;

    @BeforeEach
    void setUp() {
        // Mock the credentials provider to return test credentials
        when(credentialsProvider.getCredentials(anyString()))
                .thenReturn(new Credentials(TEST_PASSWORD_HASH, List.of()));
    }

    @Test
    void shouldConnectToSftpServer() throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(TEST_USERNAME, "localhost", sftpPort);
        session.setPassword(TEST_PASSWORD);

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect(5000);

        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect(5000);

        assertThat(channelSftp.isConnected()).isTrue();

        channelSftp.disconnect();
        session.disconnect();
    }

    @Test
    void cantConnectToSsh() throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(TEST_USERNAME, "localhost", sftpPort);
        session.setPassword(TEST_PASSWORD);

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        session.connect(5000);
        // When/Then
        assertThatThrownBy(() -> {
            ChannelShell channelShell = (ChannelShell) session.openChannel("shell");
            channelShell.connect(5000);
        }).isInstanceOf(JSchException.class)
                .hasMessageContaining("failed to send channel request");

        // Cleanup
        session.disconnect();
    }
}