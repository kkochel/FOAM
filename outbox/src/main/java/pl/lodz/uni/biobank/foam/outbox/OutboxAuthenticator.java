package pl.lodz.uni.biobank.foam.outbox;

import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.session.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class OutboxAuthenticator implements PasswordAuthenticator {
    private static final Logger log = LoggerFactory.getLogger(OutboxAuthenticator.class);

    private final CegaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public OutboxAuthenticator(CegaUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean authenticate(String username, String password, ServerSession session) throws PasswordChangeRequiredException {
        log.info("Authenticating user {}", username);
        Optional<CegaUser> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            log.info("User {} has not been authenticated.", username);
            return false;
        }

        boolean valid = passwordEncoder.matches(password, user.get().getPassword());
        if (!valid) {
            log.info("User {} invalid password.", username);
        }
        return valid;
    }
}
