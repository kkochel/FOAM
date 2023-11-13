package pl.lodz.uni.biobank.foam.outbox;

import org.apache.sshd.common.config.keys.KeyUtils;
import org.apache.sshd.common.config.keys.PublicKeyEntryDecoder;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.session.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.List;


@Component
public class OutboxAuthenticator implements PublickeyAuthenticator, PasswordAuthenticator {
    private static final Logger log = LoggerFactory.getLogger(OutboxAuthenticator.class);

    private final CegaCredentialsProvider credentialsProvider;

    public OutboxAuthenticator(CegaCredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
    }

    @Override
    public boolean authenticate(String username, String password, ServerSession session) throws PasswordChangeRequiredException {
        Credentials credentials = credentialsProvider.getCredentials(username);
        String hash = credentials.passwordHash();
        boolean isAuthenticated = StringUtils.startsWithIgnoreCase(hash, "$2") && BCrypt.checkpw(password, hash);

        if (isAuthenticated) {
            log.info("The user {} has been authenticated", username);
        } else {
            log.info("User {} has not been authenticated. Password hash {}", username, hash);
        }

        return isAuthenticated;
    }

    @Override
    public boolean authenticate(String username, PublicKey key, ServerSession session) {
        Credentials credentials = credentialsProvider.getCredentials(username);

        if (key == null) {
            log.error("Public key for user {} is empty", username);
        }

        if (credentials.publicKey() == null) {
            log.error("Key from credentials for user {} is empty", username);
            return false;
        }

        List<String> keysList = credentials.publicKey();
        for (String pubKey : keysList) {
            return KeyUtils.compareKeys(getKey(pubKey), key);
        }

        log.error("Key is empty, cannot login user");
        return false;
    }

    private PublicKey getKey(String pubKey) {
        try {
            return readKey(pubKey);
        } catch (IOException | GeneralSecurityException e) {
            log.error("Error while reading the key {}", pubKey);
            throw new RuntimeException(e);
        }
    }

    private PublicKey readKey(String key) throws IOException, GeneralSecurityException {
        String keyType = key.split(" ")[0];
        byte[] keyBytes = Base64.getDecoder().decode(key.split(" ")[1]);
        PublicKeyEntryDecoder<?, ?> publicKeyEntryDecoder = KeyUtils.getPublicKeyEntryDecoder(keyType);
        return publicKeyEntryDecoder.decodePublicKey(null, keyType, keyBytes, null);
    }

}
