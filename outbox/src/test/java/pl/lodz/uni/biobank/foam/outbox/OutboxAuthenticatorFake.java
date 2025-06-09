package pl.lodz.uni.biobank.foam.outbox;

import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.session.ServerSession;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

@Profile("test")
@Component
public class OutboxAuthenticatorFake implements PublickeyAuthenticator, PasswordAuthenticator  {
    @Override
    public boolean authenticate(String username, String password, ServerSession session) throws PasswordChangeRequiredException, AsyncAuthException {
        return true;
    }

    @Override
    public boolean authenticate(String username, PublicKey key, ServerSession session) throws AsyncAuthException {
        return true;
    }
}
