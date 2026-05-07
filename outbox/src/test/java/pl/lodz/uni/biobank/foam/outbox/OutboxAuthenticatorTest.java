package pl.lodz.uni.biobank.foam.outbox;

import org.apache.sshd.server.session.ServerSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutboxAuthenticatorTest {

    @Mock
    private CegaUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ServerSession session;

    @InjectMocks
    private OutboxAuthenticator authenticator;

    @Test
    void authenticate_whenUserNotFound_returnsFalse() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        boolean result = authenticator.authenticate("unknown", "anyPassword", session);

        assertThat(result).isFalse();
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void authenticate_whenUserFoundAndPasswordCorrect_returnsTrue() {
        CegaUser user = userWithPassword();
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret", "$2a$10$storedHash")).thenReturn(true);

        boolean result = authenticator.authenticate("alice", "secret", session);

        assertThat(result).isTrue();
        verify(passwordEncoder).matches("secret", "$2a$10$storedHash");
    }

    @Test
    void authenticate_whenUserFoundAndPasswordIncorrect_returnsFalse() {
        CegaUser user = userWithPassword();
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "$2a$10$storedHash")).thenReturn(false);

        boolean result = authenticator.authenticate("alice", "wrongPassword", session);

        assertThat(result).isFalse();
        verify(passwordEncoder).matches("wrongPassword", "$2a$10$storedHash");
    }

    @Test
    void authenticate_passwordIsPassedRawToMatcher_encodeIsNeverCalled() {
        CegaUser user = userWithPassword();
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        authenticator.authenticate("alice", "plaintext", session);

        verify(passwordEncoder, never()).encode(anyString());
        verify(passwordEncoder).matches("plaintext", "$2a$10$storedHash");
    }

    private CegaUser userWithPassword() {
        CegaUser user = new CegaUser();
        user.setPassword("$2a$10$storedHash");
        return user;
    }
}
